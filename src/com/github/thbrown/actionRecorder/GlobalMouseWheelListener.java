package com.github.thbrown.actionrecorder;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class GlobalMouseWheelListener implements NativeMouseWheelListener {

	RecordData record; 
	private StatusArea statusConsole;
	
	public GlobalMouseWheelListener(RecordData record, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = record;
	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		System.out.println("Mosue Wheel Moved: " + e.getWheelRotation());
		record.addCommand(new Command(CommandType.MOUSE_WHEEL_MOVE, Integer.toString(e.getWheelRotation()), statusConsole));
	}

}