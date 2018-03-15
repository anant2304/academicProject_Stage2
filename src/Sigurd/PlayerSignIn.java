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
    Vector<Player> players = new Vector<Player>();
    DisplayPanel display;
    int currPossition;
    public int strength;
    
    public PlayerSignIn()
    {
        display= Game.GetDisplay();
        currPossition = 0;
    }
    
    private void add(Player p) 
    {
    	players.add(p);
    }
    
    public Vector<Player> getPlayers() 
    {
    	return players;
    }
    
    public Player NextPlayer() 
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
            addPlayer(command);
    	}
    }
    
    void FinishSignIn() {
    	if(players.size() < 2)
			display.SendMessage(Game.lang1.English[18]);
		else
            Game.StartGame();
    }
    
    void CheckPlayersInGame(){
    	if(players.isEmpty())
			display.SendMessage(Game.lang1.English[19]);
		for(Player p : players) {
			display.SendMessage(p.GetPlayerName() + " is playing " + p.GetCharacterName());
		}
    }
    
    void CheckCharactersLeft() {
    	Collection<PlayerObject> characters = Game.GetAllCharcters();
    	int i = 0;
    	
    	for(PlayerObject p : characters) {
    		if(p.HasPlayer())
    			display.SendMessage(p.GetObjectName() + " is still avalable");
    		else
    			i++;
    	}
    	
    	if(i == characters.size()) 
    		display.SendMessage(Game.lang1.English[20]);
    }
    
    void addPlayer(String command) {
    	String[] playerEnteries = command.split("\\s+");
		
		if(playerEnteries.length != 2) {
			display.SendMessage(Game.lang1.English[21]);
		}
		else if(Game.DoseCharacterExist(playerEnteries[1]) == false) {
			display.SendMessage(Game.lang1.English[22]);
		}
		else if(players.size() >= 6) {
			display.SendMessage(Game.lang1.English[23]);
		}
		else if(Game.GetCharacter(playerEnteries[1]).HasPlayer()) {
			display.SendMessage("Someone is already playing " + playerEnteries[1]);
		}
		else {
			PlayerObject p = Game.GetCharacter(playerEnteries[1]);
			players.add(new Player(playerEnteries[0] ,p));
            strength++;
			display.SendMessage(playerEnteries[0] + " Is playing " + playerEnteries[1]);	
			return;
		}
		display.SendMessage(Game.lang1.English[24]);
    }
}
