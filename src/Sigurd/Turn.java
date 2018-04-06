package Sigurd;

import java.util.*;

import Cards.*;
import Sigurd.BoardObjects.*;
import Sigurd.Questions.Assertion;
import Sigurd.Questions.Question;

/**
 * Applies the actions a player does during their turn.
 * 
 * @author Adrian Wennberg Team: Sigurd Student Numbers: 16751195, 16202907,
 *         16375246
 */
public class Turn {
    private Player turnPlayer;
    private PlayerObject turnPlayerObject;
    private static final Set<String> MOVE_DIRECTIONS = new HashSet<String>(
                                                                           Arrays.asList(new String[] { "u", "d", "l", "r" }));
    private int dice1, dice2;
    private boolean canRoll;
    private int stepsLeft;
    private boolean hasEneteredRoom;
    private Question turnQuestion;
    private Assertion turnAssertion;
    
    public Turn(Player player, Iterable<Player> allPlayers) {
        dice1 = dice2 = 0;
        stepsLeft = 0;
        turnPlayer = player;
        turnPlayerObject = turnPlayer.GetPlayerObject();
        hasEneteredRoom = false;
        canRoll = true;
        turnQuestion = new Question(player, allPlayers);
        turnAssertion = new Assertion(player);
    }
    
    /**
     * Do an action based on a sting input.
     *
     * @param command
     */
    public void Commands(String command) {
        
        DisplayMessage(command);
                
        if(turnAssertion.IsActive())
        	turnAssertion.Commands(command);
        
        // If the player is asking a question, pass on the input
        else if (turnQuestion.IsActive())
            turnQuestion.Commands(command);
        
        // If the player is in a room, check if the input was a number.
        else if (turnPlayerObject.IsInRoom() && command.length() == 1 && Character.isDigit(command.toCharArray()[0]))
            MoveOutOfRoom(Integer.parseInt(command));
        
        // Check if the input is a movable direction.
        else if (MOVE_DIRECTIONS.contains(command))
            MoveInDirection(command);
        
        else {
            
            switch (command) {
                case "quit": // Quit the game
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
                case "question":
                    StartAskingQuestion();
                    break;
                case "notes":
                    ShowNotes();
                    break;
                case "cheat":
                    DisplayMessage("You're not allowed to cheat.\n" + "Type #cheat if you want to ruin all the fun.");
                    break;
                case "accuse":
                	if(turnAssertion.CanAsk() == true)
                		turnAssertion.Activate();
                	break;
                default:
                    DisplayError("That is not a valid command.");
                    break;
            }
        }
    }
    
    private void MoveInDirection(String dir) {
        if (canRoll) {
            DisplayError("You need to roll the dice before you can move.\n");
            return;
        }
        if (hasEneteredRoom) {
            DisplayError("You cannot move after you have entered a room.\n");
            return;
        }
        if (stepsLeft == 0) {
            DisplayError("You do not have any steps left to move.\n");
            return;
        }
        if (turnPlayerObject.IsInRoom()) {
            DisplayError("You have to move out of the room first.\n");
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
                throw new IllegalArgumentException("Move dir must be a string in the set {u, d, l, r}.\n");
        }
        
        Coordinates movingToCo = turnPlayerObject.GetCoordinates().add(positionChange);
        
        if (Game.GetBoard().IsPositionMovable(turnPlayerObject.GetCoordinates(), movingToCo)) {
            if (Game.GetBoard().IsDoor(movingToCo)) {
                // Moves player into room, and adds the player to the room
                // object.
                turnPlayerObject.MoveToRoom(Game.GetBoard().GetDoorRoom(movingToCo));
                hasEneteredRoom = true;
                DisplayMessage(turnPlayer + " entered the " + turnPlayerObject.GetRoom().GetName());
                turnQuestion.SetCanAsk();
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
    
    private void MoveOutOfRoom(int exit) {
        PlayerObject playerObject = turnPlayer.GetPlayerObject();
        
        if (hasEneteredRoom)
            DisplayError("You have already entered a room on this turn.\n");
        else if (dice1 == 0)
            DisplayError("You need to roll the dice before you can move.\n");
        
        else if (exit < 1 || playerObject.GetRoom().GetDoors().length < exit)
            DisplayError("Please enter a valid door number\n");
        
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
    
    private void MoveThroughPassage() {
        
        if (turnPlayerObject.IsInRoom() == false) {
            DisplayError("You cannot take a passage while not in a room\n");
        } else if (turnPlayerObject.GetRoom().HasPassage() == false) {
            DisplayError("The current room has no passages\n");
        } else if (hasEneteredRoom) {
            DisplayError("You cannot enter a passage if you entered a room this turn\n");
        } else {
            turnPlayerObject.MoveToRoom(turnPlayerObject.GetRoom().GetPassageRoom());
            Game.GetBoard().ResetRoom();
            DisplayMessage(turnPlayer + " took a secret passage to the " + turnPlayerObject.GetRoom().GetName());
            hasEneteredRoom = true;
            canRoll = false;
            dice1 = 1;
        }
    }
    
    private void StartAskingQuestion() {
        if(turnPlayerObject.IsInRoom() == false)
            DisplayError("You need to be in a room to ask a question.");
        else if (turnQuestion.HasBeenAsked())
            DisplayError("You have allready asked a question this turn");
        else if (turnQuestion.CanAsk() == false)
            DisplayError("You can only ask a question if you are in a room that you did not enter on your last turn");
        else if(turnQuestion.IsActive())
            DisplayError("You are currently asking a question.");
        else
            turnQuestion.StartAskingQuestion(turnPlayerObject.GetRoom());
    }
    
    private void RollDice() {
        if (canRoll == false && dice1 == 0) {
            DisplayError("You have already rolled your dice\n");
            return;
        }
        if (canRoll == false) {
            DisplayError("You cannot roll your dice at this time\n");
            return;
        }
        
        Random rand = new Random();
        dice1 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
        // numbers for the die results
        dice2 = rand.nextInt((6 - 1) + 1) + 1; // creating randomly generated
        // numbers for the die results
        Game.GetDisplay().SendMessage("Die 1 gives: " + dice1 + "\n" + "Die 2 gives: " + dice2 + "\n" + turnPlayer
                                      + " gets " + (dice1 + dice2) + " moves" + "\n");
        stepsLeft = dice1 + dice2;
        
        canRoll = false;
    }
    
    private void ShowNotes() {
        DisplayMessage(
                       turnPlayer.GetNotes(Game.GetCards(PlayerCard.class), Game.GetCards(WeaponCard.class), Game.GetCards(RoomCard.class)));
    }
    
    private void EndTurn() {
        Game.GetDisplay().SendMessage("Turn Over\n");
        Game.NextTurn();
    }
    
    public Player GetPlayer() {
        return turnPlayer;
    }
    
    public boolean CanLeaveRoom() {
        return (hasEneteredRoom == false && turnPlayerObject.IsInRoom());
    }
    
    public boolean IsAskingQuestion()
    {
        // TODO look at this after question is finished
        return turnQuestion.IsActive() && !turnQuestion.HasBeenAsked();
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
     * @Summary sets the number of remaing steps in this turn to 100, to be used
     *          for debuing
     * @param quantaty
     */
    public void SetStepsLeft(int quantaty) {
        dice1 = 1;
        stepsLeft = quantaty;
    }
}
