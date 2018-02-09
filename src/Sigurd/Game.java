package Sigurd;

import java.awt.*;

import javax.swing.*;
import java.util.*;

import Sigurd.BoardObjects.*;

public class Game {
	static Controler controler;
	static BoardObject currObject;
	static DisplayPanel display;
	
	static JFrame window;
	
	static Map<String,PlayerObject> playerMap = new HashMap<String,PlayerObject>();
	static Map<String,WeaponObject> weaponMap = new HashMap<String,WeaponObject>();
	
	private Game() {}//makeing the constructor private
	
	/**
	 * @Summary the main that runs the game
	 * @param args
	 */
	public static void main(String[] args) {
		controler = new Controler();
		display = new DisplayPanel();
		
		CreateWindow();
		PlacePlayers();
		PlaceWeapons();
		SetCurrentObject("white");
	}
	
	/**
	 * @Summary creates the window that holds all the panels
	 */
	@SuppressWarnings("static-access")
	static void CreateWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		
		window.add(Board.GetBoard().GetBoardPanel(), BorderLayout.WEST);
		window.add(new CommandPanel(), BorderLayout.SOUTH);
		window.add(display, BorderLayout.EAST);
		
		window.pack();
		window.setVisible(true);
	}
	
	/**
	 * @Summary creates and places all the players onto the board
	 */
	static void PlacePlayers() {
		playerMap.put("white",new PlayerObject(9, 0, Color.decode("#ffffff"), "White"));
		playerMap.put("green",new PlayerObject(14, 0, Color.decode("#00ff00"), "Green"));
		playerMap.put("peacock",new PlayerObject(23, 6, Color.decode("#326872"), "Peacock"));
		playerMap.put("plum",new PlayerObject(23, 19, Color.decode("#8E4585"), "Plum"));
		playerMap.put("scarlet",new PlayerObject(7, 24, Color.decode("#ff2400"), "Scarlet"));
		playerMap.put("mustard",new PlayerObject(0, 17, Color.decode("#ffdb58"), "Mustard"));
	}
	
	/**
	 * @Summary creates and places all weapons onto the board
	 */
	static void PlaceWeapons() {
		weaponMap.put("Rope",new WeaponObject(3,4,new Character('R'),"Rope"));
		weaponMap.put("dagger",new WeaponObject(4,12,new Character('D'),"Dagger"));
		weaponMap.put("wrench",new WeaponObject(3,21,new Character('W'),"Wrench"));
		weaponMap.put("pistol",new WeaponObject(10,3,new Character('P'),"Pistol"));
		weaponMap.put("candlestick",new WeaponObject(10,21,new Character('C'),"CandleStick"));
		weaponMap.put("leadpipe",new WeaponObject(20,22,new Character('L'),"LeadPipe"));
	}
	
	/**
	 * @ Summary Sets the current object to a given one
	 * @param index
	 * @param isPlayer
	 */
	public static void SetCurrentObject(String name) {
		if(playerMap.containsKey(name))
			currObject = playerMap.get(name);
		
		else if(weaponMap.containsKey(name))
			currObject = weaponMap.get(name);
		
		else
			throw new RuntimeException("tryed to set the current controled object to something that dose not exist");
	}
	
	/**
	 * returns if they there is an item in the system with the name given
	 * @param name
	 * @param isPlayer
	 * @return
	 */
	public static boolean ObjectExistes(String name) {
		if(playerMap.containsKey(name)) 
			return true;
		else
			return weaponMap.containsKey(name);
		
	}
	
	/**
	 * @Summary returns the working instance of the controller
	 * @return
	 */
	public static Controler GetControlerInstance() {
		return controler;
	}
	
	/**
	 * @Summary returns the object that is currently being controled by commands
	 * @return
	 */
	public static BoardObject GetCurrObject() {
		return currObject;
	}
	
	/**
	 * @Summary sets the object that is currently reciving commands from the command line
	 * @param b
	 */
	public static void SetCurrObject(BoardObject b) {
		currObject = b;
	}
}
