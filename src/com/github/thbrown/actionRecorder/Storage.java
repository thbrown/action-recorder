package com.github.thbrown.actionrecorder;

import java.util.List;

public interface Storage {

	public abstract void addCommand(Command toAdd);
	public abstract List<Command> getCommandList();
	public abstract void compress();
	public abstract void reset();
	
}
