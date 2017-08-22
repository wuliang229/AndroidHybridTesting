package testers;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import logger.LogFileGenerator;
import serverDriver.SelendroidServerDriver;

public class DepthFirstTester implements Tester{
	
	LogFileGenerator logFile;
	SelendroidServerDriver driver;
	Stack<String> iterator;
	Stack<String> futureTargets;
	Deque<String> visited;
	boolean started = false;
	String refIndex = "";
	ArrayList<String> testStrings;
	String lasthref = "";
	
	
	public DepthFirstTester(LogFileGenerator logFile, ArrayList<String> testStrings, SelendroidServerDriver driver){
		this.logFile = logFile;
		this.iterator = new Stack<String>();
		this.visited = new LinkedList<String>();
		this.futureTargets = new Stack<String>();
		this.testStrings = testStrings;
		this.driver = driver;
	}
	
	public void test(ArrayList<WebElement> possibleTargets){
		if(!started){
			boolean beenVisited = false;

			for(int i = 0; i<possibleTargets.size(); i++){
				if(possibleTargets.get(i).getTagName().equals("input")){
					int count = 0;
					for(String str : testStrings){
						String indexString = ""+i+"("+str+")";
						beenVisited = false;
						for(String temp : visited){
							if(temp.equals(indexString)){
								beenVisited = true;
								break;
							}
						}
						if(!beenVisited && indexString.split("-").length<=15){
							count++;
							System.out.println("INDEX STRING: "+indexString);
							futureTargets.push(indexString);
							futureTargets.push(""+i+"(Keys.BACK_SPACE)");
							visited.push(indexString);
						}

					}

				}
				else{
					String indexString = ""+i;
					for(String temp : visited){
						if(temp.equals(indexString)){
							beenVisited = true;
							break;
						}
					}
					if(!beenVisited && indexString.split("-").length<=15){
						futureTargets.push(indexString);
						visited.push(indexString);
					}

				}
			}
			if(!futureTargets.isEmpty()){
				iterator.push("back");
				while(!futureTargets.isEmpty()){
					iterator.push(futureTargets.pop());
				}
			}
			String targetString = iterator.pop();
			System.out.println("Target String: "+targetString);
			if(targetString.equals("back")){
					String[] refIndexArr = refIndex.split("-");
					refIndex = refIndexArr[0];

					if(refIndexArr.length>1){
					for(int k = 0; k<refIndexArr.length-1;k++){
						refIndex = refIndex+"-"+refIndexArr[k];
					}
					driver.back();
				}
			}
			else{
				String[] splitTarget = targetString.split("-");
				String targetIndexString = splitTarget[splitTarget.length-1];
				String[] splitString = targetIndexString.split("[(]");
				int targetIndex = Integer.parseInt(splitString[0]);
				String inputString = "";
				if(splitString.length>1){
					for(int j = 1; j<splitString.length-1; j++){
						inputString = inputString+splitString[j]+"(";
					}
					inputString = inputString+splitString[splitString.length-1].substring(0, splitString[splitString.length-1].length()-1);
					
				}
				System.out.println("target index:" + targetIndex + "input String: "+inputString);
				WebElement target = possibleTargets.get(targetIndex);
				
				lasthref = target.getAttribute("href");
				logFile.log("Element index: " + targetIndex + "\n");
				logFile.log("\tText                : " + target.getText() + "\n");
				logFile.log("\tLocation            : " + target.getLocation() + "\n");
				logFile.log("\tHypertext Reference : " + target.getAttribute("href") + "\n");
				
				// Easy way to get an array of all characters

				
				// Actually does the input
				if(target.getTagName().equals("input")){
					if(inputString.equals("Keys.BACK_SPACE")){
						target.sendKeys(Keys.BACK_SPACE);
						String[] refIndexArr = refIndex.split("-");
						refIndex = refIndexArr[0];

						if(refIndexArr.length>1){
						for(int k = 0; k<refIndexArr.length-1;k++){
							refIndex = refIndex+"-"+refIndexArr[k];
						}
						}
					}
					else{
						refIndex = targetString;

					
						target.sendKeys(inputString);
					}
					// Write to log file
					logFile.log("\t--> Inserting input to textbox: " + inputString + "\n");
				}
					
				if(target.getTagName().equals("a")){
					target.click();
					refIndex = targetString;

					// Press log file
					logFile.log("\t--> Clicked button\n");
					
				}
				logFile.log(""); // Bad coding: insert newline
			}
				started = true;
		}
		
		else{
			if(!iterator.isEmpty()){
				boolean beenVisited = false;

				for(int i =0; i<possibleTargets.size(); i++){
					if(possibleTargets.get(i).getTagName().equals("input")){
						int count = 0;
						for(String str : testStrings){
							String indexString = refIndex+"-"+i+"("+str+")";
							beenVisited = false;
							for(String temp : visited){
								if(temp.equals(indexString)){
									beenVisited = true;
									break;
								}
							}
							if(!beenVisited && indexString.split("-").length<=15){
								count++;
								futureTargets.push(indexString);
								futureTargets.push(refIndex+"-"+i+"(Keys.BACK_SPACE)");
								visited.push(indexString);
							}

						}

					}
					else{
						String indexString = refIndex+"-"+i;
						for(String temp : visited){
							if(temp.equals(indexString)){
								beenVisited = true;
								break;
							}
						}
						if(!beenVisited && indexString.split("-").length<=15){
							futureTargets.push(indexString);
							visited.push(indexString);
						}

					}
				
				}
				if(!futureTargets.isEmpty()){
					iterator.push("back");
					while(!futureTargets.isEmpty()){
						iterator.push(futureTargets.pop());
					}
				}
				String targetString = iterator.pop();
				if(targetString.equals("back")){
						String[] refIndexArr = refIndex.split("-");
						refIndex = refIndexArr[0];

						if(refIndexArr.length>1){
						for(int k = 0; k<refIndexArr.length-1;k++){
							refIndex = refIndex+"-"+refIndexArr[k];
						}
						driver.back();
					}
				}
				else{
					System.out.println("\n TARGET STRING: "+targetString+"\n");
					String[] splitTarget = targetString.split("-");
					String targetIndexString = splitTarget[splitTarget.length-1];
					String[] splitString = targetIndexString.split("[(]");
					int targetIndex = Integer.parseInt(splitString[0]);
					String inputString = "";
					if(splitString.length>1){
						for(int j = 1; j<splitString.length-1; j++){
							inputString = inputString+splitString[j]+"(";
						}
						inputString = inputString+splitString[splitString.length-1].substring(0, splitString[splitString.length-1].length()-1);
						
					}
					WebElement target = possibleTargets.get(targetIndex);
					
					lasthref = target.getAttribute("href");
					logFile.log("Element index: " + targetIndex + "\n");
					logFile.log("\tText                : " + target.getText() + "\n");
					logFile.log("\tLocation            : " + target.getLocation() + "\n");
					logFile.log("\tDimension           : " + target.getSize() + "\n");
					logFile.log("\tHypertext Reference : " + target.getAttribute("href") + "\n");
					
					// Easy way to get an array of all characters

					
					// Actually does the input
					if(target.getTagName().equals("input")){
						if(inputString.equals("Keys.BACK_SPACE")){
							target.sendKeys(Keys.BACK_SPACE);
							String[] refIndexArr = refIndex.split("-");
							refIndex = refIndexArr[0];
							if(refIndexArr.length>1){
							//refIndex = "";
								for(int k = 1; k<refIndexArr.length-1;k++){
									refIndex = refIndex+"-"+refIndexArr[k];
								}
							}
						}
						else{
							refIndex = targetString;

							target.sendKeys(inputString);
						}
						// Write to log file
						logFile.log("\t--> Inserting input to textbox: " + inputString + "\n");
					}
						
					if(target.getTagName().equals("a")){
						refIndex = targetString;

						target.click();
						// Press log file
						logFile.log("\t--> Clicked button\n");
						
					}
					logFile.log(""); // Bad coding: insert newline
					
					
					
					
					
				}
			}
		}
	}
	
}
