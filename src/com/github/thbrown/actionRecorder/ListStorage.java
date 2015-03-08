package com.github.thbrown.actionrecorder;
import java.util.LinkedList;
import java.util.List;

/**
 * This class accepts commands and stores them in a list. The commands in the list can then be accessed and modified by the provided methods.
 *  
 * @author thbrown
 */
public class ListStorage implements Storage {

	private List<Command> data;
	private long timeLastCommandWasAdded;
	private StatusArea statusConsole;
	
	public ListStorage(StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		data = new LinkedList<Command>();
		timeLastCommandWasAdded = System.currentTimeMillis();
	}

	/**
	 * Adds a sleep command the the data list based on how much time elapsed since the previous command was added to the data list.
	 * This method then adds the provided command to the data list.
	 * 
	 * @param toAdd	the type of command to add to the command list
	 */
	public void addCommand(Command toAdd) {
		
		// Determine how much time has elapsed between the last added command. Add a sleep command to the list for that amount of time.
		// TODO: consider conversion to nano time for guaranteed positive elapsed times
		long timeBetweenCommands = System.currentTimeMillis() - timeLastCommandWasAdded;
		if(timeBetweenCommands > 0) {
			Command sleepCommand = new Command(statusConsole, CommandType.SLEEP, (int) timeBetweenCommands);
			data.add(sleepCommand);
		}
		
		// Add the supplied command to the data list
		data.add(toAdd);
		timeLastCommandWasAdded = System.currentTimeMillis();
	}
	
	public List<Command> getCommandList() {
		return data;
	}

	public void compress() {
		// TODO: Method for compressing record for storage
	}

	@Override
	public void reset() {
		data.clear();
		timeLastCommandWasAdded = System.currentTimeMillis();
	}
	
}
