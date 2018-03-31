package Sigurd.BoardObjects;

import java.awt.Image;
import Sigurd.Coordinates;
import Sigurd.Room;

/**
 * Abstract superclass for objects that will be displayed on the board.
 * @author Adrian Wennberg, Peter Major
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public abstract class BoardObject {
	private String name;
	private Coordinates objectCoordanates;
	private Image image;
	private Room room;
    
	/**
	 * 
	 * @param x        Start X position on the board grid.
	 * @param y        Start Y position on the board grid.
	 * @param image    Image to display on the board.
	 * @param name     Name that the board object is identified by.
	 */
    protected BoardObject(Coordinates co, Image image, String name)
    {
    	this.name = name;
    	objectCoordanates = co;
        this.image = image;
    }
    
    /**
     * Sets the Image to be displayed.
     * @param image
     */
    protected void SetImage(Image image)
    {
        this.image = image;
    }

    /**
     * @return The current X position on the grid.
     */
    public Coordinates GetCoordinates() 
    {
        return objectCoordanates;
    }

    /**
     * @return The Image that will be displayed on the board.
     */
    public Image GetImage() {
        return image;
    }
    
    /**
     * @return The name of this object.
     */
    public String GetObjectName() {
    	return name;
    }
    
    public void MoveTo(Coordinates co){
        objectCoordanates = co;
    }

    public void MoveToRoom(Room r) {
        if(room != null)
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
