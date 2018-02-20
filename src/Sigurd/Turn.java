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

    public Turn(PlayerObject player) {
        turnPlayer = player;
    }

    /**
     * Do an action based on a sting input.
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
        // TODO Auto-generated method stub

    }

    private void MoveOutOfRoom(int exit) {
        // TODO If player not in room, error.
        // TODO Auto-generated method stub

    }

    private void RollDice() {
        // TODO Auto-generated method stub

    }

    private void EndTurn() {
        // TODO Auto-generated method stub

    }

    /**
     * Displays a message to the display panel.
     * Should potentially be moved to the Game class.
     */
    private void DisplayMessage(String string) {
        // TODO Auto-generated method stub

    }

    /**
     * Displays an error to the display panel.
     * Should potentially be moved to the Game class.
     */
    private void DisplayError(String string) {
        // TODO Auto-generated method stub

    }

}
