package com.github.thbrown.actionrecorder;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class GlobalMouseListener implements NativeMouseInputListener {

	RecordData record;
	private StatusArea statusConsole;
	
	public GlobalMouseListener(RecordData newRecord, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = newRecord;
	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		// Not Used
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		System.out.println("Mouse Pressed: " + e.getButton());
		record.addCommand(new Command(CommandType.MOUSE_PRESS, Integer.toString(e.getButton()), statusConsole));
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		System.out.println("Mouse Released: " + e.getButton());
		record.addCommand(new Command(CommandType.MOUSE_RELEASE, Integer.toString(e.getButton()), statusConsole));
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
		record.addCommand(new Command(CommandType.MOUSE_MOVE, Integer.toString(e.getX()), Integer.toString(e.getY())));
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		// Not Used
	}

}