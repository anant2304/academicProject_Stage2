package bots;

import gameengine.*;

public class AFKBot implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the board or the player objects
    // It may only inspect the state of the board and the player objects


    public AFKBot (Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
    }

    public String getName() {
        return "Bot2"; // must match the class name
    }

    public String getCommand() {
        // Add your code here
        return "done";
    }

    public String getMove() {
        // Add your code here
        return "r";
    }

    public String getSuspect() {
        // Add your code here
        return Names.SUSPECT_NAMES[0];
    }

    public String getWeapon() {
        // Add your code here
        return Names.WEAPON_NAMES[0];
    }

    public String getRoom() {
        // Add your code here
        return Names.ROOM_NAMES[0];
    }

    public String getDoor() {
        // Add your code here
        return "1";
    }

    public String getCard(Cards matchingCards) {
        // Add your code here
        return matchingCards.get().toString();
    }

    public void notifyResponse(Log response) {
        // Add your code here
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyPlayerName(String playerName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void notifyTurnOver(String playerName, String position) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void notifyQuery(String playerName, String query) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void notifyReply(String playerName, boolean cardShown) {
        // TODO Auto-generated method stub
        
    }
}
