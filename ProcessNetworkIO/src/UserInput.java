import java.util.Scanner;

/**
 * 
 * @author bhanu
 *
 */
public class UserInput {

	String trafficSourceLoc;
	
	public String getTrafficSourceLoc() {
		return trafficSourceLoc;
	}

	public void setTrafficSourceLoc(String trafficSourceLoc) {
		this.trafficSourceLoc = trafficSourceLoc;
	}

	String trafficDestLoc;

	public String getTrafficDestLoc() {
		return trafficDestLoc;
	}

	public void setTrafficDestLoc(String trafficDestLoc) {
		this.trafficDestLoc = trafficDestLoc;
	}
	
	String whitelistInputFilesLoc;
	
	public String getWhitelistInputFilesLoc() {
		return whitelistInputFilesLoc;
	}

	public void setWhitelistInputFilesLoc(String whitelistInputFilesLoc) {
		this.whitelistInputFilesLoc = whitelistInputFilesLoc;
	}
	
	String blacklistInputFilesLoc;

	public String getBlacklistInputFilesLoc() {
		return blacklistInputFilesLoc;
	}

	public void setBlacklistInputFilesLoc(String blacklistInputFilesLoc) {
		this.blacklistInputFilesLoc = blacklistInputFilesLoc;
	}
	
	String ioFilePath;

	public String getIoFilePath() {
		return ioFilePath;
	}

	public void setIoFilePath(String ioFilePath) {
		this.ioFilePath = ioFilePath;
	}

	/**
	 * Constructor for initializing the attributes
	 */
	public UserInput() {

//		trafficSourceLoc = "C:\\\\Users\\\\bhanu\\\\Documents\\\\Fiddler2\\\\Captures\\\\test11.saz";
//		trafficDestLoc = "C:\\\\Users\\\\bhanu\\\\Documents\\\\Fiddler2\\\\Captures\\\\test11";

		trafficSourceLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\BlacklistTraffic.har";
		trafficDestLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\ProcessedFiles\\\\Blacklist";
		
		whitelistInputFilesLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\InputFiles\\\\WhitelistFiles";;
		blacklistInputFilesLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\InputFiles\\\\BlacklistFiles";
		
		ioFilePath = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Inputs\\\\Input_ZAP_XSS_Payloads.txt";

	}

	/**
	 * This method prompts the user for input and accepts the user input and
	 * saves them in appropriate variables
	 */
	public void getUserInput() {
		Scanner input = new Scanner(System.in);

		System.out.println("Please enter the location of network traffic source file: ");
		trafficSourceLoc = input.nextLine();

		System.out.println("Please enter the location for saving the resulting files: ");
		trafficDestLoc = input.nextLine();

		input.close();
		
		System.out.println("The value of source location before processing is: " +trafficSourceLoc);

		System.out.println("The value of empty is: " + trafficSourceLoc.equalsIgnoreCase(""));
		
		System.out.println("The value of destination location before processing is: " +trafficDestLoc);

		System.out.println("The value of empty is: " + trafficDestLoc.equalsIgnoreCase(""));

		processUserInput();

		System.out.println("The values after processing is: " +trafficSourceLoc);
		
		System.out.println("The values after processing is: " +trafficDestLoc);
		
		
	}

	/**
	 * This method processes the accepted user input and puts it in a usable
	 * format
	 */
	public void processUserInput() {
		if (!(trafficSourceLoc.equalsIgnoreCase(""))) {
			trafficSourceLoc = trafficSourceLoc.replace("/", "\\\\");
		}
		if (!(trafficDestLoc.equalsIgnoreCase(""))) {
			trafficDestLoc = trafficDestLoc.replace("/", "\\\\");
		}
		if (((trafficSourceLoc.equalsIgnoreCase("")) || (trafficDestLoc.equalsIgnoreCase("")))) {

			trafficSourceLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\BlacklistTraffic.har";
			trafficDestLoc = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Source\\\\ProcessedFiles\\\\Blacklist";
	
		}
	}

}
