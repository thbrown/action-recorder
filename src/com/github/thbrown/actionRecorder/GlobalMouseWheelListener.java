package com.github.thbrown.actionRecorder;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class GlobalMouseWheelListener implements NativeMouseWheelListener {

	Record record; 

	public GlobalMouseWheelListener(Record record) {
		this.record = record;
	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		System.out.println("Mosue Wheel Moved: " + e.getWheelRotation());
		record.addCommand(new Command(CommandType.MOUSE_WHEEL_MOVE, Integer.toString(e.getWheelRotation())));
	}

}