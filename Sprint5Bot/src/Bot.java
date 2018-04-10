import java.util.*;

public class Bot implements BotAPI {
    ControllerAgent controller;

    Bot (PlayerAPI player, MapAPI map, DiceAPI dice, LogAPI log) {
        controller = new ControllerAgent(player, map, dice, log);
    }

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
    
    class ControllerAgent {

        PlayerAPI player;
        MapAPI map;
        DiceAPI dice;
        LogAPI log;
        Stack<String> path;
        Stack<String> turnCommands;
        
        PathfinderAgent pathfinder;
        CardAgent cardAgent;

        ControllerAgent(PlayerAPI player, MapAPI map, DiceAPI dice, LogAPI log) {
            cardAgent = new CardAgent(player, log);
            pathfinder = new PathfinderAgent(map);
            this.player = player;
            this.map = map;
            this.dice = dice;
            this.log = log;
        }

        public String ChooseCard(Cards matchingCards) {
            return matchingCards.next().toString();
        }

        public String GetMove() {
            if(path.isEmpty())
            {
                if(player.getToken().isInRoom())
                    ChoosePath(pathfinder.GetOptions(player.getToken().getRoom(), dice.getTotal()));
                else
                    ChoosePath(pathfinder.GetOptions(player.getToken().getPosition(), dice.getTotal()));
            }
            return path.pop();
        }

        private void ChoosePath(List<PathfinderAgent.Path> options) {
            Stack<String> reversePath = new Stack<>();
            path = new Stack<>();
            for(char c : options.get(0).moves.toCharArray())
                reversePath.push(c + "");
            while(reversePath.isEmpty() == false)
                path.push(reversePath.pop());
            
        }

        public String GetCommand() {
            if(turnCommands.isEmpty()) {
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

        public PathfinderAgent(MapAPI map) {
        }

        // Get a list of the possible paths to take from on a room
        // Should include a path back to the same room at least
        public List<Path> GetOptions(Room room, int maxSteps) {
            // TODO Anant add pathfinding here
            return null;
        }

        // Get a list of the possible paths to take from on a position
        public List<Path> GetOptions(Coordinates startPosition, int maxSteps) {
            // TODO Anant add pathfinding here
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

        public CardAgent(PlayerAPI player, LogAPI log) {
            // TODO Peter add card agent setup based on player information here
        }

        public void UpdateCards() {
            // TODO Peter add card agent logic here
        }

    }
}
