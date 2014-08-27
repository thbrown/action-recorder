package com.github.thbrown.actionrecorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * This is the main class for the ActionRecorder application.
 * 
 * This applications records mouse and keyboard presses and mouse movement. The recordings can then be played back.
 * @author thbrown
 */
public class Main extends JFrame implements ActionListener {

	// UI objects
	private JPanel mainPanel;
	private JButton startRecording;
	private JButton stopRecording;
	private JButton replay;
	private StatusArea statusConsole;

	// JNativeHook library objects
	private GlobalKeyboardListener keyboardListener;
	private GlobalMouseListener mouseListener;
	private GlobalMouseWheelListener mouseWheelListener;
	
	// Storages object to store save recorded data
	private Storage dataHolder;
	private Storage dataGarbageCan;
	
	// A handle for the thread used to playback data
	private Playback playbackThread;
	
	public static void main(String[] args) {
		Main m = new Main("ActionRecorder");
		m.setVisible(true);
	}

	public Main(String title) {
		setTitle(title);
		setPreferredSize(new Dimension(500,350));
		setMinimumSize(getPreferredSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		this.addContent(mainPanel);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setFocusable(true);
		
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		//Build the first menu.
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		JMenuItem menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		// Attach the menu to the JFrame
		this.setJMenuBar(menuBar);
		
		// Initialize storage objects, one that saves command data and one that discards it
		dataHolder = new ListStorage(statusConsole);
		dataGarbageCan = new BlackholeStorage();
		
		// Initialize the jnativehook Library
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			String errorMessage = "Error: There was a problem registering the native hook.";
			statusConsole.append(errorMessage);
			statusConsole.append(ex.getMessage());
			startRecording.setEnabled(true);
			return;
		}

		// Initialize the jnativehook Keyboard Listener
		keyboardListener = new GlobalKeyboardListener(dataGarbageCan, statusConsole);
		GlobalScreen.getInstance().addNativeKeyListener(keyboardListener);

		// Initialize the jnativehook Mouse Click/Movement Listener
		mouseListener = new GlobalMouseListener(dataGarbageCan, statusConsole);
		GlobalScreen.getInstance().addNativeMouseListener(mouseListener);
		GlobalScreen.getInstance().addNativeMouseMotionListener(mouseListener);

		// Initialize the jnativehook Mouse Wheel Listener
		mouseWheelListener = new GlobalMouseWheelListener(dataGarbageCan, statusConsole);
		GlobalScreen.getInstance().addNativeMouseWheelListener(mouseWheelListener);
	}

	/**
	 * Adds UI elements (Record, Stop, and Replay buttons plus the status JTextArea) to the specified panel.
	 * 
	 * @param p	the panel to add the UI elements to
	 */
	public void addContent(JPanel p) {
		JPanel buttonPanel = new JPanel();
		startRecording = new JButton("Record");
		startRecording.putClientProperty("id",ButtonAction.START_RECORDING);
		startRecording.addActionListener(this);

		stopRecording = new JButton("Stop Recording");
		stopRecording.putClientProperty("id",ButtonAction.STOP_RECORDING);
		stopRecording.addActionListener(this);
		stopRecording.setEnabled(false);

		replay = new JButton("Replay");
		replay.putClientProperty("id", ButtonAction.REPLAY);
		replay.addActionListener(this);

		buttonPanel.add(startRecording);
		buttonPanel.add(stopRecording);
		buttonPanel.add(replay);
		p.add(buttonPanel, BorderLayout.NORTH);

		statusConsole = new StatusArea(13, 40);
		DefaultCaret caret = (DefaultCaret)statusConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane(statusConsole);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		p.add(scrollPane, BorderLayout.SOUTH);
	}

	/**
	 * Executes code for the specified button press.
	 * 
	 * @param eventId	the type of button that was pressed
	 */
	public void processButtonPress(ButtonAction eventId) {
		
		if(eventId == ButtonAction.START_RECORDING) {

			// Disable the startRecording/replay buttons
			startRecording.setEnabled(false);
			replay.setEnabled(false);

			// Start saving the incoming commands to a fresh list
			dataHolder.reset();
			keyboardListener.setStorageObject(dataHolder);
			mouseListener.setStorageObject(dataHolder);
			mouseWheelListener.setStorageObject(dataHolder);

			// Disable the 'Stop Recording' button
			stopRecording.setEnabled(true);

		} else if(eventId == ButtonAction.STOP_RECORDING) {
			
			// Disable the stopRecording button
			stopRecording.setEnabled(false);
			
			// Start discarding the incoming commands
			keyboardListener.setStorageObject(dataGarbageCan);
			mouseListener.setStorageObject(dataGarbageCan);
			mouseWheelListener.setStorageObject(dataGarbageCan);
			
			// Enable startRecording/replay buttons
			startRecording.setEnabled(true);
			replay.setEnabled(true);

		} else if(eventId == ButtonAction.REPLAY) {
			
			replay.setEnabled(false);
			stopRecording.setEnabled(false);
			replay.setEnabled(true);
			
			// Start a new thread to playback the recorded actions
			this.playbackThread = new Playback(dataHolder, statusConsole);
			playbackThread.start();

		} else {
			statusConsole.append("Command not recognized: " + eventId.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JButton) {
			ButtonAction buttonId = (ButtonAction) ((JComponent) event.getSource()).getClientProperty("id");
			processButtonPress(buttonId);
		} else if(event.getSource() instanceof JMenuItem) {
			System.exit(0);
		} else {
			statusConsole.append("Unrecognized action: " + event.toString());
		}
	}

	// Shortcut debug function (not used)
	static void p(Object s) {
		System.out.println(s);
	}
	
	/* This code may be useful later...
		// Disable jnativehook
		GlobalScreen.getInstance().removeNativeKeyListener(keyboardListener);
		GlobalScreen.getInstance().removeNativeMouseListener(mouseListener);
		GlobalScreen.getInstance().removeNativeMouseMotionListener(mouseListener);
		GlobalScreen.getInstance().removeNativeMouseWheelListener(mouseWheelListener);
		GlobalScreen.unregisterNativeHook();
	 */

}

