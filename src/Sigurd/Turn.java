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
    private static final String[] SET_VALUES = new String[] { "u", "d", "l", "r" };
    private static final Set<String> moveDirections = new HashSet<String>(Arrays.asList(SET_VALUES));

    /**
     * Create a turn with a specified player.
     * 
     * @param The
     *            player who's turn it is.
     */
    public Turn(PlayerObject player) {
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
            LeaveRoom(Integer.parseInt(input));

        // Check if the input is a movable direction.
        else if (moveDirections.contains(input))
            Move(input);

        else {

            switch (input) {
            case "quit": // Quit the game
                // TODO: Add quit game method.
                break;
            case "done": // Done with the round.
                // TODO Should we allow done as initial input? Without
                // moving or rolling?
                TurnDone();
                break;
            case "roll":
                Roll();
                break;
            case "passage":
                TakePassage();
                break;
            default:
                DisplayError(input + " is not a valid command.");
                break;
            }

        }
    }

    /**
     * Player moves through a secret passage.
     */
    private void TakePassage() {
        // TODO If player not in room, error.
        // TODO Auto-generated method stub

    }

    /**
     * Moves the player in the specified direction.
     * 
     * @param dir
     *            direction to move;
     */
    private void Move(String dir) {
        // TODO Auto-generated method stub

    }

    /**
     * Player leaves the room they are currently in.
     * 
     * @param exit
     */
    private void LeaveRoom(int exit) {
        // TODO If player not in room, error.
        // TODO Auto-generated method stub

    }

    /**
     * Rolls the dice for the turn and displays the result.
     */
    private void Roll() {
        // TODO Auto-generated method stub

    }

    /**
     * Ends the current turn
     */
    private void TurnDone() {
        // TODO Auto-generated method stub

    }

    /**
     * Displays a message to the display panel.
     * 
     * @param stringc
     *            Message to display.
     */
    private void DisplayMessage(String string) {
        // TODO Auto-generated method stub

    }

    /**
     * Displays an error to the display panel.
     * 
     * @param string
     *            Error to display.
     */
    private void DisplayError(String string) {
        // TODO Auto-generated method stub

    }

}
