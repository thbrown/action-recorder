package com.github.thbrown.actionrecorder;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class GlobalMouseListener implements NativeMouseInputListener {

	Storage record;
	private StatusArea statusConsole;
	
	public GlobalMouseListener(Storage data, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = data;
	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		// Not Used
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		record.addCommand(new Command(CommandType.MOUSE_PRESS, Integer.toString(e.getButton()), statusConsole));
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		record.addCommand(new Command(CommandType.MOUSE_RELEASE, Integer.toString(e.getButton()), statusConsole));
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		record.addCommand(new Command(CommandType.MOUSE_MOVE, Integer.toString(e.getX()), Integer.toString(e.getY())));
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		record.addCommand(new Command(CommandType.MOUSE_MOVE, Integer.toString(e.getX()), Integer.toString(e.getY())));
	}
	
	public void setStorageObject(Storage s) {
		this.record = s;
	}

}