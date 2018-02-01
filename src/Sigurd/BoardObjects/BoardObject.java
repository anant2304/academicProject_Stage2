package Sigurd.BoardObjects;

import java.awt.Image;


public abstract class BoardObject {
    private int x;
    private int y;
    private Image image;
    
    public BoardObject(int _x, int _y, Image _image)
    {
        x = _x;
        y = _y;
        image = _image;
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
}
