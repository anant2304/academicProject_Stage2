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
    private int d1, d2;
    private int stepsLeft;
    private boolean hasEneteredRoom;

    public Turn(PlayerObject player) {
        d1 = d2 = 0;
        stepsLeft = 0;
        turnPlayer = player;
        hasEneteredRoom = false;
    }

    /**
     * Do an action based on a sting input.
     * 
     * @param input
     */
    public void TurnAction(String input) {

        // If the player is in a room, check if the input was a number.
        if (turnPlayer.IsInRoom() && input.length() == 1 && Character.isDigit(input.toCharArray()[0]))
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

            // TODO Add a help command and add info about help command in
            // default response.
        }
    }

    private void MoveInDirection(String dir) {
        if (d1 == 0) {
            DisplayError("You need to roll the dice befor you can move.");
            return;
        }
        if (hasEneteredRoom) {
            DisplayError("You cannot move after you have enetered a room.");
            return;
        }
        if (stepsLeft == 0) {
            DisplayError("You do not have any steps left to move.");
            return;
        }
        if(turnPlayer.IsInRoom())
        {
            DisplayError("You have to move out of the room first.");
            return;
        }
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

        if (Board.GetBoard().IsPositionMovable(turnPlayer.GetCoordinates(), movingToCo)) {
            if (Board.GetBoard().IsDoor(movingToCo)) {
                // Moves player into room, and adds the player to the room
                // object.
                turnPlayer.MoveToRoom(Board.GetBoard().GetDoorRoom(movingToCo));
            } else {
                // Moves a player along the board grid.
                turnPlayer.Move(positionChange);
                DisplayMessage(turnPlayer.GetName() + " moved in direction: " + dir);
                stepsLeft--;
                DisplayMessage(turnPlayer.GetName() + " has " + stepsLeft + " steps left to move.");
            }
        } else {
            DisplayError(turnPlayer.GetName() + " cannot move in direction " + dir);
        }
    }

    private void MoveThroughPassage() {
        if (turnPlayer.IsInRoom() == false) {
            DisplayError("You cannot take a passage while not in a room");
        } else if (turnPlayer.GetRoom().HasPassage() == false) {
            DisplayError("The current room hase no passages");
        } else if(hasEneteredRoom)
        {
            DisplayError("You cannot enter a passage if you enetered a room this turn");
        }else{ // TODO Decide if we want to take passage after roll.
            turnPlayer.MoveToRoom(turnPlayer.GetRoom().GetPassageRoom());
            DisplayMessage(turnPlayer.GetName() + " took a secret passage to the " + turnPlayer.GetRoom().GetName());
            hasEneteredRoom = true;
        }
    }

    private void MoveOutOfRoom(int exit) {
        if (hasEneteredRoom)
            DisplayError("You have allready entered a room on this turn. Type ''");
        // TODO Add help info in error message
        else if (d1 == 0)
            DisplayError("You need to roll the dice befor you can move.");

        else if (turnPlayer.GetRoom().GetDoors().length <= exit)
            DisplayError("Please enter a valid door number or type '' ");
        // TODO Add help info in error message

        else {
            turnPlayer.LeaveRoom();
            turnPlayer.MoveTo(turnPlayer.GetRoom().GetDoors()[exit]);
            stepsLeft--;
        }
    }

    private void RollDice() {
        if (d1 != 0)
            DisplayError("You have allready rolled your dice");

        Random rand = new Random();
        d1 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        d2 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        Game.GetDisplay().sendMessage("Die 1 gives: " + d1 + "\n" + "Die 2 gives: " + d2 + "\n" + "Player gets "
                + (d1 + d2) + " moves" + "\n");
        stepsLeft = d1 + d2;
    }

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