package Sigurd;

import java.awt.*;

import javax.swing.*;
import java.util.*;

import Sigurd.BoardObjects.*;

public class Game {
	static Controler controler;
	static BoardObject currObject;
	
	static JFrame window;
	
	static LinkedList<PlayerObject> playerList = new LinkedList<PlayerObject>();
	static LinkedList<WeaponObject> weaponList = new LinkedList<WeaponObject>();
	
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
		window.setLayout(new FlowLayout());
		
		window.add(Board.GetBoard().GetBoardPanel());
		window.add(new CommandPanel());
		
		window.pack();
		window.setVisible(true);
	}
	
	/**
	 * creates a dummy player for testing purposes
	 * @param number assigned to it to tell them apart in logs
	 */
	static void CreateDumbyPalyer(int num) {// TODO : change player constructor
		//PlayerObject p = new PlayerObject(6,7,new Character('H'), "player:"+num);
		//playerList.add(p);
	}
	/**
	 * creates a dummy weapon for testing purposes
	 * @param number assigned to it to tell them apart in logs
	 */
	static void CreateDumbyWeapon(int num) {
		WeaponObject w = new WeaponObject(6,7,new Character('H'), "weapon:"+num);
		weaponList.add(w);
	}
	
	/**
	 * creates and places all the players onto the board
	 */
	static void PlacePlayers() {}
	
	/**
	 * creates and places all weapons onto the board
	 */
	static void PlaceWeapons() {
		weaponList.add(new WeaponObject(7,7,new Character('H'),"Hammer"));
	}
	
	/**
	 * Sets the current object to a given one
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
	 * returns bool of if there is an object at the given index in list. checks player list if isPlayer is true, weapon list otherwise.
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
	 * returns the working instance of the controler
	 * @return
	 */
	public static Controler GetControlerInstance() {
		return controler;
	}
	
	/**
	 * returns the object that is currently being controled by commands
	 * @return
	 */
	public static BoardObject GetCurrObject() {
		return currObject;
	}
	
	/**
	 * sets the object that is currently reciving commands from the command line
	 * @param b
	 */
	public static void SetCurrObject(BoardObject b) {
		currObject = b;
	}
}
