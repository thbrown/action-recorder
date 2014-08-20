package com.github.thbrown.actionrecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Seems to be unused... for now. Should be implemented to parse String data from a saved record (not yet implemented)
 * 
 * @author thbrown
 */
public class Parser {
	
	private StatusArea statusConsole;

	public List<MoveCommand> parse(String inputString) {
		
		statusConsole.append("InputString: " + inputString);
		
		String[] line = inputString.split("\n");
		statusConsole.append("Split String " + line);
		
		List<MoveCommand> holder = new ArrayList<MoveCommand>();
		
		for(int i=0; i<line.length; i++) {
			String[] words = line[i].split(" ");
			int x = Integer.parseInt(words[0]);
			int y = Integer.parseInt(words[1]);
			MoveCommand newCommand = new MoveCommand(x,y);
			holder.add(newCommand);
		}
		
		return holder;
	}

}
