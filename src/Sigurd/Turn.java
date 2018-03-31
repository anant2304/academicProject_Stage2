package Sigurd;

import java.util.*;

import Cards.*;
import Sigurd.BoardObjects.*;

/**
 * Applies the actions a player does during their turn.
 * 
 * @author Adrian Wennberg
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class Turn {
    private Player turnPlayer;
    private PlayerObject turnPlayerObject;
    private static final Set<String> MOVE_DIRECTIONS = new HashSet<String>(
            Arrays.asList(new String[] { "u", "d", "l", "r" })
            );
    private int dice1, dice2;
    private boolean canRoll;
    private int stepsLeft;
    private boolean hasEneteredRoom;

    public Turn(Player player) {
        dice1 = dice2 = 0;
        stepsLeft = 0;
        turnPlayer = player;
        turnPlayerObject = turnPlayer.GetPlayerObject();
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
        if (turnPlayerObject.IsInRoom() && command.length() == 1 && Character.isDigit(command.toCharArray()[0]))
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
            case "notes":
                ShowNotes();
                break;
            case "cheat":
                DisplayMessage("You're not allowed to cheat.\n" +
                               "Type #cheat if you want to ruin all the fun.");
                break;
            default:
                DisplayError(command + " is not a valid command.");
                break;
            }
        }
    }

    private void MoveInDirection(String dir) {
        if (canRoll) {
            DisplayError(Language.English[25]);
            return;
        }
        if (hasEneteredRoom) {
            DisplayError(Language.English[26]);
            return;
        }
        if (stepsLeft == 0) {
            DisplayError(Language.English[27]);
            return;
        }
        if(turnPlayerObject.IsInRoom())
        {
            DisplayError(Language.English[28]);
            return;
        }        Coordinates positionChange;
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
            throw new IllegalArgumentException(Language.English[29]);
        }
        
        
        Coordinates movingToCo = turnPlayerObject.GetCoordinates().add(positionChange);

        if (Game.GetBoard().IsPositionMovable(turnPlayerObject.GetCoordinates(), movingToCo)) {
            if (Game.GetBoard().IsDoor(movingToCo)) {
                // Moves player into room, and adds the player to the room
                // object.
                turnPlayerObject.MoveToRoom(Game.GetBoard().GetDoorRoom(movingToCo));
                hasEneteredRoom = true;
                DisplayMessage(turnPlayer + " entered the " + turnPlayerObject.GetRoom().GetName());
            } else {
                // Moves a player along the board grid.
                turnPlayerObject.Move(positionChange);
                DisplayMessage(turnPlayer + " moved in direction: " + dir);
                stepsLeft--;
                DisplayMessage(turnPlayer + " has " + stepsLeft + " steps left to move.");
            }
        } else {
            DisplayError(turnPlayer + " cannot move in direction " + dir);
        }
    }

    private void MoveThroughPassage() {
        
        if (turnPlayerObject.IsInRoom() == false) {
            DisplayError(Language.English[30]);
        } else if (turnPlayerObject.GetRoom().HasPassage() == false) {
            DisplayError(Language.English[31]);
        } else if(hasEneteredRoom){
            DisplayError(Language.English[32]);
        }else{
            turnPlayerObject.MoveToRoom(turnPlayerObject.GetRoom().GetPassageRoom());
            Game.GetBoard().ResetRoom();
            DisplayMessage(turnPlayer + " took a secret passage to the " + turnPlayerObject.GetRoom().GetName());
            hasEneteredRoom = true;
            canRoll = false;
            dice1 = 1;
        }
    }

    private void MoveOutOfRoom(int exit) {
        PlayerObject playerObject = turnPlayer.GetPlayerObject();
        
        if (hasEneteredRoom)
            DisplayError(Language.English[33]);
        else if (dice1 == 0)
            DisplayError(Language.English[34]);

        else if (exit < 1 || playerObject.GetRoom().GetDoors().length < exit)
            DisplayError(Language.English[35]);

        else {
            Room playerRoom = playerObject.GetRoom();
            playerObject.LeaveRoom();
            Game.GetBoard().ResetRoom();
            turnPlayerObject.MoveTo(playerRoom.GetDoors()[exit - 1].GetOutside());
            stepsLeft--;
            DisplayMessage(turnPlayer + " left the " + playerRoom.GetName() + " through exit number " + exit);
            DisplayMessage(turnPlayer + " has " + stepsLeft + " steps left to move.");
        }
    }

    private void RollDice() {
        if (canRoll == false && dice1 == 0) {
            DisplayError(Language.English[36]);
            return;
        }
        if (canRoll == false) {
            DisplayError(Language.English[37]);
            return;
        }

        Random rand = new Random();
        dice1 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        dice2 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
                                            // numbers for the die results
        Game.GetDisplay().SendMessage("Die 1 gives: " + dice1 + "\n" + "Die 2 gives: " + dice2 + "\n" + turnPlayer +" gets "
                + (dice1 + dice2) + " moves" + "\n");
        stepsLeft = dice1 + dice2;
        
        canRoll = false;
    }
    
    private void ShowNotes()
    {
        StringBuilder sb = new StringBuilder(
                  String.format("%-30s \n", "Notes: " + turnPlayer));
        sb.append(String.format("%-30s \n", "X: You have this card."));
        sb.append(String.format("%-30s \n\n", "A: Everyone sees this card."));
        
        sb.append(String.format("%-30s \n", "Players"));
        sb.append(GetCardNotesFromIterator(Game.GetCards(PlayerCard.class)));
        
        sb.append(String.format("%-30s\n", "Weapons"));
        sb.append(GetCardNotesFromIterator(Game.GetCards(WeaponCard.class)));
        
        sb.append(String.format("%-30s\n", "Rooms"));
        sb.append(GetCardNotesFromIterator(Game.GetCards(RoomCard.class)));
        
        DisplayMessage(sb.toString());
    }
    
    private String GetCardNotesFromIterator(Iterator<? extends Card> cards)
    {
        StringBuilder sb = new StringBuilder();
        while(cards.hasNext())
        {
            Card c = cards.next();
            char displayChar;
            if(turnPlayer.HasCard(c)) // Check if player has card
                displayChar = c.CanEveryOneSee()?'A':'X';
            else
                displayChar = ' ';
            
            sb.append(String.format("%-30s%c\n", c.getName(), displayChar));
        }
        return sb.toString();
    }

    private void EndTurn() {
        Game.GetDisplay().SendMessage(Language.English[38]);
        Game.NextTurn();
    }
    
    
    public Player GetPlayer()
    {
        return turnPlayer;
    }
    
    public boolean CanLeaveRoom()
    {
        return (hasEneteredRoom == false && turnPlayerObject.IsInRoom());
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
