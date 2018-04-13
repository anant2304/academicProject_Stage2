package bots;

import java.util.*;

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
}
