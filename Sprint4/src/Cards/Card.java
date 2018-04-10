package Cards;
/**
 * Abstract superclass for cards.
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Adrian Wennberg, Peter Major
 */
public abstract class Card {

	private String name;
	private boolean canEveryOneSee = false;
	private boolean isInEnvelope;
	
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
	
    public void PutInEnvelope()
    {
        isInEnvelope = true;
    }
	
	public boolean CanEveryOneSee() {
		return canEveryOneSee;
	}
	
	public boolean IsInEnvelope()
	{
	    return isInEnvelope;
	}
}
