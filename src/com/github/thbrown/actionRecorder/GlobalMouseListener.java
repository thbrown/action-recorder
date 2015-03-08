package com.github.thbrown.actionrecorder;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class GlobalMouseListener implements NativeMouseInputListener {

	Storage record;
	private StatusArea statusConsole;
	private Main window;
	private MouseInfoColorUpdater r;
	
	public GlobalMouseListener(Storage data, StatusArea statusConsole, Main main) {
		this.statusConsole = statusConsole;
		this.record = data;
		this.window = main;
		try {
			r = new MouseInfoColorUpdater(window);
			Thread t = new Thread(r);
			t.start();
		} catch (AWTException e) {
			statusConsole.append("Could not start Robot, check your computer's accessibility settings");
		}
	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		// Not Used
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		record.addCommand(new Command(statusConsole, CommandType.MOUSE_PRESS, e.getButton()));
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		record.addCommand(new Command(statusConsole,CommandType.MOUSE_RELEASE, e.getButton()));
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		record.addCommand(new Command(statusConsole, CommandType.MOUSE_MOVE, e.getX(),e.getY()));
		window.updateMousePositionLabel(e.getX(), e.getY());
		r.requestColorUpdate(e.getX(), e.getY());
	}

	// Dragging the mouse is the same thing as moving it (as mouse presses and releases are recorded)
	public void nativeMouseDragged(NativeMouseEvent e) {
		record.addCommand(new Command(statusConsole, CommandType.MOUSE_MOVE, e.getX(), e.getY()));
	}
	
	public void setStorageObject(Storage s) {
		this.record = s;
	}

}