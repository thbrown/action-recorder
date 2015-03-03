package com.github.thbrown.actionrecorder;

import java.util.HashSet;
import java.util.Set;

/**
 * It monitors keypresses passed to it to see if the presses have an associated hotkey. If so, the class calls the appropriate methods to handle the hotkey press.
 * 
 * @author thbrown
 */
public class Hotkey {
	
	private Set<Integer> pressedKeys =  new HashSet<Integer>();
	private Main window;
	
	public Hotkey(Main window) {
		this.window = window;
	}
	
	public void remove(Integer toRemove) {
		pressedKeys.remove(toRemove);
	}
	
	public void add(Integer toAdd) {
		pressedKeys.add(toAdd);
	}
	
	public void checkForAndExecuteHotkeys() {
		System.out.println("Keys pressed: " + pressedKeys);
		
		// Escape key was pressed
		if(pressedKeys.contains(27)) {
			window.requestPlaybackThreadStop();	
		}
	}

}
