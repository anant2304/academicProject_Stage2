package Sigurd.BoardObjects;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import Sigurd.Board;

public class WeaponObject extends BoardObject {

    public WeaponObject(int _x, int _y, Character testChar, String _name) {
    	super(_x,_y,(Image)null,_name);
        
    	BufferedImage ima = new BufferedImage(22,22,BufferedImage.TYPE_INT_ARGB);
         
    	Graphics2D g2d = ima.createGraphics();
        g2d.setFont(new Font("Arial", 0,20));
        g2d.drawString(testChar.toString(), 3, 20);
        
        image = ima;
        Board.GetBoard().GetBoardPanel().repaint();
    }
	
}
