package Sigurd;
import java.awt.*;
import java.util.Scanner;
import java.util.Vector;

import Sigurd.BoardObjects.PlayerObject;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Anant 
 * 
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246 
 */

/*  Class PlayerSignIn  */
class PlayerSignIn
{
    Vector<PlayerObject> players = new Vector<PlayerObject>();
    DisplayPanel display;
    int currPossition;
    public static int strength;
    
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
    	switch(command) {
    	case "done" :
    		FinishSignIn();
    		break;
    	case "players" :
    		CheckPlayersInGame();
    		break;
    	case "characters" : 
    		CheckCharactersLeft();
    		break;
    	default : 
    		strength++;
            addPlayer(command);
    	}
    }
    
    void FinishSignIn() {
    	if(players.size() < 2)
			display.SendMessage("you must have at least 2 players to paly");
		else
            Game.StartGame();
    }
    
    void CheckPlayersInGame(){
    	if(players.isEmpty())
			display.SendMessage("There are no players in the game yet");
		for(PlayerObject p : players) {
			display.SendMessage(p.getPlayerName() + " is playing " + p.GetObjectName());
		}
    }
    
    void CheckCharactersLeft() {
    	Collection<PlayerObject> characters = Game.GetAllCharcters();
    	int i = 0;
    	
    	for(PlayerObject p : characters) {
    		if(p.getPlayerName() == null)
    			display.SendMessage(p.GetObjectName() + " is still avalable");
    		else
    			i++;
    	}
    	
    	if(i == characters.size()) 
    		display.SendMessage("There are no characters left unclamed");
    }
    
    void addPlayer(String command) {
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
