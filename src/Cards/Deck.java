package Cards;

import java.util.*;

public class Deck {

	//only pass references to these cards, to have a card is to just have a reference to one of these elements
	private Map<String,PlayerCard> playerCards = new HashMap<String,PlayerCard>();
	private Map<String,RoomCard> roomCards = new HashMap<String,RoomCard>();
	private Map<String,WeaponCard> weaponCards = new HashMap<String,WeaponCard>();
	private Stack<Card> deck = new Stack<Card>();
	
	private Random rand = new Random();
	
	private static Deck instance;//this is here JUST to make the get card method static
	
	public Deck(){
		FillCards();
		FillDeck();
		ShuffleDeck();
		
		if(instance == null)
			instance = this;
		else
			throw new RuntimeException("Duplicate Deck Createded");
	}
	
	private void FillCards() {//TODO : fill in all the cards
		//i think this should be done by pulling from a centeral location where we define
		//all the players, rooms and weapons... so below is just test values
		
		playerCards.put("peter", new PlayerCard("peter", null));
		roomCards.put("mancave", new RoomCard("mancave",null));
		weaponCards.put("computer", new WeaponCard("computer",null));
	}
	
	private void FillDeck() {
		String ErrorPlaces = "";
		
		if(playerCards.isEmpty())
			ErrorPlaces += " Players";
			
		if(roomCards.isEmpty())
			ErrorPlaces += " Rooms";
		
		if(weaponCards.isEmpty())
			ErrorPlaces += " Weapons";
		
		if(ErrorPlaces.equals("") == false)
			throw new RuntimeException("No cards found for" + ErrorPlaces);
			
		for(String key : playerCards.keySet()) 
			deck.push(playerCards.get(key));
		for(String key : roomCards.keySet()) 
			deck.push(roomCards.get(key));
		for(String key : weaponCards.keySet()) 
			deck.push(weaponCards.get(key));
	}
	
	
	private void ShuffleDeck() {//TODO : this is an O(n) shuffel, look into better shuffeling algorithms
		Card[][] array = new Card[3][Math.max(playerCards.size(), 
						Math.max(roomCards.size(), weaponCards.size()))];
		int sizeOfArray[] = new int[3];
		
		while(deck.isEmpty() == false) {
			int num = rand.nextInt(3);
			
			while(sizeOfArray[num] >= array[num].length)
				num = (num+1) % 3;
			
			array[num][sizeOfArray[num]] = deck.pop();
			sizeOfArray[num]++;
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < sizeOfArray[i]; j++) {
				int num = rand.nextInt(sizeOfArray[i] - j) + j;
				Card temp = array[i][num];
				array[i][num] = array[i][j];
				array[i][j] = temp;
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < sizeOfArray[i]; j++) {
				deck.push(array[i][j]);
			}
		}
	}
	
	
	public Card DrawCard() {
		if(IsEmpty() == false)
			return deck.pop();
		return null;
	}
	
	
	public int Size() {
		return deck.size();
	}
	
	
	public boolean IsEmpty() {
		return deck.size() == 0;
	}
	
	
	public static Card getCard(String name, Class<?> typeOfCard ) {
		Card temp;
		
		if(typeOfCard == PlayerCard.class) {//can't do swtich statment on Class<?>
			if((temp = instance.playerCards.get(name)) != null)
				return temp;
		}
		else if(typeOfCard == RoomCard.class) {
			if((temp = instance.roomCards.get(name)) != null)
				return temp;			
		}
		else if(typeOfCard == WeaponCard.class) {
			if((temp = instance.weaponCards.get(name)) != null)
				return temp;			
		}
		else throw new RuntimeException("Tryed to get a card with an incorect type in deck");
		
		throw new RuntimeException("Item not found when retriving from deck");
	}
	
	
	public static void main(String[] args) {
		Deck deck = new Deck();
		
		System.out.println(deck.Size());
		System.out.println(deck.IsEmpty());
		System.out.println(deck.DrawCard());
		System.out.println(deck.DrawCard());
		System.out.println(deck.Size());
		System.out.println(deck.DrawCard());
		System.out.println(deck.IsEmpty());
		System.out.println(Deck.getCard("peter",PlayerCard.class));
		System.out.println(deck.DrawCard());
	}
}
