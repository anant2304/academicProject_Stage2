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
	private String name;
    private int x;
    private int y;
    private Image image;
    
    public BoardObject(int _x, int _y, Image _image, String _name)
    {
    	name = _name;
        x = _x;
        y = _y;
        image = _image;
        Board board = Board.GetBoard();
        board.AddMovable(this);
    }
    public BoardObject(int _x, int _y, String imagePath, String _name)
    {
        this(_x,_y,(Image)null,_name);
        BufferedImage ima = null;
        try{
        ima = ImageIO.read(new File(imagePath));
        }
        catch(IOException e) {}
        image = ima;
        Board.GetBoard().GetBoardPanel().repaint();
    }
    public BoardObject(int _x, int _y, Character testChar, String _name) {
    	this(_x,_y,(Image)null,_name);
         BufferedImage ima = new BufferedImage(22,22,BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2d = ima.createGraphics();
         g2d.setFont(new Font("Arial", 0,23));
         g2d.drawString(testChar.toString(), 3, 20);
         image = ima;
         Board.GetBoard().GetBoardPanel().repaint();
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
