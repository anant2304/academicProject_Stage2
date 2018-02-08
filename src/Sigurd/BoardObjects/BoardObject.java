package Sigurd.BoardObjects;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Sigurd.Board;
import Sigurd.Controler;


public abstract class BoardObject {
	protected String name;
	protected int x;
    protected int y;
    protected Image image;
    
    protected BoardObject(int _x, int _y, Image _image, String _name)
    {
    	name = _name;
        x = _x;
        y = _y;
        image = _image;
        Board board = Board.GetBoard();
        board.AddMovable(this);
    }

    public int GetX()
    {
        return x;
    }
    
    public int GetY()
    {
        return y;
    }

    public Image GetImage() {
        return image;
    }
    
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
    	Board.GetBoard().GetBoardPanel().repaint();
    	System.out.println(name + " moved in direction : " + d);
    }
}
