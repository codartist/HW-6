/**
 *  Justin Fagan
 *  Email: justin.fagan@stonybrook.edu
 *  ID: 112089362
 *
 *  ClosedAuctionException
 *
 *  This is an Exception class that is thrown when the timeRemaining on an auction is 0 and the user tries to place a new bid
 *
 */

public class ClosedAuctionException extends Exception{

    public ClosedAuctionException(){

        super();

    }

    public ClosedAuctionException(String message){

        super(message);

    }

}
