package testers;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import logger.LogFileGenerator;

public class BreadthFirstTester implements Tester{
	
	LogFileGenerator logFile;

	public BreadthFirstTester(LogFileGenerator logFile){
		this.logFile = logFile;
	}
	
	public void test(ArrayList<WebElement> possibleTargets){
		
	}
	
}