package Cards;

import java.util.*;

public class Deck {

	//only pass references to these cards, to have a card is to just have a reference to one of these elements
	private Map<String,PlayerCard> players = new HashMap<String,PlayerCard>();
	private Map<String,RoomCard> rooms = new HashMap<String,RoomCard>();
	private Map<String,WeaponCard> weapons = new HashMap<String,WeaponCard>();
	private Stack<Card> deck = new Stack<Card>();
	
	public Deck(){
		FillCards();
		FillDeck();
		ShuffleDeck();
	}
	
	private void FillCards() {//TODO : fill in all the cards
	}
	
	private void FillDeck() {
		//TODO : 
	}
	
	private void ShuffleDeck() {}//TODO : 
	
	public Card DrawFromDeck() {
		return null;
		//TODO : 
	}
	
	public int getDeckSize() {
		return deck.size();
	}
	
	public Card getCard(String name, Class<?> typeOfCard ) { //TODO
		return null;
	}
	
}
