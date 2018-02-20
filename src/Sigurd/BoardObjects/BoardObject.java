package Sigurd.BoardObjects;

import java.awt.Image;

import Sigurd.Controler;
import Sigurd.Coordinates;

/**
 * Abstract superclass for objects that will be displayed on the board.
 * @author Adrian Wennberg, Peter Major
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public abstract class BoardObject {
	private String name;
	private Coordinates obectCoordanates;
	private Image image;
    
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
    	obectCoordanates = co;
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
        return obectCoordanates;
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
    public void MoveTo(Coordinates co){
    	obectCoordanates = co;
    }
}
