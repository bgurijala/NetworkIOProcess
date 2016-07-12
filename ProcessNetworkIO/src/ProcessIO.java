import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import nl.flotsam.xeger.Xeger;

/**
 * 
 */

/**
 * @author bhanu
 *
 */
public class ProcessIO {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		UserInput userInput = new UserInput();

		userInput.getUserInput();

		// unzip(userInput.getTrafficSourceLoc(),
		// userInput.getTrafficDestLoc()); //Uncomment it when using fiddler.

		// Parse har file in JSON format

		parseNwIOFile(userInput.getTrafficDestLoc(), userInput.getTrafficSourceLoc());
		
//		writeWhitelistInputFiles(userInput.getWhitelistInputFilesLoc());
		
	//	writeBlacklistInputFiles(userInput.getBlacklistInputFilesLoc());

		// Parse all client files and write relevant information to specified
		// location

		// Parse all server files and write relevant information to specified
		// location

	}

	/**
	 * Unzipping code that will be used if fiddler is used to unzip and extract
	 * the saz file.
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	/*
	 * public static void unzip(String zipFilePath, String destDirectory) throws
	 * IOException { File destDir = new File(destDirectory); if
	 * (!destDir.exists()) { destDir.mkdir(); } ZipInputStream zipIn = new
	 * ZipInputStream(new FileInputStream(zipFilePath)); ZipEntry entry =
	 * zipIn.getNextEntry(); // iterates over entries in the zip file while
	 * (entry != null) { String filePath = destDirectory + File.separator +
	 * entry.getName(); if (!entry.isDirectory()) { // if the entry is a file,
	 * extracts it extractFile(zipIn, filePath); } else { // if the entry is a
	 * directory, make the directory File dir = new File(filePath); dir.mkdir();
	 * } zipIn.closeEntry(); entry = zipIn.getNextEntry(); } zipIn.close(); }
	 */
	/**
	 * Used for extracting files from saz format written by fiddler
	 *
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	/*
	 * private static void extractFile(ZipInputStream zipIn, String filePath)
	 * throws IOException { BufferedOutputStream bos = new
	 * BufferedOutputStream(new FileOutputStream(filePath)); byte[] bytesIn =
	 * new byte[BUFFER_SIZE]; int read = 0; while ((read = zipIn.read(bytesIn))
	 * != -1) { bos.write(bytesIn, 0, read); } bos.close(); }
	 */

	/**
	 * 
	 * @param sourceFilePath
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void parseNwIOFile(String destLoc, String sourceFilePath) throws IOException, ParseException {

		FileReader reader = new FileReader(sourceFilePath);

		JSONParser jsonParser = new JSONParser();

		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

		JSONObject log = (JSONObject) jsonObject.get("log");

		JSONArray entries = (JSONArray) log.get("entries");

		int k = 0;
		int fileSeqNo = 1;
		for (Object obj : entries) {
			System.out.println("The loop is running for: " + k++);

			JSONObject entry = (JSONObject) obj;

			String entryTimeStamp = (String) entry.get("startedDateTime");
			System.out.println(
					"The start time of the entry that corresponds to this request and response is: " + entryTimeStamp);

			// Handle request related information of an entry
			JSONObject request = (JSONObject) entry.get("request");

			String method = (String) request.get("method");
			System.out.println("The method of the request is: " + method);
			
			String url = (String) request.get("url");
			
			if(!(url.equals("http://localhost/collabtive-12/manageuser.php?action=login"))){

			JSONObject postData = (JSONObject) request.get("postData");

			JSONArray postedParams = (JSONArray) postData.get("params");

			if (postedParams.size() > 0) {

				// Write request data to a file
				String reqFilePath = destLoc + "\\\\" + fileSeqNo + "_request.txt";

				System.out.println("The generated path is: " + reqFilePath);

				File reqFile = new File(reqFilePath);

				if (!reqFile.exists()) {
					reqFile.createNewFile();
				}

				Writer writer = new FileWriter(reqFile);

				BufferedWriter bufferedWriter = new BufferedWriter(writer);

				bufferedWriter.write(entryTimeStamp);
				bufferedWriter.newLine();

				Iterator i = postedParams.iterator();

				// take each value from the json array separately
				while (i.hasNext()) {
					JSONObject innerObj = (JSONObject) i.next();
					System.out.println("param-name: " + innerObj.get("name") + " with value " + innerObj.get("value"));
					bufferedWriter.write((String) innerObj.get("name") + " : " + innerObj.get("value").toString().replace("+", " "));
					bufferedWriter.newLine();

				}

				bufferedWriter.close();
			}
			}
			// Handle response related information of an entry
			JSONObject response = (JSONObject) entry.get("response");

			JSONObject content = (JSONObject) response.get("content");

			String mimeType = (String) content.get("mimeType");

			System.out.println("The mime type retrieved is: " + mimeType);
			
			if(mimeType != null){
			
			if (mimeType.contains("text/html")) {

				String contextText = (String) content.get("text");

				if (contextText != null) {

					if (contextText.contains("html")) {

						if (contextText != null) {
							System.out.println("The value of the response is: " + contextText);
							ArrayList<String> processedIO = processResponse(contextText);
							
							if(processedIO.size() > 0){
								// Write request data to a file
								String resFilePath = destLoc + "\\\\" + fileSeqNo + "_response.txt";

								System.out.println("The generated path is: " + resFilePath);

								File respFile = new File(resFilePath);

								if (!respFile.exists()) {
									respFile.createNewFile();
								}

								Writer writer = new FileWriter(respFile);

								BufferedWriter bufferedWriter = new BufferedWriter(writer);

								bufferedWriter.write(entryTimeStamp);
								bufferedWriter.newLine();

	//						}

							// if(processedIO.size() > 0){
							for (int j = 0; j < processedIO.size(); j = j + 2) {
								bufferedWriter.write("name : ");
								bufferedWriter.write(processedIO.get(j));
								bufferedWriter.newLine();
								bufferedWriter.write("desc : ");
								bufferedWriter.write(processedIO.get(j + 1));
								bufferedWriter.newLine();
							}
							bufferedWriter.close();
							fileSeqNo++;
							}

						}

					}
					
				}
			}
		}

		}

	}

	/**
	 * 
	 * @param respString
	 * @return
	 */
	public static ArrayList<String> processResponse(String respString) {

		String response = respString;

		ArrayList<String> processedIO = new ArrayList<String>();

		while (response.length() > 1) {

			int index = response.indexOf("<a href=\"manageproject.php?action=showproject&amp;id=");

			if (index != -1) {

				response = response.substring(index);
				// System.out.println("The new response string is: "+response);

				int beginIndex = response.indexOf(">");
				int endIndex = response.indexOf("</a>");

				String pName = response.substring(beginIndex + 1, endIndex);

				pName = pName.trim();

				System.out.println("The project name retrieved is: " + pName);

				processedIO.add(pName);

				response = response.substring(endIndex + 4);

				int stIndex = response.indexOf("<div class=\"acc-in\">");
				int startInd = -1, endInd = -1;
				if (stIndex > -1) {
					startInd = stIndex + 20;
					endInd = response.indexOf("<p");
					System.out.println("endInd value is: "+endInd);
					if(endInd < 0){
						response = response.substring(startInd);
						startInd = response.indexOf("<div class=\"message-in\">")+24;
						System.out.println("The value of new start index is: "+startInd);
						endInd = response.indexOf("</div>");
						System.out.println("The value os new end Ind is: "+endInd);
					}
					String pDesc = response.substring(startInd, endInd);
					pDesc = pDesc.trim();
					System.out.println("The project Description is: " + pDesc);
					processedIO.add(pDesc);
				}
				if (endInd != -1) {
					response = response.substring(endInd);
				} else {
					response = response.substring(endIndex + 4);
				}
			} else {
				if (response.length() > 30) {
					response = response.substring(20);
				} else {
					break;
				}
			}
		}

		return processedIO;
	}

	/**
	 * 
	 * @param destLoc
	 * @throws IOException
	 */
	public static void writeWhitelistInputFiles(String destLoc) throws IOException{

		ArrayList<String> resultName = new ArrayList<String>();
		ArrayList<String> resultDesc = new ArrayList<String>();
		ArrayList<String> resultBudget = new ArrayList<String>();
		ArrayList<String> resultDate = new ArrayList<String>();
		String resString = "";

		String nameFormat = "[a-zA-Z0-9]{2,5}[a-zA-Z0-9 '.:]{5,25}";
		String descFormat = "[a-zA-Z0-9 ,.?/<>?:;\"\'\\+-_)(*&^%$#@!={}]{20,255}";
		String budgetFormat = "[0-9]{1,3},?[0-9]{3,3}";
		String dateFormat = "[1-31]{1,2}[.]{1,1}[1-12]{1,2}[.]{1,1}[1901-2102]{4,4}";
		
		Xeger generator = new Xeger(nameFormat);
		for(int i = 0; i < 150; i++){
			resString = generator.generate();
			System.out.println("The generated string is: "+resString);
			resultName.add(i, resString);
		}
		
		generator = new Xeger(descFormat);
		for(int i = 0; i < 150; i++){
			resString = generator.generate();
			System.out.println("The generated desc string is: "+resString);
			resultDesc.add(i, resString);
		}
				
		generator = new Xeger(budgetFormat);
		for(int i = 0; i < 150; i++){
			resString = generator.generate();
			System.out.println("The generated budget string is: "+resString);
			resultBudget.add(i, resString);
		}
		
		generator = new Xeger(dateFormat);
		for(int i = 0; i < 150; i++){
			resString = generator.generate();
			System.out.println("The generated date string is: "+resString);
			resultDate.add(i, resString);
		}
		
		int fileSeqNo = 1;
		for(int i = 0; i < 150; i++){
			String inputFilePath = destLoc + "\\\\" + fileSeqNo + "_whitelist_input.txt";

			System.out.println("The generated path is: " + inputFilePath);

			File inputFile = new File(inputFilePath);

			if (!inputFile.exists()) {
				inputFile.createNewFile();
			}

			Writer writer = new FileWriter(inputFile);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write("name : "+resultName.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("desc : "+resultDesc.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("end : "+resultDate.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("budget : "+resultBudget.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("assignto[] : 1");
			bufferedWriter.newLine();
			bufferedWriter.write("assignme : 1");
			bufferedWriter.newLine();

			bufferedWriter.close();
			fileSeqNo++;
		}

	}
	
	/**
	 * 
	 * @param destLoc
	 * @throws IOException
	 */
	public static void writeBlacklistInputFiles(String destLoc) throws IOException{

		ArrayList<String> resultName = new ArrayList<String>();
		ArrayList<String> resultDesc = new ArrayList<String>();
		ArrayList<String> resultBudget = new ArrayList<String>();
		ArrayList<String> resultDate = new ArrayList<String>();
		String resString = "";
		String ioFilePath = "C:\\\\Users\\\\bhanu\\\\OneDrive\\\\Research\\\\Implementation\\\\Inputs\\\\Input_ZAP_XSS_Payloads.txt";

		String nameFormat = "[a-zA-Z0-9]{2,5}[a-zA-Z0-9 '.:]{5,25}";
		String budgetFormat = "[0-9]{1,3},?[0-9]{3,3}";
		String dateFormat = "[1-31]{1,2}[.]{1,1}[1-12]{1,2}[.]{1,1}[1901-2102]{4,4}";

		resultDesc = readIO(ioFilePath);
		
		Xeger generator = new Xeger(nameFormat);
		for(int i = 0; i < resultDesc.size(); i++){
			resString = generator.generate();
			resultName.add(i, resString);
		}
		

		
		generator = new Xeger(budgetFormat);
		for(int i = 0; i < resultDesc.size(); i++){
			resString = generator.generate();
			resultBudget.add(i, resString);
		}
		
		generator = new Xeger(dateFormat);
		for(int i = 0; i < resultDesc.size(); i++){
			resString = generator.generate();
			resultDate.add(i, resString);
		}
		
		int fileSeqNo = 1;
		for(int i = 0; i < resultDesc.size(); i++){
			String inputFilePath = destLoc + "\\\\" + fileSeqNo + "_blacklist_input.txt";

			System.out.println("The generated path is: " + inputFilePath);

			File inputFile = new File(inputFilePath);

			if (!inputFile.exists()) {
				inputFile.createNewFile();
			}

			Writer writer = new FileWriter(inputFile);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write("name : "+resultName.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("desc : "+resultDesc.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("end : "+resultDate.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("budget : "+resultBudget.get(i));
			bufferedWriter.newLine();
			bufferedWriter.write("assignto[] : 1");
			bufferedWriter.newLine();
			bufferedWriter.write("assignme : 1");
			bufferedWriter.newLine();

			bufferedWriter.close();
			fileSeqNo++;
		}

	}
	
	/**
	 * 
	 * @param ioFilePath
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> readIO(String ioFilePath) throws IOException {
		ArrayList<String> io = new ArrayList<String>();

		FileInputStream fis = new FileInputStream(ioFilePath);

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;

		while ((line = br.readLine()) != null) {
			io.add(line);
		}
		br.close();

		return io;
	}
}
