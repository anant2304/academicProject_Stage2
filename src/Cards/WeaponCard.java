package Cards;

import Sigurd.BoardObjects.WeaponObject;

public class WeaponCard extends Card {

	public WeaponObject myWeaponObject;
	
	WeaponCard(String _name, WeaponObject weapon) {
		super(_name);
		myWeaponObject = weapon;
	}
	
	public WeaponObject getWeapon() {
		return myWeaponObject;
	}

}
