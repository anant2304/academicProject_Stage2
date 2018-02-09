package Sigurd;

import java.awt.*;

import javax.swing.*;
import java.util.*;

import Sigurd.BoardObjects.*;

public class Game {
	static Controler controler;
	static BoardObject currObject;
	
	static JFrame window;
	
	static ArrayList<PlayerObject> playerList = new ArrayList<PlayerObject>();
	static ArrayList<WeaponObject> weaponList = new ArrayList<WeaponObject>();
	
	private Game() {}
	
	/**
	 * @Summary the main that runs the game
	 * @param args
	 */
	public static void main(String[] args) {
		controler = new Controler();
		
		CreateWindow();
		PlacePlayers();
		PlaceWeapons();
		SetCurrentObject(0, false);
	}
	
	/**
	 * @Summary creates the window that holds all the panels
	 */
	@SuppressWarnings("static-access")
	static void CreateWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		
		window.add(Board.GetBoard().GetBoardPanel(), BorderLayout.EAST);
		window.add(new CommandPanel(), BorderLayout.SOUTH);
		
		window.pack();
		window.setVisible(true);
	}
	
	/**
	 * @Summary creates a dummy player for testing purposes
	 * @param number assigned to it to tell them apart in logs
	 */
	static void CreateDumbyPalyer(int num) {
		PlayerObject p = new PlayerObject(6,7,Color.BLUE, "player:"+num);
		playerList.add(p);
	}
	/**
	 * @Summary creates a dummy weapon for testing purposes
	 * @param number assigned to it to tell them apart in logs
	 */
	static void CreateDumbyWeapon(int num) {
		WeaponObject w = new WeaponObject(6,7,new Character('H'), "weapon:"+num);
		weaponList.add(w);
	}
	
	/**
	 * @Summary creates and places all the players onto the board
	 */
	static void PlacePlayers() {
		playerList.add(new PlayerObject(9, 0, Color.decode("#ffffff"), "White"));
		playerList.add(new PlayerObject(14, 0, Color.decode("#00ff00"), "Green"));
		playerList.add(new PlayerObject(23, 6, Color.decode("#326872"), "Peacock"));
		playerList.add(new PlayerObject(23, 19, Color.decode("#8E4585"), "Plum"));
		playerList.add(new PlayerObject(7, 24, Color.decode("#ff2400"), "Scarlet"));
		playerList.add(new PlayerObject(0, 17, Color.decode("#ffdb58"), "Mustard"));
	}
	
	/**
	 * @Summary creates and places all weapons onto the board
	 */
	static void PlaceWeapons() {
		weaponList.add(new WeaponObject(3,4,new Character('R'),"Rope"));
		weaponList.add(new WeaponObject(4,12,new Character('D'),"Dagger"));
		weaponList.add(new WeaponObject(3,21,new Character('W'),"Wrench"));
		weaponList.add(new WeaponObject(10,3,new Character('P'),"Pistol"));
		weaponList.add(new WeaponObject(10,21,new Character('C'),"CandleStick"));
		weaponList.add(new WeaponObject(20,22,new Character('L'),"LeadPipe"));
	}
	
	/**
	 * @ Summary Sets the current object to a given one
	 * @param index
	 * @param isPlayer
	 */
	public static void SetCurrentObject(int index, boolean isPlayer) {
		if(isPlayer)
			currObject = playerList.get(index);
		else
			currObject = weaponList.get(index);
	}
	
	/**
	 * @Summary returns bool of if there is an object at the given index in list. checks player list if isPlayer is true, weapon list otherwise.
	 * @param index
	 * @param isPlayer
	 * @return
	 */
	public static boolean ObjectExistes(int index, boolean isPlayer) {
		if(isPlayer) {
			if(index < playerList.size())
				return (playerList.get(index) != null);
		}
		else {
			if(index < weaponList.size())
				return (weaponList.get(index) != null);
		}
		return false;
		
	}
	
	/**
	 * @Summary returns the working instance of the controler
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
