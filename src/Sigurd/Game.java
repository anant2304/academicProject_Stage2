package Sigurd;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.*;

import Sigurd.BoardObjects.*;

/**
 * Servers an an entry point and ties the different classes together.
 * @author Peter Major
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class Game {
	private static CommandPanel command;
	private static Board board;
	private static DisplayPanel display;
	
	private static Stack<Turn> turnStack = new Stack<Turn>();
	
	private static Map<String,PlayerObject> characterMap = new HashMap<String,PlayerObject>();
	private static Map<String,WeaponObject> weaponMap = new HashMap<String,WeaponObject>();
	
	private static PlayerSignIn playerSign;
	
	/**
	 * @Summary the main that runs the game
	 * @param args
	 */
	public static void main(String[] args) {
		command = new CommandPanel();
		display = new DisplayPanel();
		board = new Board();
		playerSign = new PlayerSignIn();
			
        CreateWindow();
		PlacePlayers();
		PlaceWeapons();
        
		command.TakeFocus();//would be in create window but some issue causes it to work only half the time, here it always works
	
		display.SendMessage("Enter the names of the players\nin the form [Player Name] [Character Name]");
		//the game now waits for input, first that input is passed to the PlayerSignIn class,
		//after the game has started it is then passed to each respective turn object as they are taken
	}
	
	/**
	 * private constructor
	 */
	private Game() {}
	
	/**
	 * @Summary creates the window that holds all the panels
	 */
    @SuppressWarnings("static-access")
	private static void CreateWindow() {
        JFrame window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
				
		window.add(command, BorderLayout.SOUTH);
		window.add(board.GetBoardPanel(), BorderLayout.CENTER);
		window.add(display, BorderLayout.EAST);
		
		window.pack();
		window.setResizable(false); //makes the frame non-resizable
		window.setVisible(true);
		
		//sets the currser to the command line when the game window is opened
		window.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				command.TakeFocus();
			}
		});
	}
	
    /**
     * @Summary called by the PlayerSignIn class to progress the game into a playable state
     */
    public static void StartGame() {		
		NextTurn();
    }
    
	/**
	 * @Summary creates and places all the players onto the board
	 */
	private static void PlacePlayers() {
		characterMap.put("white",new PlayerObject(new Coordinates(9, 0), Color.decode("#ffffff"), "White"));
		characterMap.put("green",new PlayerObject(new Coordinates(14, 0), Color.decode("#00ff00"), "Green"));
		characterMap.put("peacock",new PlayerObject(new Coordinates(23, 6), Color.decode("#326872"), "Peacock"));
		characterMap.put("plum",new PlayerObject(new Coordinates(23, 19), Color.decode("#8E4585"), "Plum"));
		characterMap.put("scarlet",new PlayerObject(new Coordinates(7, 24), Color.decode("#ff2400"), "Scarlet"));
		characterMap.put("mustard",new PlayerObject(new Coordinates(0, 17), Color.decode("#ffdb58"), "Mustard"));
		
        for(PlayerObject p : characterMap.values())
        {
            board.AddMovable(p);
        }
	}
	
	/**
	 * @Summary creates and places all weapons onto the board
	 */
	private static void PlaceWeapons() {
		weaponMap.put("rope",new WeaponObject(new Coordinates(3,4),new Character('R'),"Rope"));
		weaponMap.put("dagger",new WeaponObject(new Coordinates(4,12),new Character('D'),"Dagger"));
		weaponMap.put("wrench",new WeaponObject(new Coordinates(3,21),new Character('W'),"Wrench"));
		weaponMap.put("pistol",new WeaponObject(new Coordinates(10,3),new Character('P'),"Pistol"));
		weaponMap.put("candlestick",new WeaponObject(new Coordinates(10,21),new Character('C'),"CandleStick"));
		weaponMap.put("leadpipe",new WeaponObject(new Coordinates(20,22),new Character('L'),"LeadPipe"));
		
        for(WeaponObject p : weaponMap.values())
            board.AddMovable(p);
	}
	
	/**
	 * @Summary returns whether there has been a turn yet
	 */
    public static boolean isGameStarted()
    {
        return (!turnStack.isEmpty());
    }
    
    /**
	 * @Summary creates a new turn with the next player
	 */
	public static void NextTurn() {
		NewTurn(playerSign.NextPlayer());
	}
	
	/**
	 * @Summary ends the last turn and starts a new one
	 */
	public static void NewTurn(PlayerObject p) {
		Turn newTurn = turnStack.push(new Turn(p));
		if(newTurn.CanLeaveRoom())
		    board.SetRoom(p.GetRoom());
		board.GetBoardPanel().repaint();
		display.SendMessage(turnStack.peek().GetPlayer().getPlayerName() + " its your turn, you are " + turnStack.peek().GetPlayer().GetObjectName());
	}
	
	/**
	 * @Summary returns a reference to the current turn
	 */
	public static Turn CurrentTurn() {
		return turnStack.peek();
	}
	
	public static boolean DoseCharacterExist(String name) {
		return characterMap.containsKey(name);
	}
	
	public static PlayerObject GetCharacter(String name) {
		return characterMap.get(name);
	}
	
	/**
	 * @Summary returns the dispaly panel
	 * @return
	 */
	public static DisplayPanel GetDisplay() {
		return display;
	}
	
	/**
	 * @summary returns the board, witch you can get the board panel from
	 * @return
	 */
	public static Board GetBoard() {
		return board;
	}
	
	/**
	 * @Summary Takes a command and passes it to the correct command method in some class
	 * */
	public static void PassCommand(String com) {
		
		if(com.equals(""));//ignore empty strings
		else if(com.charAt(0) == '#') {
            Commands(com);
        }
        else if(com.equals("help")) {
            DisplayHelp();
        }
        else if(isGameStarted()==false){
            playerSign.Commands(com);
        }
        else {
            turnStack.peek().Commands(com);//commands in the turn class
            board.GetBoardPanel().repaint();
        }
	}
	
	/**
	 * @Summary exicutes developer commands for debuging purposes
	 */
	private static void Commands(String com) {
		switch(com) {
		case "#exit" :
			System.exit(0);
			break;
		case "#steps100" :
			turnStack.peek().SetStepsLeft(100);
			break;
		}
		
		display.SendMessage(com);
	}
	
	/**
	 * @Summary Dispalys a context sensitive help menu to the display pannel
	 */
	private static void DisplayHelp(){
		if(isGameStarted())
			display.SendMessage(
				"type in \"roll\" to roll your dice\n"
				+ "type in u,d,l or r to move up, down, left, or right respectivly\n"
				+ "if you are in a room at the start of your turn, after rolling type the number corrisponding to an exit to leave\n"
				+ "type in \"done\" to end your turn"
				+ "type in \"quit\" to close down the game\n"
				+ "type in \"#exit\" if something goes wrong"
				);
		else
			display.SendMessage(
					"type in a name then press enter or return to add it to the game\n"
					+ "type in \"players\" to see who is currently in the game"
					+ "if you have entered everyone's name type \"done\" to start the game"
					+ "type in \"#exit\" to abort the game"
					);
	}
}
