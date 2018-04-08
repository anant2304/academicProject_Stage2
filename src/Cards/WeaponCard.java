package Cards;

import Sigurd.BoardObjects.WeaponObject;
/**
 * Cards for weapons
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Peter Major
 */
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
