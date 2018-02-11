package Sigurd.BoardObjects;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


/**
 * Weapons that are displayed to the screen.
 * @author PeterMajor
 * Team: Sigurd
 */
public class WeaponObject extends BoardObject {

	/**
	 * @Summary Creates a weapon object represented the weapon on the map by a letter
	 * @param _x
	 * @param _y
	 * @param testChar
	 * @param _name
	 */
    public WeaponObject(int _x, int _y, Character Char, String _name) {
    	super(_x,_y,(Image)null,_name);
        
    	// Creates the image that will be displayed for this object.
    	BufferedImage image = new BufferedImage(22,22,BufferedImage.TYPE_INT_ARGB);
         
    	// Draws A single character onto the image.
    	Graphics2D g2d = image.createGraphics();
        g2d.setFont(new Font("Arial", 0,20));
        g2d.drawString(Char.toString(), 3, 20);
        
        SetImage(image);
    }
	
}
