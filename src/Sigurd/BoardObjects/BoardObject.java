package Sigurd.BoardObjects;

import java.awt.Image;

import Sigurd.Controler;

/**
 * Abstract superclass for objects that will be displayed on the board.
 * @author Adrian Wennberg, Peter Major
 * Team: Sigurd
 */
public abstract class BoardObject {
	private String name;
	private int x;
	private int y;
	private Image image;
    
	/**
	 * 
	 * @param x        Start X position on the board grid.
	 * @param y        Start Y position on the board grid.
	 * @param image    Image to display on the board.
	 * @param name     Name that the board object is identified by.
	 */
    protected BoardObject(int x, int y, Image image, String name)
    {
    	this.name = name;
        this.x = x;
        this.y = y;
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
    public int GetX()
    {
        return x;
    }
    
    /**
     * @return The current Y position on the grid.
     */
    public int GetY()
    {
        return y;
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
    public String GetName() {
    	return name;
    }
    
    /**
     * @Summay moves the object in the specified direction, 0=up,1=right,2=down,3=left
     */
    public void Move(Controler.moveDirection d){
    	switch(d) {
    	case up :
    		y--;
    		break;
    	case down:
    		y++;
    		break;
    	case left:
    		x--;
    		break;
    	case right:
    		x++;
    		break;
    	}
    }
}
