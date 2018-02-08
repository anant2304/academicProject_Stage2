package Sigurd.BoardObjects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Sigurd.Board;
import Sigurd.Controler;

public class PlayerObject extends BoardObject {

    public PlayerObject(int _x, int _y, String imagePath, String _name)
    {
        super(_x,_y,(Image)null,_name);
        BufferedImage ima = null;
       
        try{
        ima = ImageIO.read(new File(imagePath));
        }
        catch(IOException e) {}
        
        image = ima;
    }
    
    @Override
    public void Move(Controler.moveDirection d) {
    	int tempX = x;
    	int tempY = y;
    	switch(d) {
    	case up :
    		tempY--;
    		break;
    	case down:
    		tempY++;
    		break;
    	case left:
    		tempX--;
    		break;
    	case right:
    		tempX++;
    		break;
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
