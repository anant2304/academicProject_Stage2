package Sigurd.Questions;

import java.util.Iterator;
import Sigurd.*;
import java.util.Stack;

import Cards.*;

public class Question extends AbstractQuestion {
    private Stack<Player> playersToAsk;
    
    public Question(Player player, Iterable<Player> allPlayers)
    {
        super(player);
        SetupPlayersToAsk(allPlayers);
    }
    
    private void SetupPlayersToAsk(Iterable<Player> players) {
        if(players.iterator().hasNext() == false)
            throw new IllegalArgumentException("No other players to ask question to");
        
        playersToAsk = new Stack<Player>();
        
        // Put players after current player into stack
        Iterator<Player> it = players.iterator();
        while(it.hasNext() && it.next().equals(asker) == false);
        while(it.hasNext()) playersToAsk.push(it.next());
        
        // Put players before current player into stack
        it = players.iterator();
        Player p;
        while (it.hasNext() && (p = it.next()) != asker) playersToAsk.push(p);
    }
    
    public void StartAskingQuestion(Room r)
    {
        room = Game.GetCard(r.GetName(), RoomCard.class);
        Activate();
    }
    
    @Override
    public String toString()
    {
        return asker + " asked if it was " + 
                character.getName() + " in the " + 
                room.getName() + " with the " + 
                weapon.getName();
    }

    @Override
    protected void DoneWithInput() {
        Game.GetDisplay().SendMessage(this.toString());
        // TODO continue with question
        Deactivate();
    }
}
