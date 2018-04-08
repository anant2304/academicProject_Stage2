package Cards;

import Sigurd.BoardObjects.PlayerObject;

/** Team: Sigurd
* Student Numbers:
* 16751195, 16202907, 16375246
*/

public class PlayerCard extends Card {

	private PlayerObject myPlayerObject;
	
	PlayerCard(String _name, PlayerObject player) {
		super(_name);
		myPlayerObject = player;
	}
	
	public PlayerObject getPlayerObject() {
		return myPlayerObject;
	}
}
