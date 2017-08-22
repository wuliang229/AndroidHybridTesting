package logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import gui.MainGUI;


/**
 * Creates a .txt file that will log all the inputs and interactive elements selected by 
 * this application on webview. 
 * 
 */
public class LogFileGenerator {
	PrintWriter writer;
	MainGUI mainGui;
	
	/**
	 * Default constructor that will create the .txt file at where the code of the application is
	 * @throws IOException if unable to create and write file
	 */
	public LogFileGenerator(){	
		try{
		    writer = new PrintWriter("Hybrid_Testing_Log.txt", "UTF-8");
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
	
	/**
	 * Constructor that will create the .txt file at the path that the user specified
	 * 
	 * @param path - the desired path to store the log file
	 * @throws IOException if unable to create and write file
	 */
	public LogFileGenerator(String path){
		// No path specified
		if (path.equals("")){
			try{
			    writer = new PrintWriter("Hybrid_Testing_Log.txt", "UTF-8");
			} catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage());
			}
		}
		// Path provided
		else{
			try{
			    writer = new PrintWriter(path + File.separator + "Hybrid_Testing_Log.txt", "UTF-8");
			} catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 */
	public void setGUI(MainGUI gui){
		this.mainGui = gui;
	}
	/**
	 * Writes input into the log file
	 * @param input to store into log file
	 */
	public void log(String input){
		writer.println(input);
		mainGui.displayNewInput(input);
		
	}
	
	/**
	 * Closes file
	 */
	public void close (){
		writer.close();
	}
}
