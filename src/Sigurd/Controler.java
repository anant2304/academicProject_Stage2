package Sigurd;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Peter Major
 * @Summary Takes in commands from the Command Panel and does actions based on them.
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class Controler {
	public enum moveDirection {up, down, left, right};
	
	//Runnable being java's representation for a method with no return or arguments
	Map<String,Runnable> coms = new HashMap<String,Runnable>();
	
	String argument = "";
	
	public Controler() {//define all commands here	
		coms.put( "u", 		() -> Move(moveDirection.up));
		coms.put( "d", 		() -> Move(moveDirection.down));
		coms.put( "l", 		() -> Move(moveDirection.left));
		coms.put( "r", 		() -> Move(moveDirection.right));
		coms.put("exit",	() -> System.exit(0));
		coms.put("control", () -> Control(argument));
	}
	
	/**
	 * @Summary Executes the given command
	 * 
	 * @param command
	 */
	public void ExicuteCommand(String command) {

        Game.display.sendMessage(command);
		command = command.toLowerCase();
		command = command.trim();
		
		String[] splitCom = command.split("\\s+");
				
		if(splitCom.length > 1 )//if has second word
			argument = splitCom[1];//put second word in argument
		
		if(coms.containsKey(splitCom[0])) {//if first word is a command
			coms.get(splitCom[0]).run();
		}
		else{
			Game.display.sendMessage("    Command does not exist\n");
		}
		
		argument = "";
	}
		
	//set of command actions a user can call
	
	/**
	 * @Summary moves the current player in the given direction
	 * @param direction
	 */
	void Move(moveDirection d){
		if(Game.currentObject != null) {
	        Board.GetBoard().GetBoardPanel().repaint();
		}
		else 
			Game.display.sendMessage("    Current object is null\n");
	}

	/**
	 * @Summary sets the current controlled object based on the name passed to it
	 * @param the name of the object you want to control
	 */
	void Control(String Object) {
		if(Game.ObjectExistes(Object)) {
			Game.SetCurrentObject(Object);
			Game.display.sendMessage("    Controling " + Object + "\n");
		}
		
		else
			Game.display.sendMessage("    Object not found\n");
	}
}
