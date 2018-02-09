package Sigurd.BoardObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


import Sigurd.Board;
import Sigurd.Controler;

public class PlayerObject extends BoardObject {

	/**
	 * @Summary represents the player on the board with a colored circle 
	 * @param _x
	 * @param _y
	 * @param c
	 * @param _name
	 */
    public PlayerObject(int _x, int _y, Color c, String _name) {
        super(_x,_y,(Image)null,_name);
        
        BufferedImage ima = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = (Graphics2D) ima.getGraphics();
        g2d.setColor(c);
        g2d.fillOval(0, 0, 23, 23);
        
        image = ima;
    }
    
    /**
     * moves the currently controlled object in the given direction
     */
    @Override
    public void Move(Controler.moveDirection d) {
    	int tempX = x;
    	int tempY = y;
    	
    	switch(d) {
	    	case up   : tempY--; break;
	    	case down : tempY++; break;
	    	case left : tempX--; break;
	    	case right: tempX++; break;
	    	}
    	
    	if(Board.GetBoard().IsPositionMovable(tempX, tempY)) {
    		x = tempX; 
    		y = tempY;
    	
	    	Board.GetBoard().GetBoardPanel().repaint();
	    	System.out.println(name + " moved in direction : " + d);
    	}
    	else {
    		System.out.println("Space is not walkable");
    	}
    }

}
