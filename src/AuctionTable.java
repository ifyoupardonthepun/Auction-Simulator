// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #6
// CSE214
// R.04 James Finn

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;
import big.data.*;

public class AuctionTable extends HashMap<String,Auction> implements Serializable {
    /**
     * A constructor method
     */
    public AuctionTable() {
    }

    /**
     * Uses the BigData library to construct an AuctionTable from a remote data source.
     * @param URL
     * String representing the URL fo the remote data source.
     *
     * @return
     * The AuctionTable constructed from the remote data source.
     *
     * @throws IllegalArgumentException
     * Thrown if the URL does not represent a valid datasource (can't connect or invalid syntax).
     */
    public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException {
        try {
            DataSource ds = DataSource.connect(URL).load();
            String[] sellerName = ds.fetchStringArray("listing/seller_info/seller_name");
            String[] currentBids = ds.fetchStringArray("listing/auction_info/current_bid");
            AtomicReferenceArray<Double> currentBid = new AtomicReferenceArray<>(new Double[currentBids.length]);
            for (int i = 0; i < currentBids.length; i++) {
                currentBid.set(i, Double.parseDouble(currentBids[i].replace('$', '0').replaceAll(",", "")));
            }
            String[] timeLeft = ds.fetchStringArray("listing/auction_info/time_left");
            int[] timeRemaining = new int[timeLeft.length];
            for (int i = 0; i < timeLeft.length; i++) {
                String[] time = timeLeft[i].split(" ");
                int day = Integer.parseInt(String.valueOf(time[0]));
                if(timeLeft[i].contains("hours")) {
                    timeRemaining[i] = day * 24 + Integer.parseInt(String.valueOf(time[2]));
                } else {
                    timeRemaining[i] = day * 24;
                }
            }
            String[] auctionID = ds.fetchStringArray("listing/auction_info/id_num");
            String[] bidderName = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
            String[] memory = ds.fetchStringArray("listing/item_info/memory");
            String[] hardDrive = ds.fetchStringArray("listing/item_info/hard_drive");
            String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
            String[] itemInfo = new String[memory.length];
            for (int i = 0; i < itemInfo.length; i++) {
                itemInfo[i] = cpu[i] + memory[i] + hardDrive[i];
            }
            AuctionTable auction = new AuctionTable();
            for (int i = 0; i < auctionID.length; i++) {
                String bid = currentBids[i].replaceAll(",", "");
                auction.put(auctionID[i], new Auction(timeRemaining[i], Double.parseDouble(bid.substring(bid.indexOf('$') + 1)),
                        auctionID[i], sellerName[i], bidderName[i], itemInfo[i]));
            }
            return auction;
        } catch (Exception e) {
            throw new IllegalArgumentException("The given URL can not be connected to.");
        }
    }

    /**
     * Manually posts an auction, and add it into the table.
     *
     * @param auctionID
     * the unique key for this object
     *
     * @param auction
     * The auction to insert into the table with the corresponding auctionID
     *
     * @throws IllegalArgumentException
     * If the given auctionID is already stored in the table.
     */
    public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException{
        if(keySet().contains(auctionID)) {
            throw new IllegalArgumentException();
        }
        put(auctionID, auction);
    }

    /**
     * Get the information of an Auction that contains the given ID as key
     *
     * @param auctionID
     * the unique key for this object
     *
     * @return
     * An Auction object with the given key, null otherwise.
     */
    public Auction getAuction(String auctionID) {
        return get(auctionID);
    }

    /**
     * Simulates the passing of time. Decrease the timeRemaining of all Auction objects by the amount specified.
     * The value cannot go below 0.
     *
     * @param numHours
     * the number of hours to decrease the timeRemaining value by.
     *
     * @throws IllegalArgumentException
     * If the given numHours is non positive.
     */
    public void letTimePass(int numHours) throws IllegalArgumentException{
        if (numHours < 0) {
            throw new IllegalArgumentException("Hours cannot hold a negative value. Please try again!");
        }
        for (String i: keySet()) {
            getAuction(i).decrementTimeRemaining(numHours);
        }

    }

    /**
     * Iterates over all Auction objects in the table and removes them if they are expired (timeRemaining == 0).
     */
    public void removeExpiredAuctions() {
        String[] removeExpired = new String[keySet().size()];
        int i = 0;
        for (String j : keySet()) {
            if (getAuction(j).getTimeRemaining() <= 0) {
                removeExpired[i] = j;
                i += 1;
            }
        }
        for (i = 0; i < removeExpired.length; i++) {
            if (removeExpired[i] != null) {
                super.remove(removeExpired[i]);
            }
        }
    }

    /**
     * Prints the AuctionTable in tabular form.
     */
    public void printTable() {
        StringBuilder table = new StringBuilder(" Auction ID |    Bid     |        Seller         |          " +
                "Buyer          |   Time    |  Item Info \n");
        table.append("=".repeat(131));
        table.append("\n");
        for (Auction i: values()) {
            table.append(i).append("\n");
        }
        System.out.println(table.substring(0,table.length() - 1));
    }
}
