package Sigurd.InputPannel;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author PM
 * 
 * @Summary The set of commands a player
 *
 */
public class Command {
	
	enum moveDirection {up, down, left, right};
	
	Map<String,Runnable> coms = new HashMap<String,Runnable>();//Runnable being java's representation for a method with no return or arguments
	
	public Command() {
		//set all the commands here
		
		//pleb commands
		coms.put("u",() -> Move(moveDirection.up));
		coms.put("d", () -> Move(moveDirection.down));
		coms.put("l", () -> Move(moveDirection.left));
		coms.put("r", () -> Move(moveDirection.right));

		//dev commands
		coms.put("setplayer1", () -> SetPlayer(1));
		coms.put("setplayer2", () -> SetPlayer(2));
		coms.put("setplayer3", () -> SetPlayer(3));
		coms.put("setplayer4", () -> SetPlayer(4));
	}
	
	/**
	 * @Summary Executes the given command
	 * 
	 * @param command
	 */
	public  void Exicute(String command) {
		command = command.toLowerCase();
		
		if(coms.containsKey(command)) {
			coms.get(command).run();
		}
		else{
			System.out.println("Command dose not exist");
		}
	}
		
	//set of command actions a user can call
	
	/**
	 * @Summary moves the current player in the given direction
	 * @param direction
	 */
	void Move(moveDirection d){
		switch(d) {
		case up:
			System.out.println("up");
			break;
		case down:
			System.out.println("down");
			break;
		case left:
			System.out.println("left");
			break;
		case right:
			System.out.println("right");
			break;
		}
	}

	/**
	 * @Summary sets the current player to the number given
	 * @param player
	 */
	void SetPlayer(int player) {
		System.out.println("palyer " + player);
	}
	
}
