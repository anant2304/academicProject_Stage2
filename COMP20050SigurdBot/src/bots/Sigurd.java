package bots;

import java.util.*;

import bots.CardMatrix.CardRow;
import bots.CardMatrix.PlayerColum;
import bots.CardMatrix.Position;
import gameengine.*;
import gameengine.Map;

public class Sigurd implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the board or the player objects
    // It may only inspect the state of the board and the player objects

    private Player player;
    private PlayersInfo playersInfo;
    private Map map;
    private Dice dice;
    private Log log;
    private Deck deck;

    public Sigurd (Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;
        controller = new ControllerAgent();
    }
    ControllerAgent controller;

    public String getName() {
        return "Sigurd";
    }

    public String getCommand() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetCommand(); 
    }

    public String getMove() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetMove();
    }

    public String getSuspect() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetCommand();
    }

    public String getWeapon() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetCommand();
    }

    public String getRoom() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetCommand();
    }

    public String getDoor() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetMove();
    }

    public String getCard(Cards matchingCards) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.ChooseCard(matchingCards);
    }
    

    public void notifyResponse(Log response) {
        // Add your code here
    }
    
    class ControllerAgent {
        Queue<String> path;
        Stack<String> turnCommands;
        
        PathfinderAgent pathfinder;
        CardAgent cardAgent;

        ControllerAgent() {
            cardAgent = new CardAgent();
            pathfinder = new PathfinderAgent();
            
        }

        public String ChooseCard(Cards matchingCards) {
            return matchingCards.next().toString();
        }

        public String GetMove() {
            if(path == null || path.isEmpty())
            {
                if(player.getToken().isInRoom()) {
                    ChoosePath(pathfinder.GetOptions(player.getToken().getRoom(), dice.getTotal()));
                }
                else
                    ChoosePath(pathfinder.GetOptions(player.getToken().getPosition(), dice.getTotal()));
            }
            return path.remove();
        }

        private void ChoosePath(List<PathfinderAgent.Path> options) {
            path = new LinkedList<>();
            for(char c : options.get(0).moves.toCharArray())
                path.add(c + "");
            
            for (PathfinderAgent.Path path : options) {
                System.out.println(path.moves);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }

        public String GetCommand() {
            if(turnCommands == null || turnCommands.isEmpty()) {
                cardAgent.UpdateCards();
                PlanTurn();
            }
            return turnCommands.pop();
        }

        private void PlanTurn() {
            turnCommands = new Stack<>();
            turnCommands.push("done");
            turnCommands.push("roll");
        }
    }
    

    class PathfinderAgent {

        final String[] dirs = {"u", "l", "d", "r"};
        public PathfinderAgent() {
        }

        // Get a list of the possible paths to take from on a room
        // Should include a path back to the same room at least
        public List<Path> GetOptions(Room room, int maxSteps) {
            return null;
        }

        // Get a list of the possible paths to take from on a position
        public List<Path> GetOptions(Coordinates startPosition, int maxSteps) {
            return null;
        }
        
        class Path {
            /** Steps to take in order i.e. "uullu" is up, up, left, left, up. 
             * Start with a digit if starting in a room.  */
            final String moves;
            
            /** True if path ends in a room. */
            final boolean goesToRoom;
            final Room room; // room at end of path
            final Coordinates coordinates; // coordinates at end of path
            
            Path(String m, Coordinates c)
            {
                moves = m;
                goesToRoom = false;
                room = null;
                coordinates = c;
            }
            

            Path(String m, Room r)
            {
                moves = m;
                goesToRoom = true;
                room = r;
                coordinates = null;
            }
        }
    }

    
    class CardAgent {

        public CardAgent() {
            // TODO Peter add card agent setup based on player information here
        }

        public void UpdateCards() {
            // TODO Peter add card agent logic here
        }

    }

    class CardMatrix{
        java.util.Map<String,CardRow> cardRow;
        java.util.Map<Integer,PlayerColum> playerCol;
        
     	CardMatrix(Collection<String> cards, int numPlayers){
     		cardRow = new HashMap<String, CardRow>();
     		playerCol = new HashMap<Integer,PlayerColum>();
     		
     		for(String s : cards)
     			cardRow.put(s, new CardRow(s));
     		
    		for(int i = 0; i < numPlayers; i++) {
    			playerCol.put(i, new PlayerColum(i));
    			PlayerColum tempPlay = playerCol.get(i);
    			
    			for(String s : cards) {
        			CardRow tempCard = cardRow.get(s);
        			
        			Position tempPos = new Position(tempPlay.index,tempCard.name);
        			tempCard.players.add(tempPos);
        			tempPlay.cards.add(tempPos);
    			}
    		}
    	}
    	
    	void CardFound(int p, String c) {
    		cardRow.get(c).crossoutAllBarOne(p);
    	}
    	
    	void PlayerDoseNotHave(int p, String c) {
    		cardRow.get(c).crossout(p);
    	}
    	
    	CardRow getCardRow(String s) {
    		for(CardRow cr : cardRow.values())
    			if(cr.name.equals(s) == true)
    				return cr;
    		return null;
    	}
    	
    	public String toString() {
    		String temp = "";
    		
    		for(CardRow cr : cardRow.values()) {
    			temp += cr.name + "| ";
    			for(int i = 0; i < playerCol.size(); i++)
    				temp += cr.players.get(i).crossedout + " ";
    			temp += "\n";
    		}
    		
    		return temp;
    	}
    	
    	
    	class CardRow{
    		String name;
    		List<Position> players = new ArrayList<Position>();
    		boolean isFound = false;
    		int owner;
    		
     		CardRow(String name){
    			this.name = name;
    		}
    		
    		void crossout(int p){
    			players.get(p).crossedout = true;
    			
    			int count = 0;
    			for(Position q : players)
    				if(q.crossedout == false) count++;
    			if(count == 1) Found();
    		}
    		
    		void crossoutAllBarOne(int p) {
    			for(int i = 0; i < players.size();i++)
    				if(i != p) crossout(i);
    			Found();
    		}
    		
    		void Found() {
    			isFound = true;
    			for(Position q : players)
    				if(q.crossedout == false)
    					owner = q.player;
    		}
    		
    	}
    	
    	class PlayerColum{
    		int index;    		
    		List<Position> cards= new ArrayList<Position>();;
    		
    		PlayerColum(int index){
    			this.index = index;
    		}
    	}
    	
    	class Position{
        		int player;
        		String card;
        		
        		public boolean crossedout = false;
        		
        		Position(int plyaer, String card){}
        	}
    }

    class LogParser {
    	
    	java.util.Map<String,String> ParseResponce(String logMessage){
		java.util.Map<String, String> map = new HashMap<String,String>();
		String[] part = logMessage.split(" ");
		
		map.put("showingPlayer", part[0]);
		map.put("showedCard", part[1].equals("showed") ? "true":"false" );
		if(part[3].equals("card:"))
			map.put("cardShowen", part[4]);
		
		return map;
	}
	
		java.util.Map<String, String> ParseAnouncment(String logMessage){
		java.util.Map<String, String> map = new HashMap<String,String>();
		
		String[] part = logMessage.split(" ");
		
		map.put("firstPlayer", part[0]);
		map.put("secondPlayer", part[2]);
		map.put("characterCard", part[4]);
		map.put("weaponCard", part[7]);
		map.put("roomCard", part[10].substring(0, part[10].length()-1));//removeing full stop
		
		return map;
	}
	}

    class Question {
    	CardMatrix myMatrix;
    	Collection<String> cards;
    	int asker;
    	int ressponder;
    	
    	Question(Collection<String> cards) {
    		this.cards = cards;
    	}
    }
}
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    