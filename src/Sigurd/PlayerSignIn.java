package Sigurd;
import java.util.Vector;

import Sigurd.BoardObjects.PlayerObject;

import java.util.Collection;

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
    public int playerCount;
    
    public PlayerSignIn()
    {
        display= Game.GetDisplay();
        currPossition = 0;
    }
    
    public Vector<Player> getPlayers() 
    {
    	return players;
    }
    
    public Player NextPlayer() 
    {
    	return players.get(currPossition = (currPossition + 1) % players.size());
    }
    
    public void Commands(String command)
    {
        Game.GetDisplay().SendMessage(command);
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
			display.SendMessage(Language.English[18]);
		else
            Game.StartGame();
    }
    
    void CheckPlayersInGame(){
    	if(players.isEmpty())
			display.SendMessage(Language.English[19]);
		for(Player p : players) {
			display.SendMessage(p.GetPlayerName() + " is playing " + p.GetCharacterName());
		}
    }
    
    void CheckCharactersLeft() {
    	Collection<PlayerObject> characters = Game.GetAllCharcters();
    	int i = 0;
    	    	
    	for(PlayerObject p : characters) {
    		if(p.HasPlayer() == false)
    			display.SendMessage(p.GetObjectName() + " is still avalable");
    		else
    			i++;
    	}
    	
    	if(i == characters.size()) 
    		display.SendMessage(Language.English[20]);
    }
    
    void addPlayer(String command) {
    	String[] playerEnteries = command.split("\\s+");
		
		if(playerEnteries.length != 2) {
			display.SendMessage(Language.English[21]);
		}
		else if(Game.DoesCharacterExist(playerEnteries[1]) == false) {
			display.SendMessage(Language.English[22]);
		}
		else if(players.size() >= 6) {
			display.SendMessage(Language.English[23]);
		}
		else if(Game.GetCharacter(playerEnteries[1]).HasPlayer()) {
			display.SendMessage("Someone is already playing " + playerEnteries[1]);
		}
		else {
			PlayerObject p = Game.GetCharacter(playerEnteries[1]);
			players.add(new Player(playerEnteries[0] ,p));
            playerCount++;
			display.SendMessage(playerEnteries[0] + " Is playing " + playerEnteries[1]);	
			return;
		}
		display.SendMessage(Language.English[24]);
    }
}
