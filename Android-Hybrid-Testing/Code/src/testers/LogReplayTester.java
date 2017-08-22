package testers;

import serverDriver.SelendroidServerDriver;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import logger.LogFileGenerator;

/**
 * Takes the file from the path indicated at the start of server, 
 * and run through the same commands stored in
 * the log file from a PREVIOUS runthrough.
 */
public class LogReplayTester implements Tester{
	
	SelendroidServerDriver serverDriver;
	javafx.scene.control.TextArea leftConsole;
	BufferedReader BufferIn = null;

	
	/**
	 * Constructor for LogReplayTester, sets up connection with driver
	 * @param serverDriverIn
	 * @param directoryPath
	 * @param leftConsole
	 */
	public LogReplayTester(SelendroidServerDriver serverDriver, String directoryPath, javafx.scene.control.TextArea leftConsole){
		this.serverDriver = serverDriver;
		if(directoryPath.equals("")){
			directoryPath = "Hybrid_Testing_Log.txt";
		}
		else{
			directoryPath = directoryPath + File.separator + "Hybrid_Testing_Log.txt";
		}
		this.leftConsole = leftConsole;
		
		//Read log file from user directed path
		try {
			//Copy old log file into a file
			PrintWriter tempfile = new PrintWriter("temp.txt", "UTF-8");

			BufferedReader tempIn = new BufferedReader(new FileReader(directoryPath));
			
			//Copy old log file into temp.txt
	        String line = tempIn.readLine();
	        while (line != null) {
	        	tempfile.println(line);
	            line = tempIn.readLine();
	        }
	        tempIn.close();
	        tempfile.close();
	        BufferIn = new BufferedReader(new FileReader("temp.txt"));
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main test method that runs off of AutomatedTester.
	 */
	public void test(ArrayList<WebElement> possibleTargets){
		int elementNum = 0;
		String line = "";

		try {
			//End thread if EOF
			if (line == null){
				AutomatedTester.stopTesting();
			}
			else{
				while(!line.contains("Element index:") && !(line == null)){
					line = BufferIn.readLine(); //read lines from bufferedreader
					//End thread if EOF
					if (line == null){
						AutomatedTester.stopTesting();
						//BufferIn.close();
						break;
					}
					else{
						leftConsole.appendText(line + "\n");
					}
				}
				
				if (line.contains("Element index:")){
					//Parses the number after Element index: into elementNum
					elementNum = Integer.parseInt(line.substring(line.indexOf(':') + 2));

					//Skip through lines not usable by this tester to reach the next useful info
					while(!line.contains("-->")){
						line = BufferIn.readLine();
						leftConsole.appendText(line + "\n");
					}

					//If inserted text to text box
					if (line.contains("Inserting input to textbox:")){
						insertText(elementNum, line, possibleTargets);
					}
					//If clicked a button
					else if (line.contains("Clicked button")){
						clickButton(elementNum, possibleTargets);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

			
	
	
	/**
	 * Correctly identify which element to insert text into, and do that interaction
	 * through a connection to Selendroid Server
	 * @param elementNum
	 * @param line
	 */
	private void insertText(int elementNum, String line, ArrayList<WebElement> possibleTargets){
		//Parse input stored in log file
		String textInput = line.substring(line.indexOf(':') + 2); 
		
		//Execute input
		WebElement target = possibleTargets.get(elementNum);
		target.sendKeys(textInput);
	}
	
	
	/**
	 * Correctly identify which element to click, and do that
	 * through a connection to Selendroid Server
	 * @param elementNum
	 */
	private void clickButton(int elementNum, ArrayList<WebElement> possibleTargets){
		
		//Execute input
		WebElement target = possibleTargets.get(elementNum);
		target.click();
	}
	
}

