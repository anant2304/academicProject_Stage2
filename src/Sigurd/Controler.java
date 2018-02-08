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
	
	public Controler() {
		//set all the commands here
		
		//pleb commands
		coms.put("u", () -> Move(moveDirection.up));
		coms.put("d", () -> Move(moveDirection.down));
		coms.put("l", () -> Move(moveDirection.left));
		coms.put("r", () -> Move(moveDirection.right));
		coms.put("exit", () -> System.exit(0));

		//dev commands
		coms.put("setplayerwhite", () -> SetPlayer(1));
		coms.put("setplayergreen", () -> SetPlayer(2));
		coms.put("setplayerpeacock", () -> SetPlayer(3));
		coms.put("setplayerplum", () -> SetPlayer(4));
		coms.put("setplayerscarlet", () -> SetPlayer(5));
		coms.put("setplayermustard", () -> SetPlayer(6));
		coms.put("setweaponrope", () -> SetWeapon(1));
		coms.put("setweapondagger", () -> SetWeapon(2));
		coms.put("setweaponwrench", () -> SetWeapon(3));
		coms.put("setweaponpistol", () -> SetWeapon(4));
		coms.put("setweaponcandlestick", () -> SetWeapon(5));
		coms.put("setweaponleadpipe", () -> SetWeapon(6));
	}
	
	/**
	 * @Summary Executes the given command
	 * 
	 * @param command
	 */
	public  void ExicuteCommand(String command) {
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
		if(Game.currObject != null) {
			Game.currObject.Move(d);
		}
		else System.out.println("Current object is null");
	}

	/**
	 * @Summary sets the current player to the number given
	 * @param player
	 */
	void SetPlayer(int player) {
		if(Game.ObjectExistes(player-1, true)) {
			Game.SetCurrentObject(player-1, true);
			System.out.println("palyer " + player);
		}
		else {
			System.out.println("Player not found");
		}
	}
	
	/**
	 * @Summary sets the current controlled object to the weapon corresponding to the index given
	 */
	void SetWeapon(int weapon) {
		if(Game.ObjectExistes(weapon-1, false)) {
			Game.SetCurrentObject(weapon-1, false);
			System.out.println("weapon " + weapon);
		}
		else {
			System.out.println("weapon not found");
		}
	}
}
