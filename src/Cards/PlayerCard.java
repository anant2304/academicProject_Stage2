package Cards;

import Sigurd.BoardObjects.PlayerObject;

public class PlayerCard extends Card {

	private PlayerObject myPlayerObject;
	
	public PlayerCard(String _name, PlayerObject player) {
		super(_name);
		myPlayerObject = player;
	}
	
	public PlayerObject getPlayer() {
		return myPlayerObject;
	}
}
