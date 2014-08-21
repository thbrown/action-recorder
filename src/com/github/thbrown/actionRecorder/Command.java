package com.github.thbrown.actionrecorder;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Command {

	CommandType type;
	String args1;
	String args2;
	
	StatusArea status;

	Command(CommandType type, String args1, StatusArea status) {
		this.status = status;
		this.type = type;
		this.args1 = args1;
	}

	public Command(CommandType type, String args1, String args2) {
		this.type = type;
		this.args1 = args1;
		this.args2 = args2;
	}

	void execute(Robot r) {
		System.out.println("Executing: " + this.toString());
		switch(this.type){
		case MOUSE_MOVE:
			r.mouseMove(Integer.parseInt(this.args1),Integer.parseInt(this.args2));
			break;
		case MOUSE_PRESS:
			switch(Integer.parseInt(this.args1)) {
			case 1: r.mousePress(InputEvent.BUTTON1_MASK); break;
			case 2: r.mousePress(InputEvent.BUTTON2_MASK); break;
			case 3: r.mousePress(InputEvent.BUTTON3_MASK); break;
			default: status.append("Mouse " + this.args1 + " Button Not Recognized A");
			}
			break;
		case MOUSE_RELEASE:
			switch(Integer.parseInt(this.args1)) {
			case 1: r.mouseRelease(InputEvent.BUTTON1_MASK); break;
			case 2: r.mouseRelease(InputEvent.BUTTON2_MASK); break;
			case 3: r.mouseRelease(InputEvent.BUTTON3_MASK); break;
			default: status.append("Mouse " + this.args1 + " Button Not Recognized B");
			}
			break;
		case KEY_PRESS:
			r.keyPress(Integer.parseInt(args1));
			break;
		case KEY_RELEASE:
			r.keyRelease(Integer.parseInt(args1));
			break;
		case MOUSE_WHEEL_MOVE:
			r.mouseWheel(Integer.parseInt(args1));
			break;
		case SLEEP:
			try {
				Thread.sleep(Integer.parseInt(this.args1));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		default:
			status.append("Error! Command Not Recognized!");
		}
	}

	public String toString() {
		return type.toString() + " " + args1 + " " + args2;
	}
}
