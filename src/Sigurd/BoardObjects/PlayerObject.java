package Sigurd.BoardObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


import Sigurd.Board;
import Sigurd.Controler;
import Sigurd.Game;

/**
 * Players that are displayed to the screen.
 * @author Peter Major
 * Team: Sigurd
 */
public class PlayerObject extends BoardObject {

	/**
	 * @Summary Creates a player represented on the board with a colored circle 
	 * @param _x
	 * @param _y
	 * @param c
	 * @param _name
	 */
    public PlayerObject(int _x, int _y, Color c, String _name) {
        super(_x,_y,(Image)null,_name);
        
        BufferedImage image = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(c);
        g2d.fillOval(0, 0, 23, 23);
        
        SetImage(image);
    }
    
    /**
     * moves the currently controlled player in the given direction
     */
    @Override
    public void Move(Controler.moveDirection d) {
    	int tempX = GetX();
    	int tempY = GetY();
    	
    	switch(d) {
	    	case up   : tempY--; break;
	    	case down : tempY++; break;
	    	case left : tempX--; break;
	    	case right: tempX++; break;
	    	}
    	
    	if(Board.GetBoard().IsPositionMovable(tempX, tempY)) {
    		super.Move(d);
    	}
    	else {
    		Game.display.sendMessage("Space is not walkable");
    	}
    }
}
