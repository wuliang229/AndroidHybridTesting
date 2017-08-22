package serverDriver;

import io.selendroid.standalone.SelendroidConfiguration;
import io.selendroid.standalone.SelendroidLauncher;
import javafx.scene.image.Image;
import io.selendroid.common.SelendroidCapabilities;
import io.selendroid.client.SelendroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

//import extraction.ExtractedInfoHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * The purpose of this class is to provide an interface between MainGUI and the Selendroid server itself. 
 * It is responsible for starting the Selendroid Server, and extracting information regarding the GUI.
 */
public class SelendroidServerDriver {
	
	SelendroidLauncher selendroidServer;
	SelendroidDriver driver;
//	ExtractedInfoHandler infoHandler;
	String apkPath;
	String capabilities;
	public ArrayList<WebElement> possibleTargets;
	
	
	/**
	 * Initializes a SelendroidServerDriver class with a specified
	 * application APK path, and capabilities, for use in launching
	 * the Selendroid server
	 * 
	 * @param apkPath - the path of the target APK
	 * @param capabilities - the capabilities of the app for creating the selendroid driver. 
	 */
	public SelendroidServerDriver(String apkPath, String capabilities) {
//		this.infoHandler = new ExtractedInfoHandler(this);
		this.apkPath = apkPath;
		this.capabilities = capabilities;
	}
	
	/**
	 * Launches the Selendroid server with the previously given launch
	 * parameters. 
	 * 
	 * @throws Exception if the Selendroid server is not started properly
	 */
	public void setUp() throws Exception{
		SelendroidConfiguration config = new SelendroidConfiguration();
		config.addSupportedApp(apkPath); // Specifies target APK
		SelendroidLauncher selendroidServer = new SelendroidLauncher(config);
		selendroidServer.launchSelendroid();
		SelendroidCapabilities caps = new SelendroidCapabilities(capabilities); // Passes capabilities to Selendroid server
		// AppId for hybridtestapp.apk: com.example.hybridtestapp:1.0
		driver = new SelendroidDriver(caps); // Makes a new driver
	}
	
	/**
	 * Extracts the current activity's hierarchy view. 
	 * 
	 * @return the overall hierarchy of the application's current activity
	 */
	public String extractHierarchy() {
		driver.switchTo().window("NATIVE_APP");
		return(driver.getPageSource()+'\n');
	}
	
	/**
	 * Makes the driver switch to the WebView context
	 */
	public void switchToWebView(){
		driver.switchTo().window("WEBVIEW");
	}
	
	/**
	 * Returns overall page source. 
	 * 
	 * @return the overall source of the page as a String. Includes both HTML of WebView(s) and hierarchy. 
	 */
	public String extractPageSource() {
		String outputString = "";
		outputString += driver.getPageSource() + "\n";
		driver.switchTo().window("WEBVIEW");
		outputString += driver.getPageSource() + "\n";
		return outputString;
	}
	
	/**
	 * Extracts the HTML source of the WebView.
	 * 
	 * @return the HTML source of the WebView as a string
	 */
	public String extractWebViewHTML(){
		driver.switchTo().window("WEBVIEW"); // Switch to the webview
		String outputString = driver.getPageSource();
		return outputString;	
	}
	
	/**
	 * Extracts ALL (well, hopefully all) interactable elements
	 * from the selected WebView, then returns them as a string. 
	 * These found elements are then added to an array of possible
	 * targets. All elements are found, but only the ones with 
	 * attributes suggesting that they are interactable are kept. 
	 *
	 * @return the list of elements as a nicely formatted string. 
	 */
	public String extractElements(){
		driver.switchTo().window("WEBVIEW"); // Switch to the webview context
		
		// Get a list of all elements
		List<WebElement> elementsByCss = driver.findElements(By.cssSelector("*")); 
		
		possibleTargets = new ArrayList<WebElement>();
		System.out.println(elementsByCss);
		String outputString = "";
		int count = 0;
		
		for(WebElement e : elementsByCss){
			if(e.getTagName().equals("a")||e.getTagName().equals("input")){ // check if they're ones we want
				possibleTargets.add(e);
				count += 1;
//				System.out.print((e.getTagName() + " : " + e.getText() + " : " + e.getAttribute("href") + " : " + e.getAttribute("id")));
				
				// Make the formatting pretty!
				outputString += "[Element " + Integer.toString(count) + "]: " + e.getText() + "\n" + 
				"\t[Tag Name]: " + e.getTagName() + "\n"
				+ "\t[Name]: " + e.getTagName() + "\n" 
				+ "\t[href]: " + e.getAttribute("href") + "\n"
				+ "\t[ID]: " + e.getAttribute("id") + "\n"
				+ "\t[Location]: " + e.getLocation() + "\n"
			    + "\t[Dimension]: " + e.getSize()+ "\n \n";
			}
		}
		outputString += "Total elements found: " + Integer.toString(count); // Print the total amount of elements found
		System.out.println(outputString);
		return(outputString); 
	}
	
	/**
	 * This method allows the hierarchy extraction to provide interactable elements under WebView element.
	 * 
	 * @return found elements in XML format
	 */
	public String extractElementNames(){
		driver.switchTo().window("WEBVIEW"); // Switch to the webview context
		// Get a list of all elements
		List<WebElement> elementsByCss = driver.findElements(By.cssSelector("*")); 
		String outputString = "";
		for(WebElement e : elementsByCss){
			if(e.getTagName().equals("input")) { // Check if they're ones we want
				System.out.println("type=\""+e.getAttribute("type")+"\"");
				System.out.println("name=\""+e.getAttribute("name")+"\"");
				System.out.println("value=\""+e.getAttribute("value")+"\"");
				outputString = outputString + "<" + e.getTagName() + " type=\""+e.getAttribute("type")+"\""+
				" name=\""+e.getAttribute("name")+"\""+" value=\""+e.getAttribute("value")+"\""+"/>";
			}
			if(e.getTagName().equals("a")){
				outputString = outputString + "<" + e.getTagName() + " name=\""+e.getAttribute("name") + "\"" +" href=\""+
			    e.getAttribute("href")+"\""+"/>";
			}
		}
		return(outputString); 
	}
	
	/**
	 * Select all elements by a particular keyword.
	 * In alpha! Does not really work right now. 
	 * 
	 * @param keyword - the keyword we want to target
	 * @return a String of the found elements in a nice format. 
	 */
	public String extractElementsByKeyword(String keyword){
		
		List<WebElement> elements = driver.findElements((By.name(keyword)));
		String outputString = "";
		for(WebElement e: elements){
			String foundElement = e.getTagName();
			System.out.println(foundElement);
			outputString = outputString + " | " + foundElement;
		}
		return outputString;
	}
	
	/**
	 * Tears down the server.
	 */
	public void end(){ 
		if(driver != null){
			driver.quit();
		}
	    if (selendroidServer != null) {
	        selendroidServer.stopSelendroid();
	  }
	}
	
	/**
	 * Resumes the application from the foreground. 
	 * 
	 * You'll notice that this calls driver.backgroundApp() first. 
	 * The reason for this is that in the context of our application, 
	 * when you "leave" the app the app is still technically in the 
	 * foreground in some cases, like when you're on the phone screen
	 * or texting screen. Nothing happens if you call backgroundApp() if its
	 * already in the background, so this calls it just in case. 
	 */
	public void resumeApp(){
		driver.backgroundApp(); 
		driver.resumeApp();
	}
	
	/**
    * Presses the hardware back button
    */
    public void back(){
        //driver.navigate().back();
    	//Tells ADB to call android back
    	try {
			Runtime.getRuntime().exec("adb shell input keyevent 4");
		} catch (Exception e) {
			System.err.println("Caught Exception: " + e.getMessage());
		}
    }
 
	/**
	 * Puts the application to the background.
	 */
	public void hideApp(){
		driver.backgroundApp();
	}
}