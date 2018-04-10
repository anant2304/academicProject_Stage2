package Sigurd.Questions;

import Sigurd.Game;
import Sigurd.Player;
/**
 * Assertion or accusation that are made to win the game
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Peter Major
 */
public class Assertion extends AbstractQuestion {

	public Assertion(Player p) {
		super(p);
	}
	
	@Override
	protected void DoneWithInput() {
		if(Game.CompareToEnvelope(character, weapon, room) == true) {
            Game.GetDisplay().SendMessage("Correct Guess, you win the game");
			Game.EndGame();
		}
		else {
			asker.KnockOutOfGame();
			Game.GetDisplay().SendMessage("Incorecct Guess, you are out of the game\n"
					+ "Please enter done to finish your turn");
            Game.GetDisplay().log(asker + " made an incorrect accusation\n"
                    + asker + " thought it was " + character.getName() 
                    + " in the " + room.getName() + " with the " + weapon.getName());
		}
        Deactivate();
	}

	public void Activate() {
		super.Activate();
	}
}
