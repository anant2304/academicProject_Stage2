package Sigurd;

import java.util.HashSet;
import java.util.Set;

import Cards.Card;
import Sigurd.BoardObjects.PlayerObject;

public class Player {

    private final String playerName;
    private final PlayerObject playerObject;
    private final Set<Card> ownedCards;
    private final Set<Card> seenCards;
    
    private boolean isOutOfGame;

    Player(String name, PlayerObject o){
        playerName = name;
        playerObject = o;
        ownedCards = new HashSet<Card>();
        seenCards = new HashSet<Card>();
        isOutOfGame = false;
        
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
        ownedCards.add(c);
    }
    
    public void SeeCard(Card c)
    {
        if(HasCard(c))
            throw new IllegalArgumentException("Trying to see a card you own: " + c.getName());
        if(c.CanEveryOneSee())
            throw new IllegalArgumentException("Trying to see a card everyone can see: " + c.getName());
        if(c.IsInEnvelope())
            throw new IllegalArgumentException("Trying to see a card form the envelope: " + c.getName());
        
        seenCards.add(c);
    }
    
    public boolean HasCard(Card c)
    {
        return ownedCards.contains(c);
    }
    
    public boolean HasSeenCard(Card c)
    {
        return seenCards.contains(c);
    }
    
    public boolean IsOutOfGame() {
    	return isOutOfGame;
    }

    public void KnockOutOfGame() {
    	isOutOfGame = true;
    }
}
