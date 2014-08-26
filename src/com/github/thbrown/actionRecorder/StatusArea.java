package com.github.thbrown.actionrecorder;

import javax.swing.JTextArea;

/**
 * This class is an extended JTextArea used to show program updates and errors on the UI
 * 
 * @author thbrown
 */
public class StatusArea extends JTextArea {
	
	StatusArea(int i, int j) {
		super(i,j);
		initialize();
	}
	
	StatusArea() {
		initialize();
	}
	
	public void initialize() {
		this.setEditable(false);
		this.append("Welcome to Action Recorder!");
		this.append("Press the 'Record' button to begin recording mouse and keyboard commands");
		this.append("-------------------------------------------------");
	}

	/**
	 * Prints the string provided to System.out and appends the string to the JTextArea on the UI
	 * 
	 * @param toAppend	the object to print
	 */
	@Override
	public void append(String toAppend) {
		
		synchronized(this) {
			super.append(toAppend + '\n');
			super.setCaretPosition(super.getDocument().getLength());
		}
		
		System.out.println(toAppend);
	}
	
	/**
	 * Prints the string representation of the provided object to System.out and appends it to the JTextArea on the UI
	 * 
	 * @param toAppend	the object to print
	 */
	public void append(Object toAppend) {
		append(toAppend.toString());
	}
	
}
