// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #6
// CSE214
// R.04 James Finn

import java.util.*;
import java.io.*;

/**
 * Beginning of the program
 */
public class AuctionSystem implements Serializable {
    public static void main(String[] args) throws IOException {
        AuctionTable aucTable;
        Scanner stdin = new Scanner(System.in);
        char sel;
        System.out.println("Starting...");

        try {
            FileInputStream file = new FileInputStream("auction.obj");
            ObjectInputStream inStream = new ObjectInputStream(file);
            aucTable = (AuctionTable)inStream.readObject();
            inStream.close();
            System.out.println("Loading previous Auction Table...");
        } catch (Exception e) {
            System.out.println("No previous auction table detected.");
            aucTable = new AuctionTable();
            System.out.println("Creating new table...");
        }
        System.out.printf("%nPlease enter a username: ");
        String user = stdin.nextLine();
            do {
                System.out.print("\n Menu: \n \t (D) - Import Data from URL\n\t (A) - Create a New Auction \n \t (B) - Bid on an Item \n \t (I) - Get Info on Auction \n \t (P) - Print All Auctions \n " +
                        "\t (R) - Remove Expired Auctions \n \t (T) - Let Time Pass \n \t (Q) Quit \n Please make a selection from the above menu choices: ");
                String menu = stdin.nextLine();
                if (menu.length() > 0) sel = menu.toUpperCase().charAt(0);
                else sel = '\0';

                switch (sel) {
                    case 'D':
                        System.out.print("Please enter a URL: ");
                        try {
                            String urlScan = stdin.nextLine();
                                System.out.println(urlScan);
                            aucTable = AuctionTable.buildFromURL(urlScan);
                            System.out.printf("Loading...%n %nAuction data loaded successfully!");
                        } catch(IllegalArgumentException IAE) {
                            System.out.println("URL is not valid");
                        }
                        break;

                    case 'A':
                        System.out.printf("%n%nCreating new Auction as " + user + ".");
                        try {
                            System.out.print("Please enter an Auction ID: ");
                            String newID = stdin.nextLine();
                            System.out.print("Please enter an Auction time (hours): ");
                            int timeRemaining = stdin.nextInt();
                            stdin.nextLine();
                            System.out.print("Please enter the Item Info: ");
                            String itemInfo = stdin.nextLine();
                            try {
                                aucTable.putAuction(newID, new Auction(timeRemaining, 0, newID, user, "", itemInfo));
                                System.out.printf("%n%nAuction " + newID + " inserted into the table.");
                            } catch (IllegalArgumentException IAE){
                                System.out.println("This auction ID is already on the table. Please try again!");
                            }
                        } catch (InputMismatchException IME){
                            System.out.print("Incorrect input type please try again!");
                        }
                        break;

                    case 'B':
                        System.out.print("Please enter an Auction ID: ");
                        String bidID = stdin.nextLine();
                        try {
                            if (aucTable.getAuction(bidID).getTimeRemaining() >= 0) {
                                System.out.println("Auction " + bidID + " is OPEN");
                                System.out.printf("Current Bid: $%.3s%n%n", aucTable.getAuction(bidID).getCurrentBid());
                                System.out.print("What would you like to bid: ");
                                double bid = stdin.nextDouble();
                                stdin.nextLine();
                                try {
                                    if (bid < 0){
                                        System.out.println("Your bid cannot be negative!");
                                    } else {
                                        aucTable.getAuction(bidID).newBid(user, bid);
                                        System.out.println("Bid accepted.");
                                    }
                                } catch (ClosedAuctionException CAE) {
                                    System.out.println("You can no longer bid on this item.");
                                }
                            } else if (aucTable.getAuction(bidID).getTimeRemaining() <= 0) {
                                System.out.println("Auction " + bidID + " is CLOSED");
                                System.out.printf("Current Bid: $%.3s\n\n", aucTable.getAuction(bidID).getCurrentBid());
                                System.out.println("You can no longer bid on this item.");
                            }
                        } catch (NullPointerException NPE) {
                            System.out.printf("Invalid auction ID! %n Auction ID %s was not found.", bidID);
                        }
                        break;

                    case 'I':
                        System.out.print("Please enter an Auction ID: ");
                        bidID = stdin.nextLine();
                        Auction info;
                        info = aucTable.getAuction(bidID);
                        if(info == null) {
                            System.out.println("Auction does not exist.");
                        } else {
                            System.out.printf("Auction %s: %n Seller: %s%n Buyer: %s%n Time: %d hours%n Info: %s%n",
                                    bidID, info.getSellerName(), info.getBuyerName(), info.getTimeRemaining(), info.getItemInfo());
                        }
                        break;

                    case 'P':
                        aucTable.printTable();
                        break;

                    case 'R':
                        System.out.printf("%n%nRemoving expired auctions...%n");
                        aucTable.removeExpiredAuctions();
                            System.out.println("All expired auctions removed.");
                        break;

                    case 'T':
                        try {
                            System.out.print("How many hours should pass: ");
                            int time = stdin.nextInt();
                            stdin.nextLine();
                            aucTable.letTimePass(time);
                            System.out.println("\nTime passing...");
                            System.out.println("Auction times updated.");
                        } catch (InputMismatchException IME){
                            System.out.print("Incorrect input type please try again!");
                        }
                        break;
                }
            } while (sel != 'Q' && sel != 'q');
            {
                FileOutputStream file = new FileOutputStream("auction.obj");
                ObjectOutputStream outStream = new ObjectOutputStream(file);
                System.out.println("Writing Auction Table to file...");
                outStream.writeObject(aucTable);
                outStream.close();
                System.out.println("Done.");
                System.out.println("Goodbye.");
                stdin.close();
                System.exit(0);
            }
        }
    }