package com.github.thbrown.actionRecorder;

import java.awt.AWTException;
import java.awt.Robot;

import javax.swing.JTextArea;

public class Playback extends Thread {
	
	private volatile boolean run;
	private JTextArea statusConsole;
	private Record newRecord;
	
	Playback(Record newRecord, JTextArea statusConsole) {
		run = true;
		this.statusConsole = statusConsole;
		this.newRecord = newRecord;
	}
	
	public void requestThreadStop() {
		run = false;
	}
	
	public void run() {
		// Launch the robot in a new thread so we an 
		Robot executingRobot;
		
		// Execute each command using java robot
		try {
			executingRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			statusConsole.append("Unable to start java robot\n");
			return;
		}
		
		// Call execute on each command in the list
		for(Command c : newRecord.getCommandList()) {
			if(!run) {
				break;
			}
			c.execute(executingRobot);
			statusConsole.append(c.toString() + "\n");
		}
	}

}
