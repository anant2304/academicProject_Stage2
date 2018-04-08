package Sigurd.BoardObjects;

import java.awt.Image;
import Sigurd.Coordinates;
import Sigurd.Room;

/**
 * Abstract superclass for objects that will be displayed on the board.
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Adrian Wennberg, Peter Major
 */
public abstract class BoardObject {
	private String name;
	private Coordinates objectCoordanates;
	private Image image;
	private Room room;
    
    protected BoardObject(Coordinates co, Image image, String name)
    {
    	this.name = name;
    	objectCoordanates = co;
        this.image = image;
    }
    
    protected void SetImage(Image image)
    {
        this.image = image;
    }

    public Coordinates GetCoordinates() 
    {
        return objectCoordanates;
    }

    public Image GetImage() {
        return image;
    }
    
    public String GetObjectName() {
    	return name;
    }
    
    public void MoveTo(Coordinates co){
        objectCoordanates = co;
    }

    public void MoveToRoom(Room r) {
        if(room == r)
            return;
        if(IsInRoom())
            LeaveRoom();
        room = r;
        r.AddObject(this);
        MoveTo(r.GetObjectPosition(this));
    }
    
    public boolean IsInRoom()
    {
        return room != null;
    }
    
    public Room GetRoom()
    {
        if(IsInRoom() == false)
            throw new IllegalStateException(name + " not in a room.");
        return room;
    }
    
    public void LeaveRoom()
    {
        if(IsInRoom() == false)
            throw new IllegalStateException(name + " not in a room.");
        room.RemoveObject(this);
        room = null;
    }
}
