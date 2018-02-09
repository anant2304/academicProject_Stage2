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
	
	public Controler() {
		//set all the commands here
		
		//pleb commands
		coms.put("u", () -> Move(moveDirection.up));
		coms.put("d", () -> Move(moveDirection.down));
		coms.put("l", () -> Move(moveDirection.left));
		coms.put("r", () -> Move(moveDirection.right));
		coms.put("exit", () -> System.exit(0));

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
				
		if(splitCom.length > 1 )
			argument = splitCom[1];
		
		if(coms.containsKey(splitCom[0])) {
			coms.get(splitCom[0]).run();
		}
		else{
			System.out.println("Command dose not exist");
		}
		
		argument = "";
	}
		
	//set of command actions a user can call
	
	/**
	 * @Summary moves the current player in the given direction
	 * @param direction
	 */
	void Move(moveDirection d){
		if(Game.currObject != null) {
			Game.currObject.Move(d);
		}
		else System.out.println("Current object is null");
	}

	/*
	 * sets the current controlled object based on the name passed to it
	 */
	void Control(String Object) {
		switch(Object){
		case  "white" : Game.SetCurrentObject(0, true); ; break;
		case  "green" : Game.SetCurrentObject(1, true); ; break;
		case  "peacock" : Game.SetCurrentObject(2, true); ; break;
		case  "plum" : Game.SetCurrentObject(3, true); ; break;
		case  "scarlet" : Game.SetCurrentObject(4, true); ; break;
		case  "mustard" : Game.SetCurrentObject(5, true); ; break;
		
		case "rope" : Game.SetCurrentObject(0, false);break;
		case "dagger" : Game.SetCurrentObject(1, false);break;
		case "wrench" : Game.SetCurrentObject(2, false);break;
		case "pistol" : Game.SetCurrentObject(3, false);break;
		case "candlestick" : Game.SetCurrentObject(4, false);break;
		case "leadpipe" : Game.SetCurrentObject(5, false);break;
		default :
			System.out.println("Incorect argument, must be a player or weapon name");return;
		}
		System.out.println("controling " + Object);
	}
	
}
