package testers;

import serverDriver.SelendroidServerDriver;

/** 
 * This class implements the automated testing functionality. 
 * It runs a thread which interacts with the app / emulator itself. 
 * 
 * To Do: add "return to app" functionality
 * 
 * @author thomas and will
 */
public class AutomatedTester {
	private static SelendroidServerDriver serverDriver;
	private static TestingThread testingThread;
	private static Tester tester;
	//private static InputGenerator inputGen;
	private static int waitTime = 3000;
	
	/**
	 * Constructs an AutomatedTester object.
	 * 
	 * @param serverDriverIn Passes the selendroid driver to be used with the program
	 * @param inputGen Gives access to the proper inputGenerator class. 
	 */
	public AutomatedTester(SelendroidServerDriver serverDriverIn,  Tester tester){
		this.serverDriver = serverDriverIn;
		//this.inputGen = inputGen;
		this.tester = tester;
	}

	/**
	 * Starts the testing thread. 
	 */
	public static void startTesting() {
		testingThread = new TestingThread(serverDriver, waitTime, tester);
	}
	
	/**
	 * Ends the testing thread. 
	 */
	public static void stopTesting(){
		testingThread.stopThread();
	}
	
	/**
	 * Pauses the testing thread.
	 */
	public void pauseInput(){
		testingThread.pauseThread();
	}

	/**
	 * Resumes the testing thread. 
	 */
	public void resumeInput() {
		testingThread.resumeThread();
	}

	public void setWaitTime(int interval) {
		this.waitTime = interval;
	}
	
	public void setTester(Tester newTester){
		this.tester = newTester;
	}
}

class TestingThread implements Runnable{
	SelendroidServerDriver serverDriver;
	Tester tester;
	
	Thread thrd;
	boolean suspended;
	boolean stopped;
	int waitTime;
	
	TestingThread(SelendroidServerDriver serverDriver, int waitTime, Tester tester){
		thrd = new Thread(this);
		this.tester = tester;
		this.serverDriver = serverDriver;
		this.waitTime = waitTime;
		suspended = false;
		stopped = false;
		System.out.println("Wait time of : " + Integer.toString(waitTime));
		thrd.start();
	};
	
	@Override
	public synchronized void run() {
		try{
			System.out.println("Started generating inputs");
				while(!stopped){
					while(!suspended && !stopped){
						try{
							//If Selendroid is unable to extract the hierarchy, we are no longer
							//in the application. Catch will return us to the application
							serverDriver.extractHierarchy();
							
							// Extract and execute inputs
							serverDriver.extractElements();
							tester.test(serverDriver.possibleTargets);
							//generator.generateRandomInput(serverDriver.possibleTargets);
							
						} catch (Exception e) {
							//Returns us to the application
							System.out.println(e.getMessage());
							serverDriver.resumeApp();
						}
						
						// Wait to allow input to be carried out. 
						Thread.sleep(waitTime);
					}
					while(suspended){ // Sleep while we're pause
						Thread.sleep(1000);
					}
					if(stopped){ // Interrupt the thread
						thrd.interrupt();
						System.out.println("Testing thread stopped.");
					}
				}
		} catch(InterruptedException exc){
			System.out.println("Testing thread interrupted");
		}
	}
	
	/**
	 * Sets the flag to stop the thread. 
	 */
	public void stopThread(){
		stopped = true;
    }
	
	/**
	 * Sets the flag to suspend the thread, writes message to console. 
	 */
	public void pauseThread(){
		suspended = true;
		System.out.println("Testing thread paused...");
	}
	
	/**
	 * Resumes the thread, writes message to console. 
	 */
	public void resumeThread(){
		suspended = false;
		System.out.println("Resuming thread...");
	}
}