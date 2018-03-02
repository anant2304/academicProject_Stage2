package Cards;

public abstract class Card {

	private String name;
	private boolean canEveryOneSee = false;
	
	public Card(String _name) {
		name = _name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setcanEveryOneSee() {
		canEveryOneSee = true;
	}
	
	public boolean CanEveryOneSee() {
		return canEveryOneSee;
	}
}
