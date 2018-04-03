package Cards;

public abstract class Card {

	private String name;
	private boolean canEveryOneSee = false;
	boolean isInEnvelope;
	
	protected Card(String _name) {
		name = _name;
		isInEnvelope = false;
	}
	
	public String getName() {
		return name;
	}
	
	public void SetCanEveryOneSee() {
		canEveryOneSee = true;
	}
	
	public boolean CanEveryOneSee() {
		return canEveryOneSee;
	}
	
	public boolean IsInEnvelope()
	{
	    return isInEnvelope;
	}
}
