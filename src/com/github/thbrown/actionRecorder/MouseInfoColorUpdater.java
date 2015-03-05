package com.github.thbrown.actionrecorder;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class contains a robot class that is used to asynchronously update the mouseInfoPanel color.
 * 
 * This buffer allows robot getPixelColor(...) calls to be canceled before they are executed.
 *  
 * This class in necessary because getPixelColor(...) can take several hundred ms to complete.
 * 
 * @author thbrown
 */
public class MouseInfoColorUpdater implements Runnable {

	private final Object lock = new Object();
	
	GetPixelColorEvent nextEventToExecute;
	Main window;
	Color c;
	Robot r;

	MouseInfoColorUpdater(Main window) throws AWTException {
		r = new Robot();
		this.window = window;
	}
	
	public void requestColorUpdate(int x, int y) {
		synchronized (lock) {
			nextEventToExecute = new GetPixelColorEvent(x,y);
		}
	}
	
	class GetPixelColorEvent {
		int x;
		int y;
		
		GetPixelColorEvent(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	@Override
	public void run() {
		while(true) {
			r.waitForIdle();		
			synchronized (lock) {
				if(nextEventToExecute != null) {
					c = r.getPixelColor(nextEventToExecute.x, nextEventToExecute.y);
					nextEventToExecute = null;
				}
			}
			
			// Update the mouseInforBar in main, there may be some threading issues here
			if(c != null) {
				window.updateMouseColorLabel(c.getRed(), c.getGreen(), c.getBlue());
			}
			
			// We'll check every 10ms at a minimum
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
