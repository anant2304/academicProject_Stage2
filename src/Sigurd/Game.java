package Sigurd;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Cards.Card;
import Cards.Deck;
import Cards.PlayerCard;

import java.io.IOException;

import java.util.*;
import java.util.Map.Entry;

import java.util.Vector;
import java.util.Collections;

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
    private static PlayerSignIn playerSign;
    private static Deck deck;
    
    private static Stack<Turn> turnStack = new Stack<Turn>();
    
    static int[] turn= {0,0,0,0,0,0};
    static int c=0;
    static int d01,d02,max,pos;
    private static Card[] envelope;
    private static Map<String,PlayerObject> characterMap = new HashMap<String,PlayerObject>();
    private static Map<String,WeaponObject> weaponMap = new HashMap<String,WeaponObject>();
    
    static Random rand=new Random(System.currentTimeMillis());
    
    public static language lang1;
    
    
    /**
     * @Summary the main that runs the game
     * @param args
     */
    public static void main(String[] args) {
        command = new CommandPanel();
        display = new DisplayPanel();
        board = new Board();
        playerSign = new PlayerSignIn();
        deck = new Deck();
        
        CreateWindow();
        PlacePlayers();
        PlaceWeapons();
        
        lang1=new language();
        
        command.TakeFocus();//would be in create window but some issue causes it to work only half the time, here it always works
    
        try
        {
            lang1.main();
        }
        catch(IOException e)
        {
            
        }
        
        for(int i=0;i<4;i++)
        {
            display.SendMessage(lang1.English[i]+"\n");
        }
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
        
        
        window.setResizable(false); //makes the frame non-resizable
        window.pack();
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
        display.SendMessage(lang1.English[4]+"\n");
        RollForEach();
        DealCards();
        NextTurn();
    }

    private static void RollForEach()
    {
        for(int i=0;i<playerSign.strength;i++)
        {
            if(turn[i]!=-1)
            {
                d01=rand.nextInt(6)+1;
                d02=rand.nextInt(6)+1;
                turn[i]=d01+d02;
            }
        }
        display.SendMessage(lang1.English[5]+"\n");
        for(int i=0;i<playerSign.strength;i++)
        {
            if(turn[i]!=-1)
            {
                display.SendMessage(playerSign.players.get(i).GetPlayerName()+" "+ turn[i]+"\n");
            }
        }
        max=turn[0];
        pos=0;
        for(int i=0;i<playerSign.strength;i++)
        {
            if(turn[i]>max)
            {
                max=turn[i];
                pos=i;
            }
        }
        for(int i=0;i<playerSign.strength;i++)
        {
            if(turn[i]!=max)
            {
                turn[i]=-1;
            }
        }
        for(int i=0;i<playerSign.strength;i++)
        {
            if(turn[i]!=max)
                c++;
        }
        if(c==playerSign.strength-1)
        {
            display.SendMessage(playerSign.players.get(pos).GetPlayerName()+" got the highest roll of "+max+" \n");
            ChangeOrder(pos);
        }
        else
        {
            display.SendMessage(lang1.English[6]+"\n");
            RollForEach();
        }
    }
    
    private static void DealCards() {
        Vector<Player> players = playerSign.getPlayers();
        while(deck.Size() >= playerSign.strength)
        {
            for(int i = 0; i < playerSign.strength; i++)
            {
                players.get(i).GiveCard(deck.DrawCard());
            }
        }
        
        while(deck.IsEmpty() == false)
        {
            Card c = deck.DrawCard();
            for(Player p : players)
                p.GiveCard(c);
            c.SetCanEveryOneSee();
        }
    }
    
    private static void ChangeOrder(int p)
    {
        if(p!=0)
        {   
            int j=0;
            
            for(int i=p;i<playerSign.strength;i++)
            {
                int x=i;
                while(x>j)
                {
                    Collections.swap(playerSign.players,x-1,x);
                    x--;
                }
                j++;
            }
        }
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
        weaponMap.put("rope",new WeaponObject(new Coordinates(0,0),new Character('R'),"Rope"));
        weaponMap.put("dagger",new WeaponObject(new Coordinates(0,0),new Character('D'),"Dagger"));
        weaponMap.put("wrench",new WeaponObject(new Coordinates(0,0),new Character('W'),"Wrench"));
        weaponMap.put("pistol",new WeaponObject(new Coordinates(0,0),new Character('P'),"Pistol"));
        weaponMap.put("candlestick",new WeaponObject(new Coordinates(0,0),new Character('C'),"CandleStick"));
        weaponMap.put("leadpipe",new WeaponObject(new Coordinates(0,0),new Character('L'),"LeadPipe"));
        
        Room[] rooms = board.GetRooms();
        int i = 0;
        for(Entry<String, WeaponObject> e : weaponMap.entrySet())
        {
            e.getValue().MoveToRoom(rooms[i++]);
            if(i % 3 == 2)
                i++;
        }
        
        
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
    public static void NewTurn(Player p) {
        Turn newTurn = turnStack.push(new Turn(p));
        if(newTurn.CanLeaveRoom())
            board.SetRoom(p.GetPlayerObject().GetRoom());
        board.GetBoardPanel().repaint();
        display.SendMessage(turnStack.peek().GetPlayer().GetPlayerName() + " its your turn, you are " + turnStack.peek().GetPlayer().GetCharacterName());
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
    
    public static Collection<PlayerObject> GetAllCharcters() {
        return characterMap.values();
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
    
    public static Iterator<? extends Card> GetCards(Class<? extends Card> c)
    {
        return deck.GetAllCards(c);
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
        display.SendDevMessage(com);
        switch(com) {
        case "#exit" :
            System.exit(0);
            break;
        case "#steps100" :
            turnStack.peek().SetStepsLeft(100);
            break;
        case "#cheat" :
            Card[] envelope = deck.GetEnvelope();
            display.SendMessage("The murder was committed by: " + envelope[0].getName()
                                + "\nWith the weapon: "         + envelope[1].getName()
                                + "\nIn the room: "             + envelope[2].getName());
            break;
        case "#help" :
            display.SendMessage(
                    "These are cheat/testing comands, not to be used in a normal game\n"
                    + "type in \"#steps100\" to set your current steps to 100\n"
                    + "type in \"#cheat\" to inspect the murder envelope\n"
                    + "type in \"#exit\" to quit the game\n "
                    );
            break;
        }
        
    }
    
    /**
     * @Summary Dispalys a context sensitive help menu to the display pannel
     */
    private static void DisplayHelp(){
        if(isGameStarted())
        {
            for(int i=7;i<13;i++)
            {
                display.SendMessage(lang1.English[i]+"\n");
            }
        }
        else
        {
            for(int i=13;i<18;i++)
            {
                display.SendMessage(lang1.English[i]+"\n");
            }
        }
    }
}
