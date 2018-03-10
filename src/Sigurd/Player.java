package Sigurd;

import java.util.HashSet;
import java.util.Set;

import Cards.Card;
import Sigurd.BoardObjects.PlayerObject;

public class Player {

    private final String playerName;
    private final PlayerObject playerObject;
    private final Set<Card> playerCards;
    

    Player(String name, PlayerObject o){
        playerName = name;
        playerObject = o;
        playerCards = new HashSet<Card>();
        
        o.SetPlayer();
    }

    public String GetPlayerName() {
        return playerName;
    }
    
    public String GetCharacterName()
    {
        return playerObject.GetObjectName();
    }

    public PlayerObject GetPlayerObject()
    {
        return playerObject;
    }
    
    @Override
    public String toString()
    {
        return playerName + "[" + GetCharacterName() +"]";
    }
    
    public void GiveCard(Card c)
    {
        playerCards.add(c);
    }
    
    public boolean HasCard(Card c)
    {
        return playerCards.contains(c);
    }
}
