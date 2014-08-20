package com.github.thbrown.actionrecorder;
import java.util.LinkedList;
import java.util.List;

/**
 * This class stores a list of recorded actions.
 *  
 * @author thbrown
 */
public class RecordData {

	List<Command> data;
	long timeBetweenCommands;
	
	private StatusArea statusConsole;
	
	public RecordData(StatusArea statusConsole) {
		this.statusConsole = statusConsole;
		data = new LinkedList<Command>();
		timeBetweenCommands = 0;
	}

	/**
	 * Adds a sleep command the the data list based on how much time elapsed since the previous command was added to the data list.
	 * This method then adds the provided command to the data list.
	 * 
	 * @param toAdd	the type of command to add to the command list
	 */
	public synchronized void addCommand(Command toAdd) {
		
		// Determine how much time has elapsed between the last added command. Add a sleep command to the list for that amount of time.
		// TODO: convert to nano time for guaranteed positive elapsed times
		if(timeBetweenCommands != 0) {
			timeBetweenCommands = System.currentTimeMillis() - timeBetweenCommands;
			data.add(new Command(CommandType.SLEEP,Long.toString(timeBetweenCommands), statusConsole));
			System.out.println("SLEEP " + timeBetweenCommands);
		}
		
		// Add the supplied command to the data list
		data.add(toAdd);
		timeBetweenCommands = System.currentTimeMillis();
	}
	
	public List<Command> getCommandList() {
		return data;
	}

	public void compress() {
		// TODO: Method for compressing record for storage
	}

}
