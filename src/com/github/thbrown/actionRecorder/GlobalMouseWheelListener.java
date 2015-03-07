package com.github.thbrown.actionrecorder;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class GlobalMouseWheelListener implements NativeMouseWheelListener {

	private Storage record; 
	private StatusArea statusConsole;
	
	public GlobalMouseWheelListener(Storage data, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = data;
	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) { 
		record.addCommand(new Command(statusConsole, CommandType.MOUSE_WHEEL_MOVE, e.getWheelRotation()));
	}
	
	public void setStorageObject(Storage s) {
		this.record = s;
	}

}