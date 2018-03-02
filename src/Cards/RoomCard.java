package Cards;

import Sigurd.Room;

public class RoomCard extends Card {

	private Room myRoom;
	
	public RoomCard(String _name, Room room) {
		super(_name);
		myRoom = room;
	}
	
	public Room getRoom() {
		return myRoom;
	}

}
