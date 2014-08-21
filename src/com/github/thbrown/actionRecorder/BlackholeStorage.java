package com.github.thbrown.actionrecorder;

import java.util.List;

/**
 * This class accepts commands and immediately discards them. All data retrieval methods return null. All data manipulation and data storage
 * methods are no-ops. An instantiation of this class is used when data is not being recorded.
 *  
 * @author thbrown
 */
public class BlackholeStorage implements Storage {

	@Override
	public void addCommand(Command toAdd) {
		// Meh...
	}

	@Override
	public List<Command> getCommandList() {
		return null;
	}

	@Override
	public void compress() {
		// Meh...
	}

	@Override
	public void reset() {
		// Meh...
	}

}
