package com.github.thbrown.actionRecorder;
import java.util.ArrayList;
import java.util.List;


public class Player {

	public Player(Record toPlay) {

	}

	public List<MoveCommand> parse(String inputString) {
		System.out.println("InputString: " + inputString);
		List<MoveCommand> holder = new ArrayList<MoveCommand>();
		String[] line = inputString.split("\n");
		System.out.println("Split String " + line);
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
