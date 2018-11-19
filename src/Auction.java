import java.io.Serializable;

/**
 *  Justin Fagan
 *  Email: justin.fagan@stonybrook.edu
 *  ID: 112089362
 *
 *  Auction
 *
 *  Represents an active auction currently in the database.
 */
public class Auction implements Serializable {

    private int timeRemaining;
    private double currentBid;
    private String auctionID;
    private String sellerName;
    private String buyerName;
    private String itemInfo;

    /**
     * The Auction constructor for an Auction object mainly used to create a an Auction object from big data
     * @param timeRemaining The time remaining for the auction
     * @param currentBid highest bid for the item being auctioned
     * @param auctionID The unique identifier that maps to the auction object
     * @param sellerName The name of the seller
     * @param buyerName The name of the buyer
     * @param itemInfo A brief description of the item being auctioned
     */
    public Auction(int timeRemaining, double currentBid, String auctionID, String sellerName, String buyerName, String itemInfo) {
        this.timeRemaining = timeRemaining;
        this.currentBid = currentBid;
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.itemInfo = itemInfo;
    }

    /**
     * the Auction constructor for an Auction object that is manually placed in the Auction Table
     * @param timeRemaining The time remaining for the auction
     * @param auctionID The unique identifier that maps to the auction object
     * @param sellerName The name of the seller
     * @param itemInfo A brief description of the item being auctioned
     */
    public Auction(int timeRemaining, String auctionID, String sellerName, String itemInfo){
        this.timeRemaining = timeRemaining;
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.buyerName = "         ";
        this.itemInfo = itemInfo;
    }

    /**
     * @return the time remaining
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * @return the current bid on the item
     */
    public double getCurrentBid() {
        return currentBid;
    }

    /**
     * @return the auction ID
     */
    public String getAuctionID() {
        return auctionID;
    }

    /**
     * @return the name of the seller
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * @return the name of the buyer
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * @return the item info
     */
    public String getItemInfo() {
        return itemInfo;
    }

    /**
     * Decreases the time remaining for this auction by the specified amount.
     * If time is greater than the current remaining time for the auction,
     * then the time remaining is set to 0 (i.e. no negative times).
     *
     * @param time the time to decrement by
     *
     * Postconditions:
     *             timeRemaining has been decremented by the indicated amount and is greater than or equal to 0.
     */
    public void decrementTimeRemaining(int time){
        timeRemaining -= time;
        if (timeRemaining < 0){
            timeRemaining = 0;
        }
    }

    /**
     *  Makes a new bid on this auction.
     *  If bidAmt is larger than currentBid,
     *  then the value of currentBid is replaced by bidAmt and buyerName is is replaced by bidderName.
     *
     * @param bidderName the name of the new bidder
     * @param bidAmt the bid amount
     *
     * Preconditions:
     *               The auction is not closed (i.e. timeRemaining > 0).
     *
     * Postconditions:
     *               currentBid Reflects the largest bid placed on this object.
     *               If the auction is closed, throw a ClosedAuctionException.
     *
     * @throws ClosedAuctionException
     *         Thrown if the auction is closed and no more bids can be placed (i.e. timeRemaining == 0).
     */
    public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException{

        if (timeRemaining == 0) throw new ClosedAuctionException();
        if( bidAmt > currentBid){
            currentBid = bidAmt;
            buyerName = bidderName;
        }

    }

    /**
     * Returns string of data members in tabular form.
     * @return  string of data members in tabular form.
     */
    @Override
    public String toString(){

        return auctionID + "  | $ " +
                currentBid + "    | " +
                sellerName + " | " +
                buyerName + " | " +
                timeRemaining + " hours | " +
                itemInfo;

    }


}
