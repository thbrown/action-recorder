package com.github.thbrown.actionrecorder;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class contains the variables and methods necessary to execute the mouse movements and button presses
 * saved during a recording session.
 * 
 * @author thbrown
 */
public class Playback extends Thread {
	
	// Flag variable so this Thread can be halted from the main thread
	private volatile boolean run;
	
	// The text area on the UI, used to show status updates
	private StatusArea statusConsole;
	private Storage newRecord;
	private int numberOfTimesToRepeat;
	
	Playback(Storage dataHolder, StatusArea statusConsole, Integer numberOfTimesToRepeat) {
		run = true;
		this.statusConsole = statusConsole;
		this.newRecord = dataHolder;
		this.numberOfTimesToRepeat = numberOfTimesToRepeat;
	}
	
	public void requestPlaybackThreadStop() {
		run = false;
	}
	
	public void run() {
		// Launch the robot in a new thread so the buttons on the UI are still pressable
		Robot executingRobot;
		
		// Execute each command using a java robot
		try {
			executingRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			statusConsole.append("Error: Unable to start java robot");
			return;
		}
		
		for(int i=0; i < numberOfTimesToRepeat; i++) {
			// Call execute on each command in the list
			for(Command c : newRecord.getCommandList()) {
				if(!run) {
					statusConsole.append("Halting playback");
					break;
				}
				
				// Sometimes the command can be malformed (e.g. bad key code)
				try {
					c.execute(executingRobot);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					statusConsole.append("Error while running command: " + c.toString());
					statusConsole.append("Stack trace: \n" + errors.toString());
					statusConsole.append("Error: " + e.getMessage() + ". Haulting playback execution.");
					run = false;
				}
			}
		}
	}

}
