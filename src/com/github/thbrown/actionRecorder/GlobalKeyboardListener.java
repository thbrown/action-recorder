package com.github.thbrown.actionrecorder;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyboardListener implements NativeKeyListener {

	RecordData record;
	private StatusArea statusConsole;

	public GlobalKeyboardListener(RecordData record, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = record;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		record.addCommand(new Command(CommandType.KEY_PRESS, Integer.toString(e.getKeyCode()), statusConsole));
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		record.addCommand(new Command(CommandType.KEY_RELEASE, Integer.toString(e.getKeyCode()), statusConsole));
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// Not Used
	}

}
