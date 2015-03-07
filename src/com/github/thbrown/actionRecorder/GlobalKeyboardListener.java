package com.github.thbrown.actionrecorder;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyboardListener implements NativeKeyListener {

	Storage record;
	private StatusArea statusConsole;
	private Hotkey hotkey;

	public GlobalKeyboardListener(Storage data, StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		this.record = data;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		hotkey.add(e.getKeyCode());
		hotkey.checkForAndExecuteHotkeys();
		record.addCommand(new Command(statusConsole, CommandType.KEY_PRESS, e.getKeyCode()));
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		record.addCommand(new Command(statusConsole, CommandType.KEY_RELEASE, e.getKeyCode()));
		hotkey.remove(e.getKeyCode());
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// Not Used
	}
	
	public void setStorageObject(Storage s) {
		this.record = s;
	}
	
	public void setHotkeyObject(Hotkey h) {
		this.hotkey = h;
	}

}
