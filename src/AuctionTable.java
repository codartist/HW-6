import java.io.*;
import java.util.Hashtable;
import big.data.DataSource;
import java.util.Iterator;

/**
 *  Justin Fagan
 *  Email: justin.fagan@stonybrook.edu
 *  ID: 112089362
 *
 *  AuctionTable
 *
 *  The database of open auctions will be stored in a hash table to provide constant time insertion and deletion.
 */
public class AuctionTable extends Hashtable<String,Auction> implements Serializable{


    public AuctionTable(){

        super();

    }

    /**
     * Uses the BigData library to construct an AuctionTable from a remote data source.
     * @param URL String representing the URL fo the remote data source.
     * @return The AuctionTable constructed from the remote data source.
     *
     * Preconditions:
     * URL represents a data source which can be connected to using the BigData library.
     * The data source has proper syntax.
     * @throws IllegalArgumentException
     *          Thrown if the URL does not represent a valid datasource (can't connect or invalid syntax).
     *
     */
    public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException{

        AuctionTable table = new AuctionTable();

        //if (!URL.contains(".xml")) throw new IllegalArgumentException();
        try {
            DataSource ds = DataSource.connect(URL).load();
            String[] durations = ds.fetchStringArray("listing/auction_info/time_left");
            String[] ids = ds.fetchStringArray("listing/auction_info/id_num");
            String[] bids = ds.fetchStringArray("listing/auction_info/current_bid");
            String[] buyers = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
            String[] sellers = ds.fetchStringArray("listing/seller_info/seller_name");
            String[] memory = ds.fetchStringArray("listing/item_info/memory");
            String[] hardDrives = ds.fetchStringArray("listing/item_info/hard_drive");
            String[] cpus = ds.fetchStringArray("listing/item_info/cpu");

            Auction newAuction;

            for (int i = 0; i < ids.length; i++) {
                int timeRemaining = calculateTimeRemaining(durations[i]);
                double currentBid = Double.parseDouble(bids[i].replace("$", "").replace(",",""));
                newAuction = new Auction(timeRemaining, currentBid,ids[i], sellers[i],buyers[i], memory[i] + " - " + hardDrives[i] + " - " + cpus[i]);
                table.putAuction(newAuction.getAuctionID(), newAuction);

            }
        } catch (IllegalArgumentException e){
            System.out.println("Could not load data; invalid URL ");
        }


        return table;
    }

    /**
     * Manually posts an auction, and add it into the table.
     * @param auctionID the unique key for this object
     * @param auction The auction to insert into the table with the corresponding auctionID
     * Postconditions:
     * The item will be added to the table if all given parameters are correct.
     * @throws IllegalArgumentException
     *          If the given auctionID is already stored in the table.
     */
    public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException {
        if (this.containsKey(auctionID)) throw new IllegalArgumentException();
        this.put(auctionID, auction);
    }

    /**
     * Get the information of an Auction that contains the given ID as key
     * @param auctionID the unique key for this object
     * @return An Auction object with the given key, null otherwise.
     */
    public Auction getAuction(String auctionID){
        return this.get(auctionID);

    }

    /**
     *  Simulates the passing of time.
     *  Decrease the timeRemaining of all Auction objects by the amount specified.
     *  The value cannot go below 0.
     *
     * @param numHours the number of hours to decrease the timeRemaining value by.
     * Postconditions:
     * All Auctions in the table have their timeRemaining timer decreased.
     * If the original value is less than the decreased value, set the value to 0.
     * @throws IllegalArgumentException
     *          If the given numHours is non positive
     */
    public void letTimePass(int numHours) throws IllegalArgumentException{
        if (numHours < 0) throw new IllegalArgumentException();
        for (String key : this.keySet()){
            getAuction(key).decrementTimeRemaining(numHours);
        }
    }

    /**
     * Iterates over all Auction objects in the table and removes them if they are expired (timeRemaining == 0).
     * Postconditions:
     *      Only open Auction remain in the table.
     */
    public void removeExpiredAuctions(){

        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()){
            String key = itr.next();
            if (getAuction(key).getTimeRemaining() == 0){
                itr.remove();
            }

        }
    }

    /**
     * Prints the AuctionTable in tabular form.
     */
    public void printTable(){
        System.out.println("Auction ID |      Bid   |        Seller         |          Buyer          |    Time   |  Item Info");
        System.out.println("===================================================================================================================================");
        for (String key: this.keySet()){
            System.out.println(getAuction(key).toString());
        }
    }

    /**
     * Parses the String and converts the time from days and hours to an int value in hours
     * @param time the time value parsed from big data
     * @return the timeRemaining in hours as an int
     *
     */
    public static int calculateTimeRemaining(String time){
        int hours = 0;
        String[] factor = time.replace(",", "").replace("+", "").split(" ");
        if (factor.length == 2){
            if(factor[1].equals("days") || factor[1].equals("day")){
                hours += Integer.parseInt(factor[0]) * 24;
            } else if(factor[1].equals("hours") || factor[1].equals("hour")){
                hours += Integer.parseInt(factor[0]);
            }
        }
        else if(factor.length == 4){
            hours += Integer.parseInt(factor[0]) * 24 + Integer.parseInt(factor[2]);
        }
        return hours;
    }

}