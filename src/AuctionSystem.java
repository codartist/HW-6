import java.io.*;
import java.util.Scanner;

/**
 *  Justin Fagan
 *  Email: justin.fagan@stonybrook.edu
 *  ID: 112089362
 *
 *  AuctionSystem
 *
 *  This class will allow the user to interact with the database by listing open auctions,
 *  make bids on open auctions, and create new auctions for different items.
 *
 */
public class AuctionSystem implements Serializable {

    private static AuctionTable auctionTable;
    private static boolean running = true;
    private static String username;

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Starting...");
        System.out.println("Checking for previous tables...");
        initialize();

        System.out.println("Please select a username: ");

        username = scanner.next();

        System.out.println("" +
                "(D) - Import Data from URL \n" +
                "(A) - Create a New Auction \n" +
                "(B) - Bid on an Item \n" +
                "(I) - Get Info on Auction \n" +
                "(P) - Print All Auctions \n" +
                "(R) - Remove Expired Auctions \n" +
                "(T) - Let Time Pass \n" +
                "(Q) - Quit \n");

        while (isRunning()){
            System.out.print("Please select an option: ");
            String option = scanner.next();
            runner(option, scanner);
        }
        System.out.println("Goodbye.");

    }

    /**
     * Saves the Auction Table
     */
    public static void save(){

        try {
            FileOutputStream file = new FileOutputStream("auctions.obj");
            ObjectOutputStream outStream = new ObjectOutputStream(file);
            AuctionTable auctions = new AuctionTable();
            if (auctionTable != null){
                auctions.putAll(auctionTable);
            }
            outStream.writeObject(auctions);
            outStream.close();
            file.close();
            System.out.println("Done!");
            System.out.println();
            //System.out.println("Serialized data is saved in auctions.obj");
        } catch (IOException i) {
            System.out.println("There was an error saving the Auction Table");
        }
    }

    /**
     * Initializes the auction table if there was a previous table detected in the project directory
     */
    public static void initialize(){

        AuctionTable auctions;
        try {
            FileInputStream fileIn = new FileInputStream("auctions.obj");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            auctions = (AuctionTable) in.readObject();
            auctionTable = auctions;
            in.close();
            fileIn.close();
            System.out.println("Loading previous Auction Table...");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous tables detected");
            System.out.println("Creating new table...");
        }

    }

    /**
     * A boolean that controls the loop of the simulation
     * @return the boolean that determines if the program is running
     */
    public static boolean isRunning() {

        return running;

    }

    /**
     * Sets the boolean so that you can break out of the while loop
     * @param running the boolean that changes the status of the simulation
     */
    public static void setRunning(boolean running) {

        AuctionSystem.running = running;

    }

    /**
     * A runner method that handles each of the
     * @param option an option from the menu
     * @param scanner the scanner that reads the user's input
     */
    public static void runner( String option, Scanner scanner){

        switch (option.toUpperCase()){

            case "D":
                System.out.print("Please enter a URL: ");
                String url = scanner.next();

                auctionTable = AuctionTable.buildFromURL(url);
                System.out.println("Loading...");
                System.out.println("Auction data loaded successfully!");
                break;
            case "A":
                System.out.println("Creating new Auction as " + username);
                System.out.println("Please enter an Auction ID (It should be a numerical id): ");
                String id = scanner.next();
                System.out.println("Please enter an Auction time (hours): ");
                int time = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Please enter some Item Info: ");
                String info = scanner.nextLine();

                try{
                    auctionTable.putAuction(id, new Auction(time, id, username ,info));
                } catch (IllegalArgumentException e){
                    System.out.println("This table already contains that id");
                }

                break;
            case "B":
                System.out.println("Please enter an Auction ID: ");
                String auctionID = scanner.next();

                if (auctionTable.getAuction(auctionID).getTimeRemaining() != 0){
                    System.out.println("Auction " + auctionID + " is OPEN");
                    if (auctionTable.getAuction(auctionID).getCurrentBid() == 0.0){
                        System.out.println("\t Current Bid: None");
                    } else {
                        System.out.println("\t Current Bid: " + auctionTable.getAuction(auctionID).getCurrentBid());
                    }

                    System.out.println("What would you like to bid? ");
                    double newBid = scanner.nextDouble();
                    try {
                        auctionTable.getAuction(auctionID).newBid(username,newBid);
                    } catch (ClosedAuctionException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Bid Accepted");
                } else {
                    System.out.println("Auction " + auctionID + " is CLOSED");
                    if (auctionTable.getAuction(auctionID).getCurrentBid() == 0.0){
                        System.out.println("\t Current Bid: None");
                    } else {
                        System.out.println("\t Current Bid: " + auctionTable.getAuction(auctionID).getCurrentBid());
                    }
                    System.out.println();
                    System.out.println("You can no longer bid on this item.");
                }
                break;
            case "I":
                System.out.println("Please enter an Auction ID: ");
                Auction auctionData = auctionTable.getAuction(scanner.next());

                System.out.println("Auction " + auctionData.getAuctionID() + ":");
                System.out.println("\t Seller: " + auctionData.getSellerName());
                System.out.println("\t Buyer: " + auctionData.getBuyerName());
                System.out.println("\t Time: " + auctionData.getTimeRemaining());
                System.out.println("\t Info: " + auctionData.getItemInfo());
                break;
            case "P":
                auctionTable.printTable();
                break;
            case "R":
                System.out.println("Removing expired auctions");
                auctionTable.removeExpiredAuctions();
                System.out.println("All expired auctions removed.");
                break;
            case "T":
                System.out.print("How many hours should pass: ");
                int duration = scanner.nextInt();
                auctionTable.letTimePass(duration);

                System.out.println("Time passing...");
                System.out.println("Auction times updated.");
                break;
            case "Q":
                System.out.println("Writing Auction Table to file...");
                save();
                setRunning(false);
                break;
            default:
                System.out.println("Make sure you are inputting a proper option from the menu");
                System.out.println("" +
                        "(D) - Import Data from URL \n" +
                        "(A) - Create a New Auction \n" +
                        "(B) - Bid on an Item \n" +
                        "(I) - Get Info on Auction \n" +
                        "(P) - Print All Auctions \n" +
                        "(R) - Remove Expired Auctions \n" +
                        "(T) - Let Time Pass \n" +
                        "(Q) - Quit \n");
                break;
        }

    }
}
