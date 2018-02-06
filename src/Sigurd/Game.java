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
		CreateDumbyPalyer(0);
		CreateDumbyPalyer(1);
		SetCurrentObject(0, true);

	}
	
	/**
	 * @Summary creates the window that holds all the panels
	 */
	static void CreateWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		
		window.add(Board.GetBoard().GetBoardPanel());
		window.add(new CommandPanel());
		
		window.pack();
		window.setVisible(true);
	}
	static void CreateDumbyPalyer(int num) {
		PlayerObject p = new PlayerObject(6,7,new Character('H'), "Dumby:"+num);
		playerList.add(p);
	}
	
	public static void SetCurrentObject(int index, boolean isPlayer) {
		if(isPlayer)
			currObject = playerList.get(index);
		else
			currObject = weaponList.get(index);
	}
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
	
	public static Controler GetControlerInstance() {
		return controler;
	}
	
	public static BoardObject GetCurrObject() {
		return currObject;
	}
	public static void SetCurrObject(BoardObject b) {
		currObject = b;
	}
}
