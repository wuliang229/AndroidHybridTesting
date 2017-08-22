package testers;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import logger.LogFileGenerator;

public class RandomTester implements Tester{
	LogFileGenerator logFile;
	ArrayList<String> testStrings;
	
	/**
	 * Default constructor that links the log file to the input generator.
	 * Inputs generated as well as interactive elements chosen are written into this log file
	 * @param logFile
	 */
	public RandomTester(LogFileGenerator logFile, ArrayList<String> testStrings){
		this.logFile = logFile;
		this.testStrings = testStrings;
	}
	
	/**
	 * This method sends a random input to the application undergoing testing
	 * (a click if it detects a button, or a single character if it detects an input box)
	 * 
	 * @param possibleTargets - arraylist of web elements that can be interacted with.
	 */
	public void test(ArrayList<WebElement> possibleTargets){
		// Grab a random WebElement
		int randomIndex = (int)(possibleTargets.size()*Math.random());
		WebElement target = possibleTargets.get(randomIndex);
		
		// Write details of target to log file
		logFile.log("Element index: " + randomIndex + "\n");
		logFile.log("\tText                : " + target.getText() + "\n");
		logFile.log("\tLocation            : " + target.getLocation() + "\n");
		logFile.log("\tDimension           : " + target.getSize() + "\n");
		logFile.log("\tHypertext Reference : " + target.getAttribute("href") + "\n");
		
		// Easy way to get an array of all characters
		//grab a random character
		randomIndex = (int)(testStrings.size()*Math.random());
		
		// Actually does the input
		if(target.getTagName().equals("input")){
			target.sendKeys(testStrings.get(randomIndex));
			// Write to log file
			logFile.log("\t--> Inserting input to textbox: " + testStrings.get(randomIndex) + "\n");
		}
			
		if(target.getTagName().equals("a")){
			target.click();
			// Press log file
			logFile.log("\t--> Clicked button\n");
			
		}
		logFile.log(""); // Bad coding: insert newline
	}
	

}
