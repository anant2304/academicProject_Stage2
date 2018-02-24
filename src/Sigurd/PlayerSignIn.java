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
    
    public PlayerObject NextPlayer() 
    {
    	return players.get((currPossition++) % players.size());
    }
    
    public void Commands(String command)
    {
    	
    	if(command.equals("done")) {
    		if(players.size() < 2)
    			display.SendMessage("you must have at least 2 players to paly");
    		else
                Game.StartGame();
    	}
    	else if(command.equals("players")) {
    		for(PlayerObject p : players) {
    			display.SendMessage(p.getPlayerName() + " is playing " + p.GetObjectName());
    		}
    	}
    	else {
    		
    		String[] playerEnteries = command.split("\\s+");
    		
    		if(playerEnteries.length != 2) {
    			display.SendMessage("Incorect number of elements entered");
    		}
    		else if(Game.DoseCharacterExist(playerEnteries[1]) == false) {
    			display.SendMessage("the character entered is not recodnised");
    		}
    		else if(players.size() >= 6) {
    			display.SendMessage("You may only have up to six players\nEnter \"done\" to start the game");
    		}
    		else if(Game.GetCharacter(playerEnteries[1]).getPlayerName() != null) {
    			display.SendMessage("Someone is already playing " + playerEnteries[1]);
    		}
    		else {
    			PlayerObject p = Game.GetCharacter(playerEnteries[1]);
    			players.add(p);
    			p.setPlayerName(playerEnteries[0]);
    			display.SendMessage(playerEnteries[0] + " Is playing " + playerEnteries[1]);	
    			return;
    		}
    		display.SendMessage("Please enter in the form \n[Player Name] [Character Name]");
       	}
	    
    }
}
