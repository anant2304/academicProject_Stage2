package Sigurd;
import java.awt.*;
import java.util.Scanner;
import java.util.Vector;

import Sigurd.BoardObjects.PlayerObject;

import java.util.LinkedList;

/*  Class PlayerSignIn  */
class PlayerSignIn
{
    Vector<PlayerObject> players = new Vector<PlayerObject>();
    LinkedList<String> nameList = new LinkedList<String>();
    DisplayPanel display;
    int currPossition;
    
    public PlayerSignIn()
    {
        display= Game.GetDisplay();
        currPossition = 0;
    }
    
    public void add(PlayerObject p) 
    {
    	players.add(p);
    }
    
    public Vector<PlayerObject> getPlayers() 
    {
    	return players;
    }
    
    @SuppressWarnings("unchecked")
	public LinkedList<String> getNameListCopy() 
    {
    	return (LinkedList<String>) nameList.clone();
    }
    
    public PlayerObject NextPlayer() 
    {
    	return players.get((currPossition++) % players.size());
    }
    
    public void Commands(String command)
    {
    	if(command.equals("done")) {
    		if(nameList.size() < 2)
    			display.SendMessage("you must have at least 2 players to paly");
    		else
                Game.StartGame();
    		
    	}
    	else {
    		if(nameList.size() >= 6)
    			display.SendMessage("You may only have up to six players\nEnter \"done\" to start the game");
    		else {
    			nameList.addLast(command);
    			display.SendMessage(command + " has been added to the game");	
    		}
       	}
    }
}
