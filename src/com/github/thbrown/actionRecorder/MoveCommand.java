package com.github.thbrown.actionrecorder;
import java.awt.Robot;

public class MoveCommand {

	int x;
	int y;

	public MoveCommand(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void executeCommand(Robot r) {
		r.mouseMove(x, y);
	}

}
