package Sigurd;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author PM
 * 
 * @Summary The set of commands a player
 *
 */
public class Controler {
	public enum moveDirection {up, down, left, right};
	
	Map<String,Runnable> coms = new HashMap<String,Runnable>();//Runnable being java's representation for a method with no return or arguments
	
	String argument = "";
	
	public Controler() {//define all commands here		
		//pleb commands
		coms.put( "u", 		() -> Move(moveDirection.up));
		coms.put( "d", 		() -> Move(moveDirection.down));
		coms.put( "l", 		() -> Move(moveDirection.left));
		coms.put( "r", 		() -> Move(moveDirection.right));
		coms.put("exit",	() -> System.exit(0));

		//dev commands
		coms.put("control", () -> Control(argument));
	}
	
	/**
	 * @Summary Executes the given command
	 * 
	 * @param command
	 */
	public  void ExicuteCommand(String command) {
		command = command.toLowerCase();
		
		String[] splitCom = command.split(" ");
				
		if(splitCom.length > 1 )//if has second word
			argument = splitCom[1];//put second word in argument
		
		if(coms.containsKey(splitCom[0])) {//if first word is a command
			coms.get(splitCom[0]).run();
		}
		else{
			Game.display.sendMessage("Command dose not exist");
		}
		
		argument = "";
	}
		
	//set of command actions a user can call
	
	/**
	 * @Summary moves the current player in the given direction
	 * @param direction
	 */
	void Move(moveDirection d){
		if(Game.currObject != null) 
			Game.currObject.Move(d);
		
		else 
			Game.display.sendMessage("Current object is null");
	}

	/*
	 * @Summary sets the current controlled object based on the name passed to it
	 * @param the name of the object you want to control
	 */
	void Control(String Object) {
		if(Game.ObjectExistes(Object)) {
			Game.SetCurrentObject(Object);
			Game.display.sendMessage("controling " + Object);
		}
		
		else
			Game.display.sendMessage("Object not found");
	}
}
