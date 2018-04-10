package Cards;

import Sigurd.Room;

/**
 * Cards for rooms
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Peter Major
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
