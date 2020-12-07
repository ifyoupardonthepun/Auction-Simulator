// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #6
// CSE214
// R.04 James Finn

import java.io.*;

public class Auction implements Serializable {
    private int timeRemaining;
    private double currentBid;
    private final String auctionID, sellerName, itemInfo;
    private String buyerName;

    /**
     * Constructor with parameters given
     *
     * @param timeRemaining
     * Time remaining for the auction listing.
     *
     * @param currentBid
     * The current highest bid.
     *
     * @param auctionID
     * The identification number for the auction listing.
     *
     * @param sellerName
     * The name of the seller for the auction listing.
     *
     * @param buyerName
     * The name of the buyer for the auction listing
     *
     * @param itemInfo
     * Information about the item.
     */
    public Auction(int timeRemaining, double currentBid, String auctionID, String sellerName, String buyerName, String itemInfo) {
        this.timeRemaining = timeRemaining;
        this.currentBid = Double.parseDouble(String.valueOf(currentBid));
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.itemInfo = itemInfo;
    }

    /**
     *
     * @return
     * Returns the time remaining for the auction listing.
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     *
     * @return
     * Returns the highest bidder for the auction listing.
     */
    public String getCurrentBid() {
        return String.valueOf(currentBid);
    }

    /**
     *
     * @return
     * Returns the seller name for the auction listing.
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     *
     * @return
     * Returns the buyer name for the auction listing.
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     *
     * @return
     * Returns the item info for the auction listing.
     */
    public String getItemInfo() {
        return itemInfo;
    }

    /**
     * Decreases the time remaining for this auction by the specified amount.
     * If time is greater than the current remaining time for the auction, then the time remaining is set to 0 (i.e. no negative times).
     *
     * @param time
     * has been decremented by the indicated amount and is greater than or equal to 0.
     */
    public void decrementTimeRemaining(int time) {
        if(timeRemaining < 0) {
            timeRemaining = 0;
        } else
            timeRemaining -= time;
    }

    /**
     * Makes a new bid on this auction. If bidAmt is larger than currentBid, then the value of currentBid is replaced
     * by bidAmt and buyerName is is replaced by bidderName.
     *
     * @param bidderName
     * The name of the bidder that is bidding for the auction listing.
     *
     * @param bidAmt
     * The amount that is being bid for the auction listing.
     *
     * @throws ClosedAuctionException
     *  Thrown if the auction is closed and no more bids can be placed (i.e. timeRemaining == 0).
     */
    public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException {
        if(timeRemaining == 0) {
            throw new ClosedAuctionException();
        } else if(bidAmt > currentBid) {
            currentBid = Double.parseDouble(String.valueOf(bidAmt));
            buyerName = bidderName;
        }
    }

    /**
     *
     * @return
     * returns string of data members in tabular form.
     */
    public String toString() {
        return String.format(" %5s  | %10s | %-21s | %-23s | %3d hours |  %5s",
                auctionID, currentBid, sellerName, buyerName, timeRemaining, itemInfo);
    }
}