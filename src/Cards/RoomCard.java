package Cards;

import Sigurd.Room;

/** Team: Sigurd
* Student Numbers:
* 16751195, 16202907, 16375246
*/

public class RoomCard extends Card {

	private Room myRoom;
	
	RoomCard(String _name, Room room) {
		super(_name);
		myRoom = room;
	}
	
	public Room getRoom() {
		return myRoom;
	}

}
