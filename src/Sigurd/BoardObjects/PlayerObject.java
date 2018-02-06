package Sigurd.BoardObjects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerObject extends BoardObject {

	public PlayerObject(int _x, int _y, Character testChar, String _name) {
		super(_x, _y, testChar, _name);
	}
	public PlayerObject(int _x, int _y, Image _image, String _name) {
		super( _x,  _y,  _image, _name);
    }
    public PlayerObject (int _x, int _y, String imagePath, String _name) {
        super( _x,  _y,  imagePath, _name);
    }
    

}
