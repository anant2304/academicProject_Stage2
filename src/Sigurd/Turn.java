package Sigurd;

import java.util.*;

import Sigurd.BoardObjects.*;

/**
 * Applies the actions a player does during their turn.
 * 
 * @author Adrian Wennberg
 */
public class Turn {
    private PlayerObject turnPlayer;
    private static final String[] MOVE_DIRECTION_VALUES = new String[] { "u", "d", "l", "r" };
    private static final Set<String> MOVE_DIRECTIONS = new HashSet<String>(Arrays.asList(MOVE_DIRECTION_VALUES));
    private int d1,d2;
    public Turn(PlayerObject player) 
    {
        d1=d2=0; 
        turnPlayer = player;
    }

    /**
     * Do an action based on a sting input.
     * 
     * @param input
     */
    public void TurnAction(String input) {

        // Check if the input was a number in case
        // the player is trying to leave a room.
        if (input.length() == 1 && Character.isDigit(input.toCharArray()[0]))
            MoveOutOfRoom(Integer.parseInt(input));

        // Check if the input is a movable direction.
        else if (MOVE_DIRECTIONS.contains(input))
            MoveInDirection(input);

        else {

            switch (input) {
            case "quit": // Quit the game
                // TODO: Add quit game method.
                break;
            case "done": // Done with the round.
                // TODO Should we allow done as initial input? Without
                // moving or rolling?
                EndTurn();
                break;
            case "roll":
                RollDice();
                break;
            case "passage":
                MoveThroughPassage();
                break;
            default:
                DisplayError(input + " is not a valid command.");
                break;
            }

        }
    }

    private void MoveThroughPassage() {
        // TODO If player not in room, error.
        // TODO Auto-generated method stub

    }
    
    private void MoveInDirection(String dir) {
        Coordinates positionChange;
        switch (dir) {
        case "u":
            positionChange = new Coordinates(0, -1);
            break;
        case "d":
            positionChange = new Coordinates(0, 1);
            break;
        case "l":
            positionChange = new Coordinates(-1, 0);
            break;
        case "r":
            positionChange = new Coordinates(1, 0);
            break;
        default:
            throw new IllegalArgumentException("Move dir must be a string in the set {u, d, l, r}.");
        }
        
        if (Game.GetBoard().IsPositionMovable(turnPlayer.GetCoordinates().add(positionChange))) {
            turnPlayer.Move(positionChange);
            DisplayMessage(turnPlayer.GetName() + " moved in direction: " + dir);
        }
        else
        {
            DisplayError(turnPlayer.GetName() + " cannot move in direction " + dir);
        }
    }

    private void MoveOutOfRoom(int exit) {
        // TODO If player not in room, error.
        // TODO Auto-generated method stub

    }

    private void RollDice() 
    {
        Random rand = new Random();
    	d1=rand.nextInt((6 - 1) + 1) + 1; //creating randomly generated numbers for the die results
    	d2=rand.nextInt((6 - 1) + 1) + 1; //creating randomly generated numbers for the die results
    	Game.GetDisplay().sendMessage("Die 1 gives: "+d1+"\n"+"Die 2 gives: "+d2+"\n"+"Player gets "+(d1+d2)+" moves"+"\n");
    }

    /**
     * @Summary finishes this turn and starts the next one
     */
    private void EndTurn() {
    	Game.NextTurn();
    }

    /**
     * Displays a message to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayMessage(String string) {
        Game.GetDisplay().sendMessage(string);

    }

    /**
     * Displays an error to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayError(String string) {
        Game.GetDisplay().SendError(string);

    }

}
