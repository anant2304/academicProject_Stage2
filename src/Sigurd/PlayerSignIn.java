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
        Game.GetDisplay().SendMessage("> " + command);
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
            case "help":
                DisplayHelp();
                break;
            default :
                addPlayer(command);
        }
    }
    
    private void DisplayHelp() {
        display.SendMessage("Type a pair of names then press enter or return to add it to the game\n" +
                "Type \"players\" to see who is currently in the game\n" +
                "Type \"characters\" to see unclaimed characters\n" +
                "If you have entered all the players type \"done\" to start the game\n" +
                "Type \"#exit\" to abort the game\n" +
                "You must have at least 2 players to play");
    }

    void FinishSignIn() {
        if(players.size() < 2)
            display.SendError("you must have at least 2 players to play");
        else
            Game.StartGame();
    }
    
    void CheckPlayersInGame(){
        if(players.isEmpty())
            display.SendMessage("There are no players in the game yet");
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
            display.SendMessage("There are no characters left unclaimed");
    }
    
    void addPlayer(String command) {
        String[] playerEnteries = command.split("\\s+");
        
        if(playerEnteries.length != 2) {
            display.SendError("Incorrect number of elements entered");
        }
        else if(Game.DoesCharacterExist(playerEnteries[1]) == false) {
            display.SendError("the character entered is not recognised");
        }
        else if(players.size() >= 6) {
            display.SendError("You may only have up to six players\nEnter \"done\" to start the game");
        }
        else if(Game.GetCharacter(playerEnteries[1]).HasPlayer()) {
            display.SendError("Someone is already playing " + playerEnteries[1]);
        }
        else {
            PlayerObject p = Game.GetCharacter(playerEnteries[1]);
            players.add(new Player(playerEnteries[0] ,p));
            playerCount++;
            display.SendMessage(playerEnteries[0] + " is playing " + playerEnteries[1]);	
            return;
        }
        display.SendMessage("Please enter in the form \n[Player Name] [Character Name]");
    }
}
