package Sigurd.Questions;

import Cards.Deck;
import Sigurd.Game;
import Sigurd.Player;

public class Assertion extends AbstractQuestion {

	public Assertion(Player p) {
		super(p);
	}
	
	@Override
	protected void DoneWithInput() {
		if(Game.CompareToEnvelope(character, weapon, room) == true) {
			Game.IsGameOver();
		}
		else {
			asker.KnockOutOfGame();
			Game.GetDisplay().SendMessage("Incorecct Guess, you are out of the game");
			Deactivate();
		}
	}

	public void Activate() {
		super.Activate();
	}
}
