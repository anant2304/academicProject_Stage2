package Sigurd;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Cards.Card;
import Cards.Deck;
import Sigurd.BoardObjects.*;
import javafx.print.PageLayout;

import java.util.*;
import java.util.Map.Entry;

/**
 * Servers an an entry point and ties the different classes together.
 * 
 * @author Peter Major Team: Sigurd Student Numbers: 16751195, 16202907,
 *         16375246
 */
public class Game {
    private static CommandPanel command;
    private static Board board;
    private static DisplayPanel display;
    private static PlayerSignIn playerSign;
    private static Deck deck;
    private static Stack<Turn> turnStack = new Stack<Turn>();
    
    private static Map<String, PlayerObject> characterMap = new HashMap<String, PlayerObject>();
    private static Map<String, WeaponObject> weaponMap = new HashMap<String, WeaponObject>();
    private static boolean isGameOver;
    
    static Random rand = new Random(System.currentTimeMillis());
    
    /**
     * @Summary the main that runs the game
     */
    public static void main(String[] args) {
        command = new CommandPanel();
        display = new DisplayPanel();
        board = new Board();
        playerSign = new PlayerSignIn();
        isGameOver = false;
        
        CreateWindow();
        PlacePlayers();
        PlaceWeapons();
        
        deck = new Deck();// must come after placeing players and weaponss
        
        command.TakeFocus();// would be in create window but some issue causes
        // it to work only half the time, here it always
        // works
        
        
        display.SendMessage("Enter the names of the players "+"\n"+
                            "in the form [Player Name] [Character Name] "+"\n"+
                            "when you are finished type in \"done\" to start the game"+"\n"+
                            "type in \"help\" at any time to receive help\n");
        
        // the game now waits for input, first that input is passed to the
        // PlayerSignIn class,
        // after the game has started it is then passed to each respective turn
        // object as they are taken
    }
    
    /**
     * private constructor
     */
    private Game() {
    }
    
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
        
        window.setResizable(false); // makes the frame non-resizable
        window.pack();
        window.setVisible(true);
        
        // sets the currser to the command line when the game window is opened
        window.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                command.TakeFocus();
            }
        });
    }
    
    /**
     * @Summary called by the PlayerSignIn class to progress the game into a
     *          playable state
     */
    public static void StartGame() {
        display.clearScreen();
        display.SendMessage("Rolling for each player: \n");
        RollToStart(null);
        DealCards();
        NextTurn();
    }
    
    private static void RollToStart(int[] lastRolls) {
        int[] rolls = { 0, 0, 0, 0, 0, 0 };
        int maxRoll = 0;
        int pos = 0;
        if (lastRolls == null)
            lastRolls = rolls;
        
        // Roll for everyone who hasn't rolled and store the max.
        for (int i = 0; i < playerSign.playerCount; i++) {
            if (lastRolls[i] != -1) {
                int d01 = rand.nextInt(6) + 1;
                int d02 = rand.nextInt(6) + 1;
                rolls[i] = d01 + d02;
                display.SendMessage(playerSign.players.get(i) + " " + d01 + ", " + d02 + "\n");
                if (rolls[i] > maxRoll) {
                    maxRoll = rolls[i];
                    pos = i;
                }
            }
        }
        
        // Count how many players got the max roll and remove others form
        // rolling.
        int count = 0;
        for (int i = 0; i < rolls.length; i++) {
            if (rolls[i] != maxRoll)
                rolls[i] = -1;
            else
                count++;
        }
        
        // Roll again of more than one player got the max roll
        if (count == 1) {
            display.SendMessage(playerSign.players.get(pos) + " got the highest roll of " + maxRoll + " \n");
            playerSign.currPossition = (playerSign.playerCount + pos - 1) % playerSign.playerCount;
        } else {
            display.SendMessage("Rolling again for tied players: \n");
            RollToStart(rolls);
        }
    }
    
    private static void DealCards() {
        Vector<Player> players = playerSign.getPlayers();
        while (deck.Size() >= playerSign.playerCount) {
            for (int i = 0; i < playerSign.playerCount; i++) {
                players.get(i).GiveCard(deck.DrawCard());
            }
        }
        
        while (deck.IsEmpty() == false) {
            Card c = deck.DrawCard();
            c.SetCanEveryOneSee();
        }
    }
    
    private static void EndGame() {
        isGameOver = true;
        display.SendMessage("The Game is over\nThe winner is : " + turnStack.peek().GetPlayer().GetPlayerName());
        display.SendMessage("enter any command to exit the game");
    }
    
    private static boolean IsLastPlayer() {
        int inGame = 0;
        for (Player temp : playerSign.getPlayers()) {
            if (temp.IsOutOfGame() == false)
                inGame++;
        }
        return inGame < 2;
    }
    
    /**
     * @Summary creates and places all the players onto the board
     */
    private static void PlacePlayers() {
        for (String s : Reasource.GetCharacterData()) {
            PlayerObject character = ParsePlayerLine(s);
            characterMap.put(character.GetObjectName(), character);
            board.AddMovable(character);
        }
    }
    
    private static PlayerObject ParsePlayerLine(String line) {
        String[] temp = line.split("\\s+");
        
        return new PlayerObject(new Coordinates(temp[1]), Color.decode(temp[2]), temp[0]);
    }
    
    /**
     * @Summary creates and places all weapons onto the board
     */
    private static void PlaceWeapons() {
        for (String s : Reasource.GetWeaponData()) {
            WeaponObject weapon = ParseWeaponLine(s);
            weaponMap.put(weapon.GetObjectName(), weapon);
            board.AddMovable(weapon);
        }
        Room[] rooms = board.GetRooms();
        int i = 0;
        for (Entry<String, WeaponObject> e : weaponMap.entrySet()) {
            e.getValue().MoveToRoom(rooms[i++]);
            if (i % 3 == 2)
                i++;
        }
    }
    
    public static WeaponObject ParseWeaponLine(String line) {
        String[] temp = line.split("\\s+");
        
        return new WeaponObject(new Coordinates(temp[1].trim()), temp[0].charAt(0), temp[0].trim());
    }
    
    /**
     * @Summary returns whether there has been a turn yet
     */
    public static boolean isGameStarted() {
        return (!turnStack.isEmpty());
    }
    
    /**
     * @Summary creates a new turn with the next player
     */
    public static void NextTurn() {
        Player temp;
        do {
            temp = playerSign.NextPlayer();
            if (temp.IsOutOfGame() == false)
                NewTurn(temp);
            else
                display.SendMessage(temp.GetPlayerName() + " is out of the game");
        } while (temp.IsOutOfGame());
    }
    
    /**
     * @Summary ends the last turn and starts a new one
     */
    public static void NewTurn(Player p) {
        if (IsLastPlayer()) {
            EndGame();
            return;
        }
        Turn newTurn = turnStack.push(new Turn(p, playerSign.players));
        
        if (newTurn.CanLeaveRoom())
            board.SetRoom(p.GetPlayerObject().GetRoom());
        board.GetBoardPanel().repaint();
        
        display.SendMessage("type in \\\"help\\\" at any time to receive help \n");
        display.SendMessage("type in \\”passage\\” to take passage \n");
        if (turnStack.size() > 1) {
            display.clearScreen();
            display.SendMessage("type in \\\"help\\\" at any time to receive help \n");
            display.SendMessage("type in \\”passage\\” to take passage \n");
        }
        display.SendMessage(turnStack.peek().GetPlayer().GetPlayerName() + " its your turn, you are "
                            + turnStack.peek().GetPlayer().GetCharacterName());
    }
    
    /**
     * @Summary returns a reference to the current turn
     */
    public static Turn CurrentTurn() {
        return turnStack.peek();
    }
    
    public static boolean DoesCharacterExist(String name) {
        return characterMap.containsKey(name);
    }
    
    public static boolean DoesWeaponExist(String name) {
        return weaponMap.containsKey(name);
    }
    
    public static boolean DoesRoomExist(String name) {
        for (Room room : board.GetRooms()) {
            if (room.GetName().equals(name))
                return true;
        }
        return false;
    }
    
    public static PlayerObject GetCharacter(String name) {
        return characterMap.get(name);
    }
    
    public static Collection<PlayerObject> GetAllCharcters() {
        return characterMap.values();
    }
    
    public static Collection<WeaponObject> GetAllWeapons() {
        return weaponMap.values();
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
    
    public static Iterator<? extends Card> GetCards(Class<? extends Card> c) {
        return deck.GetAllCards(c);
    }
    
    public static <E extends Card> E GetCard(String name, Class<E> c) {
        return deck.GetCard(name, c);
    }
    
    public static boolean IsGameOver() {
        return isGameOver;
    }
    
    /**
     * @Summary Takes a command and passes it to the correct command method in
     *          some class
     */
    public static void PassCommand(String com) {
        
        if (com.equalsIgnoreCase(""))
            ;// ignore empty strings
        else if (com.charAt(0) == '#') {
            Commands(com);
        } else if (com.equalsIgnoreCase("help")) {
            DisplayHelp();
        } else if (com.equalsIgnoreCase("log")) {
            DisplayLog();
        } else if (IsGameOver() == true) {
            System.exit(0);
        } else if (isGameStarted() == false) {
            playerSign.Commands(com);
        } else {
            turnStack.peek().Commands(com);// commands in the turn class
            board.GetBoardPanel().repaint();
        }
    }
    
    /**
     * @Summary exicutes developer commands for debuging purposes
     */
    private static void Commands(String com) {
        display.SendDevMessage(com);
        switch (com) {
            case "#exit":
                System.exit(0);
                break;
            case "#steps100":
                turnStack.peek().SetStepsLeft(100);
                break;
            case "#cheat":
                Card[] envelope = deck.GetEnvelope();
                display.SendMessage("The murder was committed by: " + envelope[0].getName() + "\nWith the weapon: "
                                    + envelope[1].getName() + "\nIn the room: " + envelope[2].getName());
                break;
            case "#end":
                EndGame();
                break;
            case "#knockout":
                if (isGameStarted() == true) {
                    turnStack.peek().GetPlayer().KnockOutOfGame();
                    display.SendMessage("knockout : " + turnStack.peek().GetPlayer().GetPlayerName());
                }
                break;
            case "#help":
                display.SendMessage("These are cheat/testing comands, not to be used in a normal game\n"
                                    + "type in \"#steps100\" to set your current steps to 100\n"
                                    + "type in \"#cheat\" to inspect the murder envelope\n" + "type in \"#exit\" to quit the game\n ");
                break;
            default:
                display.SendMessage("no sutch dev command");
                break;
        }
    }
    
    /**
     * @Summary Dispalys a context sensitive help menu to the display pannel
     */
    private static void DisplayHelp() {
        if (isGameStarted()) {
            if (turnStack.peek().IsAskingQuestion() == false) {
                
                display.SendMessage("type in \"roll\" to roll your dice \n type in u, d, l or r to move up, down, left, or right respectively \n if you are in a room at the start of your turn, after rolling type the number corresponding to an exit to leave \n type in \"done\" to end your turn \n type in \"quit\" to close down the game \n type in \"#exit\" if something goes wrong \n type in a name then press enter or return to add it to the game");
                
            } else {
                display.SendMessage("Input the card type that you are prompted for\n"
                                    + "Type \"characters\" to see a list of all characters\n"
                                    + "Type \"weapons\" to see a list of weapons\n" + "Type \"rooms\" to see a list of rooms\n");
            }
        } else {
            
            display.SendMessage("type in a name then press enter or return to add it to the game\r\n" +
                                "type in \\\"players\\\" to see who is currently in the game\r\n" +
                                "type in \\\"characters\\\" to see unclaimed characters\r\n" + 
                                "if you have entered everyone's name type \\\"done\\\" to start the game\r\n" + 
                                "type in \\\"#exit\\\" to abort the game\r\n" + 
                                "you must have at least 2 players to play");
            
        }
    }
    
    public static void DisplayLog()
    {
        display.SendMessage("The previous questions were: ");
        for(String s: display.qList)
        {
            display.SendMessage(s);
        }
        display.SendMessage("The previous answers were: ");
        for(String s: display.aList)
        {
            display.SendMessage(s);
        }
    }
}

