package Cards;

import java.util.*;
import Sigurd.*;
import Sigurd.BoardObjects.*;

public class Deck {

    // only pass references to these cards, to have a card is to just have a
    // reference to one of these elements
    private Map<String, PlayerCard> playerCards;
    private Map<String, RoomCard> roomCards;
    private Map<String, WeaponCard> weaponCards;
    private Stack<Card> deck = new Stack<Card>();
    private Card[] envelope;

    private Random rand = new Random();

    public Deck() {
        FillCards();
        FillEnvelpoe();
        FillDeck();
        ShuffleDeck();
    }

    private void FillEnvelpoe() {
        if (envelope != null)
            throw new RuntimeException("Envelope allready filled.");
        if (playerCards == null || weaponCards == null || roomCards == null)
            throw new RuntimeException("Cards do not exist yet.");

        envelope = new Card[3];
        envelope[0] = GetEnvelopeCard(playerCards);
        envelope[1] = GetEnvelopeCard(weaponCards);
        envelope[2] = GetEnvelopeCard(roomCards);

        for (Card c : envelope)
            c.isInEnvelope = true;
    }

    public Card[] GetEnvelope() {
        return envelope;
    }

    private Card GetEnvelopeCard(Map<String, ? extends Card> map) {
        int cardIndex = rand.nextInt(map.size());
        Card randCard = null;
        for (Card c : map.values()) {
            if (cardIndex-- == 0)
                randCard = c;
        }
        return randCard;
    }

    private void FillCards() {
        if (playerCards != null || weaponCards != null || roomCards != null)
            throw new RuntimeException("Cards allready filled.");

        playerCards = new HashMap<String, PlayerCard>();
        roomCards = new HashMap<String, RoomCard>();
        weaponCards = new HashMap<String, WeaponCard>();

        Collection<PlayerObject> characters = Game.GetAllCharcters();
        Collection<WeaponObject> weapons = Game.GetAllWeapons();
        Room[] rooms = Game.GetBoard().GetRooms();

        for (PlayerObject p : characters) {
            playerCards.put(p.GetObjectName(), new PlayerCard(p.GetObjectName(), p));
        }

        for (WeaponObject w : weapons) {
            weaponCards.put(w.GetObjectName(), new WeaponCard(w.GetObjectName(), w));
        }

        for (Room r : rooms) {
            if (r.GetName().equals(Reasource.BASEMENTNAME) == false)
                roomCards.put(r.GetName(), new RoomCard(r.GetName(), r));
        }
    }

    private void FillDeck() {
        String ErrorPlaces = "";

        if (playerCards.isEmpty())
            ErrorPlaces += " Players";

        if (roomCards.isEmpty())
            ErrorPlaces += " Rooms";

        if (weaponCards.isEmpty())
            ErrorPlaces += " Weapons";

        if (ErrorPlaces.equals("") == false)
            throw new RuntimeException("No cards found for" + ErrorPlaces);

        for (Card c : playerCards.values()) {
            if (c.isInEnvelope != true)
                deck.push(c);
        }
        for (Card c : weaponCards.values()) {
            if (c.isInEnvelope != true)
                deck.push(c);
        }
        for (Card c : roomCards.values()) {
            if (c.isInEnvelope != true)
                deck.push(c);
        }
    }

    private void ShuffleDeck() {
        ArrayList<Card> deckList = new ArrayList<Card>(deck);

        Collections.shuffle(deckList);

        deck = new Stack<Card>();
        for (int i = 0; i < deckList.size(); i++) {
            deck.push(deckList.get(i));
        }
    }

    public Card DrawCard() {
        if (IsEmpty() == false)
            return deck.pop();
        throw new IllegalStateException("No more cards");
    }

    public int Size() {
        return deck.size();
    }

    public boolean IsEmpty() {
        return deck.size() == 0;
    }

    @SuppressWarnings("unchecked")
    public <E extends Card> E GetCard(String name, Class<E> typeOfCard) {
        E temp;

        if (typeOfCard == PlayerCard.class && playerCards != null && playerCards.isEmpty() == false) {
            if ((temp = (E) playerCards.get(name)) != null)
                return temp;
        } else if (typeOfCard == RoomCard.class && roomCards != null && roomCards.isEmpty() == false) {
            if ((temp = (E) roomCards.get(name)) != null)
                return temp;
        } else if (typeOfCard == WeaponCard.class && weaponCards != null && weaponCards.isEmpty() == false) {
            if ((temp = (E) weaponCards.get(name)) != null)
                return temp;
        } else
            throw new RuntimeException("Tryed to get a card with an incorect type in deck");

        throw new RuntimeException("Item not found when retriving from deck");
    }

    public Iterator<? extends Card> GetAllCards(Class<? extends Card> typeOfCard) {
        if (typeOfCard == PlayerCard.class && playerCards != null && playerCards.isEmpty() == false) {
            return playerCards.values().iterator();
        } else if (typeOfCard == RoomCard.class && roomCards != null && roomCards.isEmpty() == false) {
            return roomCards.values().iterator();
        } else if (typeOfCard == WeaponCard.class && weaponCards != null && weaponCards.isEmpty() == false) {
            return weaponCards.values().iterator();
        } else
            throw new RuntimeException("Tryed to get cards with an incorect type in deck");
    }

    public boolean CompareToEnvelope(Card character, Card weapon, Card room) {
        return envelope[0].equals(character) && envelope[1].equals(weapon) && envelope[2].equals(room);
    }
}
