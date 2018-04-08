package Cards;

import Sigurd.BoardObjects.WeaponObject;

/** Team: Sigurd
* Student Numbers:
* 16751195, 16202907, 16375246
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
