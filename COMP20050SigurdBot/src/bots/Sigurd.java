package bots;

import java.util.*;

import bots.CardMatrix.CardRow;
import bots.CardMatrix.PlayerColum;
import bots.CardMatrix.Position;
import gameengine.*;
import gameengine.Map;
import gameengine.LogParser.CardMatrix;
import gameengine.LogParser.Question;

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
        	Collection<String> allCards = new ArrayList<String>();
        	allCards.addAll(Arrays.asList(Names.ROOM_CARD_NAMES));
        	allCards.addAll(Arrays.asList(Names.SUSPECT_NAMES));
        	allCards.addAll(Arrays.asList(Names.WEAPON_NAMES));
            cardAgent = new CardAgent(log, allCards, playersInfo.numPlayers());
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
    	CardMatrix ourCardMatrix;
    	List<Question> questionList;
    	Log log;
    	int currlogPos;
    	java.util.Map<String,Integer> playerIndexMap;
    	
        CardAgent(Log log, Collection<String> cards, int numOfPlayers ) {
        	ourCardMatrix = new CardMatrix(cards, numOfPlayers);
        	questionList = new ArrayList<Question>();
        	this.log = log;
        	playerIndexMap = new HashMap<String,Integer>();
        	currlogPos = 1;
        	FillPlayerIndexMap();
        }
        
        void FillPlayerIndexMap() {
        	int i = 0;
        	for(String s : playersInfo.getPlayersNames())
        		playerIndexMap.put(s, i++);
        }
        
        void UpdateCards() {
            ParseTheLog();
            ParseTheQuestions();
        }
        
        void ParseTheLog() {//TODO : parsethelog method dose alot
        	Iterator<String> logIterator = log.iterator();
        	boolean alternator = true;
        	Question currQ = null;
        	java.util.Map<String, String> map = null;
        	int i = 0;
        	
        	while(logIterator.hasNext() && i++ < currlogPos)
        		logIterator.next();
        		
        	while(logIterator.hasNext()) {
        		String s = logIterator.next();
        		currlogPos++;
        		
        		if(alternator) {
        			currQ = new Question();
        			ParseAnouncment(s, currQ);
        		}else {
        			ParseResponce(s, currQ);
        			if(currQ.cardWasShowen)
        				questionList.add(currQ);
        			else {
        				ourCardMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.characterCard);
        				ourCardMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.weaponCard);
        				ourCardMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.roomCard);
        			}
        			if(currQ.cardShowen != null)
        				ourCardMatrix.CardFound(currQ.ressponder, currQ.cardShowen);
        				
        		}
        		alternator=!alternator;
            }
        }

        void ParseTheQuestions() {
        	boolean loop = true;
        	while(loop) {
        		loop = false;
        		for(Question q : questionList)
        			if(ParseQuestion(ourCardMatrix, q))
        				loop = true;
        	}
        				
        }
        
        boolean ParseQuestion(CardMatrix matrix, Question q) {//returns true if it changed the card matrix and all Qs need re-checking
    		//TODO : implement card parser
    		
    		
    		
    		return false;
    	}
        
        
    	void ParseAnouncment(String logMessage, Question q){
    		String[] part = logMessage.split(" ");
    		
    		System.out.println(logMessage);
    		
    		q.asker = playerIndexMap.get(part[0]);
    		q.ressponder = playerIndexMap.get(part[2]);
    		q.characterCard = part[4];
    		q.weaponCard = part[7];
    		q.roomCard = part[10].substring(0, part[10].length()-1);//removes full stop
    		
    	}

        void ParseResponce(String logMessage, Question q){
    		String[] part = logMessage.split(" ");
    		
    		System.out.println(logMessage);
    		
		if(q.ressponder != playerIndexMap.get(part[0]))
			throw new RuntimeException("tryed to parse the responce to the wrong quetion");
    		
		if(part[1].equals("showed"))
			q.cardWasShowen = true;
    	if(part[3].equals("card:"))
    		q.cardShowen = part[4].substring(0, part[4].length()-1);//remoeing full stop
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

    class Question {
    	int asker;
    	int ressponder;
    	String characterCard;
    	String weaponCard;
    	String roomCard;
    	boolean cardWasShowen;
    	String cardShowen;
    	    	
    	public String toString() {
    		String temp = asker + " asked " + ressponder + " if they have " +  characterCard  
    				+ ", " + weaponCard + ", " + roomCard;
    		if(cardShowen != null)
    			temp += ", they showed " + cardShowen;
    		return temp;
    	}
    }

	
}
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    