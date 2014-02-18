package com.github.thbrown.actionRecorder;
import java.util.LinkedList;
import java.util.List;

public class Record {

	List<Command> data;
	long timeBetweenCommands;

	public Record() {
		data = new LinkedList<Command>();
		timeBetweenCommands = 0;
	}

	public synchronized void addCommand(Command toAdd) {
		// Determine how much time has elapsed between the last added command. Add a sleep command for that amount of time.
		if(timeBetweenCommands != 0) {
			timeBetweenCommands = System.currentTimeMillis() - timeBetweenCommands;
			data.add(new Command(CommandType.SLEEP,Long.toString(timeBetweenCommands)));
			System.out.println("SLEEP " + timeBetweenCommands);
		}
		
		// Add the requested command
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
