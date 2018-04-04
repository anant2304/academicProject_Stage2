package Sigurd;

import java.util.Iterator;
import java.util.Stack;

import Cards.*;

public class Question {
    private Player asker;
    private Stack<Player> playersToAsk;
    private boolean isBeingAsked;
    private boolean canAsk;
    private PlayerCard character;
    private WeaponCard weapon;
    private RoomCard room;
    
    Question(Player player, Iterable<Player> allPlayers)
    {
        asker = player;
        isBeingAsked = false;
        canAsk = true;
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

    boolean IsBeingAsked()
    {
        return isBeingAsked;
    }
    
    boolean CanAsk()
    {
        return canAsk;
    }
    
    @Override
    public String toString()
    {
        return asker + " aksked if it was " + 
                character.getName() + " in the " + 
                room.getName() + " with the " + 
                weapon.getName();
    }
}
