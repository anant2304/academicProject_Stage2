package Sigurd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Cards.Card;
import Cards.*;
import Sigurd.BoardObjects.PlayerObject;

/** Team: Sigurd
* Student Numbers:
* 16751195, 16202907, 16375246
*/

public class Player {

    private final String playerName;
    private final PlayerObject playerObject;
    private final Set<Card> ownedCards;
    private final Set<Card> seenCards;

    private boolean isOutOfGame;

    Player(String name, PlayerObject o) {
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

    public String GetCharacterName() {
        return playerObject.GetObjectName();
    }

    public PlayerObject GetPlayerObject() {
        return playerObject;
    }

    @Override
    public String toString() {
        return playerName + "[" + GetCharacterName() + "]";
    }

    public void GiveCard(Card c) {
        ownedCards.add(c);
    }

    public void SeeCard(Card c) {
        if (HasCard(c))
            throw new IllegalArgumentException("Trying to see a card you own: " + c.getName());
        if (c.CanEveryOneSee())
            throw new IllegalArgumentException("Trying to see a card everyone can see: " + c.getName());
        if (c.IsInEnvelope())
            throw new IllegalArgumentException("Trying to see a card form the envelope: " + c.getName());

        seenCards.add(c);
    }

    public boolean HasCard(Card c) {
        return ownedCards.contains(c) && c.CanEveryOneSee() == false;
    }

    public boolean HasSeenCard(Card c) {
        return seenCards.contains(c);
    }

    public boolean IsOutOfGame() {
        return isOutOfGame;
    }

    public void KnockOutOfGame() {
        isOutOfGame = true;
    }

    public String GetNotes() {
        StringBuilder sb = new StringBuilder(String.format("%-30s \n", "Notes: " + this));
        sb.append("   X: You have this card.\n");
        sb.append("   V: You have seen this card.\n");
        sb.append("   A: Everyone sees this card.\n");

        sb.append("\nPlayers");
        sb.append(GetCardNotesFromIterator(Game.GetCards(PlayerCard.class)));

        sb.append("\nWeapons");
        sb.append(GetCardNotesFromIterator(Game.GetCards(WeaponCard.class)));

        sb.append("\nRooms");
        sb.append(GetCardNotesFromIterator(Game.GetCards(RoomCard.class)));

        return sb.toString();
    }

    private String GetCardNotesFromIterator(Iterator<? extends Card> cards) {
        StringBuilder sb = new StringBuilder();
        while (cards.hasNext()) {
            Card c = cards.next();
            char displayChar;
            if (HasCard(c)) // Check if player has card
                displayChar = 'X';
            else if (HasSeenCard(c))
                displayChar = 'V';
            else if (c.CanEveryOneSee())
                displayChar = 'A';
            else
                displayChar = ' ';

            sb.append(String.format("\n   %-20s%c", c.getName(), displayChar));
        }
        return sb.toString();
    }
}
