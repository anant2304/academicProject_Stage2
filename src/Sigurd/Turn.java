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
            Arrays.asList(new String[] { "u", "d", "l", "r" })
            );
    private int dice1, dice2;
    private boolean canRoll;
    private int stepsLeft;
    private boolean hasEneteredRoom;

    public Turn(PlayerObject player) {
        dice1 = dice2 = 0;
        stepsLeft = 0;
        turnPlayer = player;
        hasEneteredRoom = false;
        canRoll = true;
    }
    
    

    /**
     * Do an action based on a sting input.
     * 
     * @param command
     */
    public void Commands(String command) {

        // If the player is in a room, check if the input was a number.
        if (turnPlayer.IsInRoom() && command.length() == 1 && Character.isDigit(command.toCharArray()[0]))
            MoveOutOfRoom(Integer.parseInt(command));

        // Check if the input is a movable direction.
        else if (MOVE_DIRECTIONS.contains(command))
            MoveInDirection(command);

        else {

            switch (command) {
            case "quit": //Quit the game
                System.exit(0);
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
                DisplayError(command + " is not a valid command.");
                break;
            }
        }
    }

    private void MoveInDirection(String dir) {
        if (canRoll) {
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
            positionChange = Coordinates.UP;
            break;
        case "d":
            positionChange = Coordinates.DOWN;
            break;
        case "l":
            positionChange = Coordinates.LEFT;
            break;
        case "r":
            positionChange = Coordinates.RIGHT;
            break;
        default:
            throw new IllegalArgumentException("Move dir must be a string in the set {u, d, l, r}.");
        }

        Coordinates movingToCo = turnPlayer.GetCoordinates().add(positionChange);

        if (Game.GetBoard().IsPositionMovable(turnPlayer.GetCoordinates(), movingToCo)) {
            if (Game.GetBoard().IsDoor(movingToCo)) {
                // Moves player into room, and adds the player to the room
                // object.
                turnPlayer.MoveToRoom(Game.GetBoard().GetDoorRoom(movingToCo));
                hasEneteredRoom = true;
                DisplayMessage(turnPlayer.GetObjectName() + " entered the " + turnPlayer.GetRoom().GetName());
            } else {
                // Moves a player along the board grid.
                turnPlayer.Move(positionChange);
                DisplayMessage(turnPlayer.GetObjectName() + " moved in direction: " + dir);
                stepsLeft--;
                DisplayMessage(turnPlayer.GetObjectName() + " has " + stepsLeft + " steps left to move.");
            }
        } else {
            DisplayError(turnPlayer.GetObjectName() + " cannot move in direction " + dir);
        }
    }

    private void MoveThroughPassage() {
        if (turnPlayer.IsInRoom() == false) {
            DisplayError("You cannot take a passage while not in a room");
        } else if (turnPlayer.GetRoom().HasPassage() == false) {
            DisplayError("The current room hase no passages");
        } else if(hasEneteredRoom){
            DisplayError("You cannot enter a passage if you enetered a room this turn");
        }else{
            turnPlayer.MoveToRoom(turnPlayer.GetRoom().GetPassageRoom());
            DisplayMessage(turnPlayer.GetObjectName() + " took a secret passage to the " + turnPlayer.GetRoom().GetName());
            Game.GetBoard().ResetRoom();
            hasEneteredRoom = true;
            canRoll = false;
            dice1 = 1;
        }
    }

    private void MoveOutOfRoom(int exit) {
        if (hasEneteredRoom)
            DisplayError("You have allready entered a room on this turn. Type ''");
        // TODO Add help info in error message
        else if (dice1 == 0)
            DisplayError("You need to roll the dice befor you can move.");

        else if (exit < 1 || turnPlayer.GetRoom().GetDoors().length < exit)
            DisplayError("Please enter a valid door number or type '' ");
        // TODO Add help info in error message

        else {
            Room playerRoom = turnPlayer.GetRoom();
            turnPlayer.LeaveRoom();
            Game.GetBoard().ResetRoom();
            turnPlayer.MoveTo(playerRoom.GetDoors()[exit - 1].GetOutside());
            stepsLeft--;
            DisplayMessage(turnPlayer.GetObjectName() + " left the " + playerRoom.GetName() + " through exit number " + exit);
            DisplayMessage(turnPlayer.GetObjectName() + " has " + stepsLeft + " steps left to move.");
        }
    }

    private void RollDice() {
        if (canRoll == false && dice1 == 0) {
            DisplayError("You have allready rolled your dice");
            return;
        }
        if (canRoll == false) {
            DisplayError("You can not roll your dice at this time");
            return;
        }

        Random rand = new Random();
        dice1 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        dice2 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        Game.GetDisplay().SendMessage("Die 1 gives: " + dice1 + "\n" + "Die 2 gives: " + dice2 + "\n" + turnPlayer.GetObjectName() +" gets "
                + (dice1 + dice2) + " moves" + "\n");
        stepsLeft = dice1 + dice2;
        
        canRoll = false;
    }

    private void EndTurn() {
        Game.GetDisplay().SendMessage("Turn Over");
        Game.NextTurn();
    }
    
    
    public PlayerObject GetPlayer()
    {
        return turnPlayer;
    }
    
    public boolean CanLeaveRoom()
    {
        return (hasEneteredRoom == false && turnPlayer.IsInRoom());
    }
    
    /**
     * Displays a message to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayMessage(String string) {
        Game.GetDisplay().SendMessage(string);
    }

    /**
     * Displays an error to the display panel. Should potentially be moved to
     * the Game class.
     */
    private void DisplayError(String string) {
        Game.GetDisplay().SendError(string);
    }
    
    /**
     * @Summary sets the number of remaing steps in this turn to 100, to be used for debuing
     * @param quantaty
     */
    public void SetStepsLeft(int quantaty) {
    	dice1 = 1;
    	stepsLeft = quantaty;
    }
}