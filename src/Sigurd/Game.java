package Sigurd;

import java.awt.*;

import javax.swing.*;
import java.util.*;

import Sigurd.BoardObjects.*;

/**
 * Servers an an entry point and ties the different classes together.
 * @author Peter Major
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class Game {
	private static BoardObject currentObject;
	private static CommandPanel command;
	private static Board board;
	private static DisplayPanel display;
	
	private static Stack<Turn> turnList = new Stack<Turn>();
	
	private static Map<String,PlayerObject> playerMap = new HashMap<String,PlayerObject>();
	private static Map<String,WeaponObject> weaponMap = new HashMap<String,WeaponObject>();
	
	/**
	 * @Summary the main that runs the game
	 * @param args
	 */
	public static void main(String[] args) {
		command = new CommandPanel();
		display = new DisplayPanel();
		board = GetBoard();
	
		
		PlacePlayers();
		PlaceWeapons();
        CreateWindow();
		SetCurrentObject("white");
	}
	
	/**
	 * private constructor
	 */
	private Game() {}
	
	/**
	 * @Summary creates the window that holds all the panels
	 */
    @SuppressWarnings("static-access")
	private static void CreateWindow() {
        JFrame window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		
		window.add(command, BorderLayout.SOUTH);
		window.add(board.GetBoardPanel(), BorderLayout.CENTER);
		window.add(display, BorderLayout.EAST);
		
		window.pack();
		window.setVisible(true);
		command.TakeFocus();
		window.setResizable(false); //makes the frame non-resizable
	}
	
	/**
	 * @Summary creates and places all the players onto the board
	 */
	private static void PlacePlayers() {
		playerMap.put("white",new PlayerObject(new Coordinates(9, 0), Color.decode("#ffffff"), "White"));
		playerMap.put("green",new PlayerObject(new Coordinates(14, 0), Color.decode("#00ff00"), "Green"));
		playerMap.put("peacock",new PlayerObject(new Coordinates(23, 6), Color.decode("#326872"), "Peacock"));
		playerMap.put("plum",new PlayerObject(new Coordinates(23, 19), Color.decode("#8E4585"), "Plum"));
		playerMap.put("scarlet",new PlayerObject(new Coordinates(7, 24), Color.decode("#ff2400"), "Scarlet"));
		playerMap.put("mustard",new PlayerObject(new Coordinates(0, 17), Color.decode("#ffdb58"), "Mustard"));

		for(PlayerObject p : playerMap.values())
		    board.AddMovable(p);
	}
	
	/**
	 * @Summary creates and places all weapons onto the board
	 */
	private static void PlaceWeapons() {
		weaponMap.put("rope",new WeaponObject(new Coordinates(3,4),new Character('R'),"Rope"));
		weaponMap.put("dagger",new WeaponObject(new Coordinates(4,12),new Character('D'),"Dagger"));
		weaponMap.put("wrench",new WeaponObject(new Coordinates(3,21),new Character('W'),"Wrench"));
		weaponMap.put("pistol",new WeaponObject(new Coordinates(10,3),new Character('P'),"Pistol"));
		weaponMap.put("candlestick",new WeaponObject(new Coordinates(10,21),new Character('C'),"CandleStick"));
		weaponMap.put("leadpipe",new WeaponObject(new Coordinates(20,22),new Character('L'),"LeadPipe"));
		
        for(WeaponObject p : weaponMap.values())
            board.AddMovable(p);
	}
	
	/**
	 * @Summary creates a new turn with the next player
	 */
	public static void NextTurn() {
		//TODO : there is currently no list of players so i can't incrment them
		NewTurn(null);
	}
	
	/**
	 * @Summary ends the last turn and starts a new one
	 */
	public static void NewTurn(PlayerObject p) {
		turnList.push(new Turn(p));
	}
	
	public static void UndoTurn() {//TODO : this only destroys the turn it dose not roll back the curr player
		turnList.pop().UndoAllMoves();
	}
	
	/**
	 * @Summary returns a reference to the current turn
	 */
	public static Turn CurrentTurn() {
		return turnList.peek();
	}
	
	/**
	 * @ Summary Sets the current object to a given one
	 * @param index
	 * @param isPlayer
	 */
	public static void SetCurrentObject(String name) {
		if(playerMap.containsKey(name))
			currentObject = playerMap.get(name);
		else if(weaponMap.containsKey(name))
			currentObject = weaponMap.get(name);
		else
			throw new RuntimeException("tried to set the current controled object to something that dose not exist");
	}
	
	/**
	 * returns true if they is an item in the system with the name given
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
	 * @Summary returns the object that is currently being controlled by commands
	 * @return
	 */
	public static BoardObject GetCurrentObject() {
		return currentObject;
	}
	
	/**
	 * @Summary returns the game board
	 */
	public static Board GetBoard() {
		return board;
	}
	
	/**
	 * 
	 * @Summary returns the display pannel
	 */
	public static DisplayPanel GetDisplay() {
		return display;
	}
}
