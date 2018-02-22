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
    private static final Set<String> MOVE_DIRECTIONS = new HashSet<String>(
            Arrays.asList(new String[] { "u", "d", "l", "r" }));

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
            MoveOutOfRoom(Integer.parseInt(input));

        // Check if the input is a movable direction.
        else if (MOVE_DIRECTIONS.contains(input))
            MoveInDirection(input);

        else {

            switch (input) {
            case "quit": // Quit the game
                // TODO: Add quit game method.
                break;
            case "done":
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
        // TODO if player in room error.
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

        Coordinates movingToCo = turnPlayer.GetCoordinates().add(positionChange);

        if (Board.GetBoard().IsPositionMovable(movingToCo)) {
            if (Board.GetBoard().IsDoor(movingToCo)) {
                // Moves player into room, and adds the player to the room object.
                turnPlayer.MoveToRoom(Board.GetBoard().GetDoorRoom(movingToCo));
                // TODO Stop players form moving more steps.
            } else {
                // Moves a player along the board grid.
                turnPlayer.Move(positionChange);
                DisplayMessage(turnPlayer.GetName() + " moved in direction: " + dir);
                // TODO decrement number of steps left.
            }
        } else {
            DisplayError(turnPlayer.GetName() + " cannot move in direction " + dir);
        }
    }

    private void MoveOutOfRoom(int exit) {
        if(turnPlayer.IsInRoom() == false)
            DisplayError(""); // TODO Add relevant error message.
        
        Coordinates[] roomDoors = turnPlayer.GetRoom().GetDoors();
        
        if(roomDoors.length <= exit)
            DisplayError(""); // TODO Add relevant error message.
        
        turnPlayer.LeaveRoom();
        turnPlayer.MoveTo(roomDoors[exit]);
    }

    private void RollDice() {
        // TODO Auto-generated method stub

    }

    private void EndTurn() {
        // TODO Auto-generated method stub

    }

    /**
     * Displays a message to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayMessage(String string) {
        // TODO Auto-generated method stub

    }

    /**
     * Displays an error to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayError(String string) {
        // TODO Auto-generated method stub

    }

}
