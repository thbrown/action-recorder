package com.github.thbrown.actionRecorder;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Main extends JFrame implements ActionListener, KeyListener {

	// UI objects
	JPanel mainPanel;
	JLabel escapeInstructions;
	JButton startRecording;
	JButton stopRecording;
	JButton replay;
	JTextArea statusConsole;

	// JNativeHook library objects
	GlobalKeyboardListener keyboardListener;
	GlobalMouseListener mouseListener;
	GlobalMouseWheelListener mouseWheelListener;
	
	// Create a new Record object to store recorded data
	Record newRecord = null;
	
	// Keep a handle on the thread used to playback data
	Playback pb;
	
	public static void main(String[] args) {
		Main m = new Main("ActionRecorder");
		m.setVisible(true);
	}

	public Main(String title) {
		setTitle(title);
		setPreferredSize(new Dimension(500,300));
		setMinimumSize(getPreferredSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		this.addContent(mainPanel);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setFocusable(true);
		this.addKeyListener(this);
	}

	// Add UI elements to the JFrame
	public void addContent(JPanel p) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.addKeyListener(this);
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

		JTextArea statusConsole = new JTextArea();
		//statusConsole.setEditable(false);
		this.statusConsole = statusConsole;
		statusConsole.append("Welcome to Action Recorder! \n");
		statusConsole.append("Select 'Record' to begin recording mouse and keyboard commands \n");
		statusConsole.append("------------------------------------------------- \n");	

		JScrollPane scrollPane = new JScrollPane(statusConsole);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		p.add(scrollPane, BorderLayout.SOUTH);
	}


	// UI Button logic
	public void processButtonPress(ButtonAction eventId) {
		
		if(eventId == ButtonAction.START_RECORDING) {

			// Disable the startRecording/replay buttons
			startRecording.setEnabled(false);
			replay.setEnabled(false);

			// Create a new Record object (To store user actions)
			newRecord = new Record();

			// Init jnativehook
			try {
				GlobalScreen.registerNativeHook();
			} catch (NativeHookException ex) {
				String errorMessage = "Error: There was a problem registering the native hook.";
				statusConsole.append(errorMessage + '\n');
				statusConsole.append(ex.getMessage() + '\n');
				System.err.println(errorMessage);
				System.err.println(ex.getMessage());
				startRecording.setEnabled(true);
				return;
			}

			// Init Keyboard Listener
			keyboardListener = new GlobalKeyboardListener(newRecord);
			GlobalScreen.getInstance().addNativeKeyListener(keyboardListener);

			// Init Mouse Click/Movement Listener
			mouseListener = new GlobalMouseListener(newRecord);
			GlobalScreen.getInstance().addNativeMouseListener(mouseListener);
			GlobalScreen.getInstance().addNativeMouseMotionListener(mouseListener);

			// Init Mouse Wheel Listener
			mouseWheelListener = new GlobalMouseWheelListener(newRecord);
			GlobalScreen.getInstance().addNativeMouseWheelListener(mouseWheelListener);

			stopRecording.setEnabled(true);

		} else if(eventId == ButtonAction.STOP_RECORDING) {
			
			// Disable the stopRecording button
			stopRecording.setEnabled(false);
			
			// Disable jnativehook
			GlobalScreen.getInstance().removeNativeKeyListener(keyboardListener);
			GlobalScreen.getInstance().removeNativeMouseListener(mouseListener);
			GlobalScreen.getInstance().removeNativeMouseMotionListener(mouseListener);
			GlobalScreen.getInstance().removeNativeMouseWheelListener(mouseWheelListener);
			GlobalScreen.unregisterNativeHook();
			
			// Enable startRecording/replay buttons
			startRecording.setEnabled(true);
			replay.setEnabled(true);

		} else if(eventId == ButtonAction.REPLAY) {
			
			this.pb = new Playback(newRecord, statusConsole);
			pb.start();

		} else {
			statusConsole.append("Command Not Recognized\n");
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ButtonAction buttonId = (ButtonAction) ((JComponent) event.getSource()).getClientProperty("id");
		processButtonPress(buttonId);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		ButtonAction action = null;
		
		System.out.println("Anything!");
		
		// Logic for hotkeys
		switch( keyCode ) { 
			/*
			case KeyEvent.VK_UP:
				action = ButtonAction.START_RECORDING;
				break;
			case KeyEvent.VK_DOWN:
				action = ButtonAction.STOP_RECORDING;
				break;
			case KeyEvent.VK_R:
				action = ButtonAction.REPLAY;
				break;
			
			case KeyEvent.VK_R:
				System.out.println("hshshs");
			case KeyEvent.VK_ESCAPE:
				if(pb != null) {
					System.out.println("Requesting stop");
					pb.requestThreadStop();
				}
				break;
			*/
		}
		
		processButtonPress(action);
	}

	// Unused
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent event) {}

	// Shortcut debug function (not used)
	static void p(Object s) {
		System.out.println(s.toString());
	}

}

