package com.github.thbrown.actionrecorder;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyboardListener implements NativeKeyListener {

	Storage record;
	private StatusArea statusConsole;

	public GlobalKeyboardListener(Storage data, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = data;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		record.addCommand(new Command(CommandType.KEY_PRESS, Integer.toString(e.getKeyCode()), statusConsole));
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		record.addCommand(new Command(CommandType.KEY_RELEASE, Integer.toString(e.getKeyCode()), statusConsole));
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// Not Used
	}
	
	public void setStorageObject(Storage s) {
		this.record = s;
	}

}
