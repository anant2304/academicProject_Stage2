package Sigurd.BoardObjects;

import java.awt.Image;

public class WeaponObject extends BoardObject {

	public WeaponObject(int _x, int _y, Character testChar, String _name) {
		super(_x, _y, testChar, _name);
	}
	public WeaponObject(int _x, int _y, Image _image, String _name) {
		super( _x,  _y,  _image, _name);
    }
    public WeaponObject (int _x, int _y, String imagePath, String _name) {
        super( _x,  _y,  imagePath, _name);
    }
	
}
