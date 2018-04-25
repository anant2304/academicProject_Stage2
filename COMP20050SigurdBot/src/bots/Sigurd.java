package bots;

import java.util.*;

import bots.Sigurd.CardMatrix.CardRow;
import bots.Sigurd.PathfinderAgent.MovePlan;
import gameengine.*;
import gameengine.Map;

public class Sigurd implements BotAPI {
    // Team Sigurd
    // Adrian Wennberg, Peter Major, Anant Shaw
    // 16751195, 16375246, 16202907
    
    // Bot written by Adrian Wennberg and Peter Major


    Player player;
    PlayersInfo playersInfo;
    Map map;
    Dice dice;
    Log log;
    Deck deck;
    ControllerAgent controller;
    
    final java.util.Map<String,Integer> playerIndexMap;
    final java.util.Map<Integer,String> indexPlayerMap;
    public final Random random;

    public Sigurd(Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;
        controller = new ControllerAgent();
        random = new Random();
        
        playerIndexMap = new HashMap<String,Integer>();
        indexPlayerMap = new HashMap<Integer,String>();
    }

    public String getName() {
        return "Sigurd";
    }

    public String getCommand() {
        return controller.GetCommand();
    }

    public String getMove() {
        return controller.movePlan.TakeStep();
    }

    public String getSuspect() {
        if(controller.cardAgent.HasSolution())
            return controller.cardAgent.characterMatrix.solutionCard;
        return controller.questionCards[0];
    }

    public String getWeapon() {
        if(controller.cardAgent.HasSolution())
            return controller.cardAgent.weaponMatrix.solutionCard;
        return controller.questionCards[1];
    }

    public String getRoom() {
        return controller.cardAgent.roomMatrix.solutionCard;
    }

    public String getDoor() {
        return controller.movePlan.TakeExit();
    }

    public String getCard(Cards matchingCards) {
        return matchingCards.iterator().next().toString();
    }

    public void notifyResponse(Log response) {
        controller.ParseResponse(response);
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void notifyPlayerName(String playerName) {
        
    }

    @Override
    public void notifyTurnOver(String playerName, String position) {
        
    }

    @Override
    public void notifyQuery(String playerName, String query) {
        
    }

    @Override
    public void notifyReply(String playerName, boolean cardShown) {
        
    }

    void FillPlayerIndexMap() {
        int i = 0;
        for(String s : playersInfo.getPlayersNames()) {
            playerIndexMap.put(s, i++);
        }
        playerIndexMap.put("Envelope", i);
        
        for(String s : playerIndexMap.keySet())
            indexPlayerMap.put(playerIndexMap.get(s), s);
    }
    
    
    class ControllerAgent {
        PathfinderAgent pathfinder;
        CardAgent cardAgent;

        final Queue<String> turnCommands;
        final String[] questionCards;
        
        MovePlan movePlan;
        int turnNumber;
        boolean turnStarted;

        ControllerAgent() {
            pathfinder = new PathfinderAgent();
            turnCommands = new LinkedList<>();
            questionCards = new String[3];
            
            turnNumber = 1;
            turnStarted = false;
            
            movePlan = pathfinder.GetStartPath(player.getToken().getPosition());
        }
        
        String GetCommand() {
            if (turnCommands.isEmpty()) {
                if(turnStarted == false)
                    TurnStart();
                    
                else 
                    PlanTurnEnd();

                turnStarted = !turnStarted;
            }
            return turnCommands.remove();
        }

        void TurnStart() {
            if(turnNumber == 1)
                cardAgent  = new CardAgent();
            
            cardAgent.UpdateCards();
            
            PlanMovement();

            if (movePlan.nextIsPassage) {
                movePlan.TakePassage();
                turnCommands.add("passage");
            }
            else
                turnCommands.add("roll");
        }
        
        private HashMap<Room, Float> GetRoomPriorities() {
            HashMap<Room, Float> unknownRooms = new HashMap<>();
            for (String s : Names.ROOM_CARD_NAMES) {
                CardRow cardRow = cardAgent.roomMatrix.cardRow.get(s);
                
                float priority = 1.0f;
                if(player.hasCard(s) || deck.isSharedCard(s) || 
                        (cardAgent.roomMatrix.isSolved && cardAgent.roomMatrix.solutionCard.equals(s)))
                    priority *= 0.2f;
                
                else if (cardRow.isFound || cardAgent.roomMatrix.isSolved)
                    priority *= 0.01f;
                
                else {
                    for(int i = 0; i < playersInfo.numPlayers(); i++){
                        if(cardRow.players.get(i).mightHave == false)
                            priority *= 2;
                    }
                }
                
                switch(s)
                {
                case"Hall":
                    priority *= 1 + 0.1 * turnNumber;
                    if(cardAgent.roomMatrix.isSolved)
                        priority *= 2;
                    break;
                case"Study":
                    priority *= 1 + 0.05 * turnNumber;
                    if(cardAgent.roomMatrix.isSolved)
                        priority *= 2;
                    break;
                case"Library":
                    priority *= 1 + 0.07 * turnNumber;
                    if(cardAgent.roomMatrix.isSolved)
                        priority *= 2;
                    break;
                case"Lounge":
                    priority *= 1 + 0.05 * turnNumber;
                    if(cardAgent.roomMatrix.isSolved)
                        priority *= 2;
                    break;
                case"Dining Room":
                    priority *= 1 + 0.05 * turnNumber;
                    if(cardAgent.roomMatrix.isSolved)
                        priority *= 2;
                    break;
                }

                unknownRooms.put(map.getRoom(s), priority);
            }
            return unknownRooms;
        }

        void PlanMovement() {
            if (IsInRoom() == false)
                return;

            if(cardAgent.HasSolution())
                movePlan = pathfinder.new MovePlan(Names.ROOM_NAMES[Names.ROOM_NAMES.length - 1]);
            
            else
                movePlan = pathfinder.MoveToNearest(GetRoomPriorities());
        }
        
        void PlanTurnEnd()
        {
            if (IsInRoom()) {
                
                if(GetCurrentRoom().hasName("Cellar"))
                    turnCommands.add("accuse");
                
                else if(cardAgent.HasSolution() == false)
                    AskQuestion();
            }

            turnCommands.add("done");
            turnNumber++;
        }

        void AskQuestion() {
            turnCommands.add("question");
            if(cardAgent.weaponMatrix.isSolved || cardAgent.characterMatrix.isSolved) {
                questionCards[0] = FindInterestingCard(cardAgent.characterMatrix, !cardAgent.characterMatrix.isSolved);
                questionCards[1] = FindInterestingCard(cardAgent.weaponMatrix,    !cardAgent.weaponMatrix.isSolved);
            }
            else {
                boolean guessWeapon = random.nextFloat() > 0.5f;
                questionCards[0] = FindInterestingCard(cardAgent.characterMatrix, !guessWeapon);
                questionCards[1] = FindInterestingCard(cardAgent.weaponMatrix,     guessWeapon);
                
            }
            questionCards[2] = GetCurrentRoom().toString();
        }

        void ParseResponse(Log response) {
            String last = "";
            for (String string : response) {
                last = string;
            }
    
            String[] lastSplit = last.split(": ");
            if( lastSplit.length > 1 ) {
                cardAgent.AllMatriciesCardFound(last.split("\\s+")[0],lastSplit[1].substring(0, lastSplit[1].length() -1));
            }
        }

        String FindInterestingCard(CardMatrix cardMatrix, boolean unknownCard) {
            List<String> topCards = new LinkedList<>();
            float value = 0;
            
            for (String s : cardMatrix.cardRow.keySet()) {
                CardRow row = cardMatrix.cardRow.get(s);
                float thisValue = 1.0f;
                
                if(row.isFound && (unknownCard ||
                  (row.owner != playerIndexMap.get(getName())
                  && row.owner != playerIndexMap.get("Envelope") )))
                    thisValue *= 0;
                
                if(unknownCard) {
                    for(int i = 0; i < playersInfo.numPlayers(); i++) {
                        if(row.players.get(i).mightHave == false)
                            thisValue *= 1.1;
                    }
                }
                
                if(thisValue >= value) {
                    if(value != thisValue) {
                        topCards.clear();
                        value = thisValue;
                    }
                    topCards.add(s);
                }
            }
            if(value == 0)
                throw new IllegalStateException("no interesting cards: " + unknownCard);
            
            return topCards.get(random.nextInt(topCards.size()));
        }
    
        boolean IsInRoom() {
            return map.isCorridor(player.getToken().getPosition()) == false;
        }

        Room GetCurrentRoom() {
            if (player.getToken().isInRoom())
                return player.getToken().getRoom();

            else if (IsInRoom())
                return map.getRoom(player.getToken().getPosition());

            throw new IllegalStateException("Not in a room");
        }
    }

    class PathfinderAgent {
        private final List<RoomPath>[] roomPaths;
        private final HashMap<MapCoordinates, MovePlan> startPaths;
        private final HashMap<Integer, Float> lengthsCosts;

        @SuppressWarnings("unchecked")
        PathfinderAgent() {
            roomPaths = new List[10];
            startPaths = new HashMap<>();
            lengthsCosts = new HashMap<>();
            GenerateLengthCosts();
            GenerateRoomPaths();
            GenerateStartPaths();
        }

        public MovePlan MoveToNearest(HashMap<Room, Float> hashMap) {
            List<MovePlan> plans = new LinkedList<>(); 
            for (Room r : hashMap.keySet()) 
                plans.add(new MovePlan(FindPathTo(r)));

            MovePlan bestPlan = plans.get(0);
            float minValue = bestPlan.GetCost() / hashMap.get(bestPlan.destination);
            
            for (MovePlan movePlan : plans) {
                if(movePlan.GetCost() / hashMap.get(movePlan.destination) < minValue) {
                    bestPlan = movePlan;
                    minValue = bestPlan.GetCost() / hashMap.get(bestPlan.destination);
                }
            }
            return bestPlan;
        }                 

        private Deque<RoomPath> FindPathTo(Room destination) {
            if(player.getToken().getRoom().equals(destination))
                return PathToSameRoom(destination);
            
            
            HashMap<Room, RoomPath> shortestPaths = new HashMap<>(); 
            HashMap<Room, Float> lowestCost = new HashMap<>();
            PriorityQueue<Entry> costOrdering = new PriorityQueue<>();
            

            shortestPaths.put(player.getToken().getRoom(), null);
            lowestCost.put(player.getToken().getRoom(), 0.0f);
            costOrdering.add(new Entry(0, player.getToken().getRoom()));

            while (costOrdering.peek().room.equals(destination) == false && costOrdering.isEmpty() == false) {
                for (RoomPath pathsFrom : GetRoomPaths(costOrdering.peek().room)) {
                    if (lowestCost.containsKey(pathsFrom.endRoom) == false || lowestCost
                            .get(pathsFrom.endRoom) > lowestCost.get(costOrdering.peek().room) + pathsFrom.cost) {
                        
                        costOrdering.add(new Entry(lowestCost.get(costOrdering.peek().room) + pathsFrom.cost, pathsFrom.endRoom));
                       
                        shortestPaths.put(pathsFrom.endRoom, pathsFrom);
                        lowestCost.put(pathsFrom.endRoom,
                                lowestCost.get(costOrdering.peek().room) + pathsFrom.cost);

                    }
                }
                costOrdering.remove();
            }

            Stack<RoomPath> reversePath = new Stack<>();
            Room current = destination;
            while (lowestCost.get(current) != 0.0f) {
                reversePath.push(shortestPaths.get(current));
                current = shortestPaths.get(current).startRoom;
            }

            Deque<RoomPath> path = new LinkedList<>();
            while (reversePath.isEmpty() == false)
                path.add(reversePath.pop());
            
            return path;
        }
        
        List<RoomPath> GetRoomPaths(Room startRoom) {
            for (int i = 0; i < 10; i++) {
                if (startRoom.hasName(Names.ROOM_NAMES[i]))
                    return roomPaths[i];
            }
            throw new IllegalArgumentException("Room " + startRoom + " not a room");
        }
        
        private Deque<RoomPath> PathToSameRoom(Room room)
        {
            Deque<RoomPath> path= new LinkedList<>();
            for (RoomPath p : GetRoomPaths(room)) {
                if(p.endRoom.equals(room))
                    path.add(p);
            }

            return path;
        }

        private void GenerateRoomPaths() {
            for (int i = 0; i < 10; i++)
                roomPaths[i] = new LinkedList<>();
            
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[5])));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[0])));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[7])));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[2])));
            
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[0]), 2, "du"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[1]), 7, "drrruur"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[2]), 20, "ddrrrrrrrrrrrrruruuu"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[3]), 17, "ddrrrrrrrrrrrrrdr"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[4]), 23, "ddrrrrrrrrrrrrddddddddr"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[5]), 28, "ddrrrrrrrrrrrrddddddddddddrd"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[6]), 19, "ddrrrrdddddddddrrrd"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[7]), 19, "ddrrrrddddddddddlld"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[8]), 11, "ddrrrrddddl"));
            roomPaths[0].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[0]), map.getRoom(Names.ROOM_NAMES[9]), 20, "ddrrrrdddddddddrrrru"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[0]), 7, "1llddllu"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[1]), 2, "1lr"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[2]), 4, "4rrru"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[3]), 6, "3ddrrrr"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[4]), 12, "3ddrrdddddddr"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[5]), 17, "3ddrrdddddddddddrd"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[6]), 13, "2ddddddddddrrd"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[7]), 15, "2ddddddddddllldd"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[8]), 7, "2dddddll"));
            roomPaths[1].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[1]), map.getRoom(Names.ROOM_NAMES[9]), 14, "2ddddddddddrrru"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[0]), 20, "dddldllllllllllllluu"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[1]), 4, "dlll"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[2]), 2, "du"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[3]), 7, "dlddddr"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[4]), 14, "dlddddddddrrrd"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[5]), 20, "dlldddddddddddddddrd"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[6]), 20, "dllddddddddddddlllld"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[7]), 27, "dllddddddddddddlllllllllldd"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[8]), 19, "dllddddlllllllldddl"));
            roomPaths[2].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[2]), map.getRoom(Names.ROOM_NAMES[9]), 20, "dllddddddddddddllllu"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[0]), 17, "1lullllllllllllluu"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[1]), 6, "1lllluu"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[2]), 7, "1luuuuru"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[3]), 2, "1lr"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[4]), 4, "2dlld"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[5]), 15, "1lldddddddddddrd"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[6]), 15, "1lllddddddddllld"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[7]), 22, "1lllddddddddllllllllldd"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[8]), 14, "1lllllllllldddl"));
            roomPaths[3].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[3]), map.getRoom(Names.ROOM_NAMES[9]), 15, "1lllddddddddlllu"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[0]), 23, "1luuuuuuuulllllllllllluu"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[1]), 12, "1lluuuuuuuluu"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[2]), 14, "2ullluuuuuuuuru"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[3]), 4, "2urru"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[4]), 2, "1lr"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[5]), 7, "1lddddrd"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[6]), 7, "1llddddl"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[7]), 14, "1lldllllllllldd"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[8]), 14, "1lldllllllllluu"));
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[9]), 7, "1lldlllu"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[0]), 28, "uluuuuuuuuuuuulllllllllllluu"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[1]), 17, "uluuuuuuuuuuulluu"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[2]), 20, "uluuuuuuuuuuuuuuurru"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[3]), 15, "uluuuuuuuuuuurr"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[4]), 7, "uuuluur"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[5]), 2, "ud"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[6]), 4, "ulll"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[7]), 17, "ulluuullllllllldd"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[8]), 17, "ulluuullllllllluu"));
            roomPaths[5].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[5]), map.getRoom(Names.ROOM_NAMES[9]), 10, "uuullulllu"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[0]), 19, "1ullluuuuuuuuulllluu"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[1]), 13, "1ulluuuuuuuuuu"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[2]), 20, "2urrrruuuuuuuuuuuurru"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[3]), 15, "2urrruuuuuuuurrr"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[4]), 7, "3ruuuurr"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[5]), 4, "3rrrd"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[6]), 2, "1ud"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[7]), 8, "1ullllldd"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[8]), 8, "1ullllluu"));
            roomPaths[6].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[6]), map.getRoom(Names.ROOM_NAMES[9]), 2, "2uu"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[0]), 19, "urruuuuuuuuuulllluu"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[1]), 15, "uurrruuuuuuuuuu"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[2]), 27, "uurrrrrrrrrruuuuuuuuuuuurru"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[3]), 22, "uurrrrrrrrruuuuuuuurrr"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[4]), 14, "uurrrrrrrrrurr"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[5]), 17, "uurrrrrrrrrdddrrd"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[6]), 8, "uurrrrrd"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[7]), 2, "ud"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[8]), 4, "uuuu"));
            roomPaths[7].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[7]), map.getRoom(Names.ROOM_NAMES[9]), 9, "uurrrrrru"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[0]), 11, "2ruuuulllluu"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[1]), 7, "2ruuuuru"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[2]), 19, "2ruuurrrrrrrruuuurru"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[3]), 14, "2ruuurrrrrrrrrr"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[4]), 14, "1ddrrrrrrrrrurr"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[5]), 17, "1ddrrrrrrrrrrdddrd"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[6]), 8, "1ddrrrrrd"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[7]), 4, "1dddd"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[8]), 2, "1du"));
            roomPaths[8].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[8]), map.getRoom(Names.ROOM_NAMES[9]), 9, "1ddrrrrrru"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[0]), 20, "dlllluuuuuuuuulllluu"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[1]), 14, "dllluuuuuuuuuu"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[2]), 20, "drrrruuuuuuuuuuuurru"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[3]), 15, "drrruuuuuuuurrr"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[4]), 7, "drrrrur"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[5]), 10, "drrrrdddrd"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[6]), 2, "dd"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[7]), 9, "dlllllldd"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[8]), 9, "dlllllluu"));
            roomPaths[9].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[9]), map.getRoom(Names.ROOM_NAMES[9]), 2, "du"));
        }

        private void GenerateStartPaths() {
            startPaths.put(new MapCoordinates(9, 0), new MovePlan("dllddddr", map.getRoom(Names.ROOM_NAMES[1]),new MapCoordinates(9, 0)));
            startPaths.put(new MapCoordinates(14, 0), new MovePlan("drrddddl", map.getRoom(Names.ROOM_NAMES[1]),new MapCoordinates(14, 0)));
            startPaths.put(new MapCoordinates(23, 6), new MovePlan("llllluu", map.getRoom(Names.ROOM_NAMES[2]),new MapCoordinates(23, 6)));
            startPaths.put(new MapCoordinates(23, 19), new MovePlan("lllllldd", map.getRoom(Names.ROOM_NAMES[5]),new MapCoordinates(23, 19)));
            startPaths.put(new MapCoordinates(7, 24), new MovePlan("uuuuuuld", map.getRoom(Names.ROOM_NAMES[7]),new MapCoordinates(7, 24)));
            startPaths.put(new MapCoordinates(0, 17), new MovePlan("rrrrrruu", map.getRoom(Names.ROOM_NAMES[8]),new MapCoordinates(0, 17)));
        }

        private void GenerateLengthCosts() {
            lengthsCosts.put(1, 1.0f);
            lengthsCosts.put(2, 1.0f);
            lengthsCosts.put(3, 1.02748f);
            lengthsCosts.put(4, 1.0813f);
            lengthsCosts.put(5, 1.16762f);
            lengthsCosts.put(6, 1.28387f);
            lengthsCosts.put(7, 1.43081f);
            lengthsCosts.put(8, 1.6113f);
            lengthsCosts.put(9, 1.77407f);
            lengthsCosts.put(10, 1.93387f);
            lengthsCosts.put(11, 2.07727f);
            lengthsCosts.put(12, 2.22286f);
            lengthsCosts.put(13, 2.35492f);
            lengthsCosts.put(14, 2.4804f);
            lengthsCosts.put(15, 2.61737f);
            lengthsCosts.put(16, 2.76581f);
            lengthsCosts.put(17, 2.91215f);
            lengthsCosts.put(18, 3.06262f);
            lengthsCosts.put(19, 3.20571f);
            lengthsCosts.put(20, 3.34366f);
            lengthsCosts.put(21, 3.48554f);
            lengthsCosts.put(22, 3.62919f);
            lengthsCosts.put(23, 3.77215f);
            lengthsCosts.put(24, 3.91537f);
            lengthsCosts.put(25, 4.05967f);
            lengthsCosts.put(26, 4.20056f);
            lengthsCosts.put(27, 4.3466f);
            lengthsCosts.put(28, 4.4894f);
            lengthsCosts.put(29, 4.62886f);
            lengthsCosts.put(30, 4.77219f);
            lengthsCosts.put(31, 4.91833f);
            lengthsCosts.put(32, 5.06258f);
            lengthsCosts.put(33, 5.19683f);
            lengthsCosts.put(34, 5.34499f);
            lengthsCosts.put(35, 5.48752f);
            lengthsCosts.put(36, 5.63099f);
            lengthsCosts.put(37, 5.77364f);
            lengthsCosts.put(38, 5.91678f);
            lengthsCosts.put(39, 6.05671f);
            lengthsCosts.put(40, 6.20273f);
            lengthsCosts.put(41, 6.34496f);
            lengthsCosts.put(42, 6.49048f);
            lengthsCosts.put(43, 6.63132f);
            lengthsCosts.put(44, 6.77791f);
            lengthsCosts.put(45, 6.91356f);
            lengthsCosts.put(46, 7.06065f);
            lengthsCosts.put(47, 7.19714f);
            lengthsCosts.put(48, 7.34595f);
            lengthsCosts.put(49, 7.48767f);
        }

        MovePlan GetStartPath(Coordinates coordinates)
        {
            return startPaths.get(new MapCoordinates(coordinates));
        }
        
        float GetPathCost(int length)
        {
            if(length >= 50)
                throw new IllegalStateException("Path more than 50 squares long");
            else if(lengthsCosts.containsKey(new Integer(length)) == false)
                throw new IllegalStateException(length + " has no cost");
            return lengthsCosts.get(length);
        }
        
        class Entry implements Comparable<Entry>
        {
            final float cost;
            final Room room;
            
            public Entry(float c, Room r) {
                cost = c;
                room = r;
            }
            
            @Override
            public int compareTo(Entry o) {
                return  ((Float)cost).compareTo(o.cost);
            }
        }
        
        class MapCoordinates extends Coordinates {
            MapCoordinates(int col, int row) {
                super(col, row);
            }

            MapCoordinates(Coordinates co) {
                super(co);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null || Coordinates.class.isAssignableFrom(obj.getClass()) == false)
                    return false;

                Coordinates other = (Coordinates) obj;
                return other.getCol() == getCol() && other.getRow() == getRow();
            }

            @Override
            public int hashCode() {
                return getCol() * 37 + getRow();
            }
        }

        class RoomPath {
            final Room startRoom;
            final Room endRoom;
            final int length;
            final String steps;
            final float cost;

            RoomPath(Room startRoom, Room endRoom, int length, String steps) {
                this.startRoom = startRoom;
                this.endRoom = endRoom;
                this.length = length;
                this.steps = steps;
                this.cost = GetPathCost(length);
            }
            
            RoomPath(Room startRoom, Room endRoom)
            {
                this.startRoom = startRoom;
                this.endRoom = endRoom;
                this.length = 1;
                this.steps = "p";
                this.cost = 1;
            }
            
            RoomPath(Room endRoom, String steps)
            {
                this.startRoom = null;
                this.endRoom = endRoom;
                this.length = steps.length();
                this.steps = steps;
                this.cost = GetPathCost(steps.length());
            }
            
            boolean IsPassage()
            {
                return steps.equals("p");
            }
        }
    
        class MovePlan {
            private final Deque<RoomPath> paths;
            private final Room destination;
            private final Queue<String> currentSteps;

            private String exitDoor;
            private MapCoordinates currentPosition;
            private Room currentRoom;
            private Room currentDestination;
            boolean nextIsPassage;
            
            MovePlan(Deque<RoomPath> pathsQueue){
                if(pathsQueue.isEmpty())
                    throw new IllegalStateException("No paths given");
                
                paths = pathsQueue;

                currentRoom = paths.peek().startRoom;
                currentDestination = paths.peek().endRoom;
                destination = paths.getLast().endRoom;

                currentSteps = new LinkedList<>();
                SetSteps(paths.peek().steps); 
                
                paths.remove();
            }
            
            MovePlan(String steps, Room room, MapCoordinates startPos) {
                paths = new LinkedList<>();
                currentSteps = new LinkedList<>();
                destination = room;
                currentDestination = room;
                currentPosition = startPos;
                SetSteps(steps);
            }
            
            MovePlan(String destinationRoom) {
                this(FindPathTo(map.getRoom(destinationRoom)));
            }

            public float GetCost() {              
                
                float cost = 0;
                if(currentSteps.isEmpty() == false)
                    cost += GetPathCost(currentSteps.size());
                    
                else if(nextIsPassage)
                    cost += 1.0f;

                for (RoomPath p : paths)
                    cost += p.cost;
                        
                return cost;
            }

            boolean IsDone()
            {
                return paths.isEmpty() && currentSteps.isEmpty() && nextIsPassage == false;
            }
            
            Room GetDestiantion() {
                return destination;
            }

            public Room GetCurrentDestination() {
                return currentDestination;
            }

            int StepsLeftToRoom()
            {
                return currentSteps.size();
            }
            
            private void ReachedRoom() {
                if(IsDone())
                    return;
                if(currentSteps.isEmpty()){
                    currentRoom = paths.peek().endRoom;
                    currentPosition = null;
                    paths.remove();
                    if(paths.isEmpty() == false) {
                        currentDestination = paths.peek().endRoom;
                        SetSteps(paths.peek().steps);
                    }
                }
            }

            public boolean IsStillValid() {
                return (currentPosition != null && currentPosition.equals(player.getToken().getPosition()))
                    || (currentRoom != null && currentRoom.equals(player.getToken().getRoom()));
            }

            String TakeStep()
            {
                if(nextIsPassage)
                    throw new IllegalStateException("Taking steps on passage");
                
                if(currentSteps.isEmpty())
                    throw new IllegalStateException("No more steps to take");
                    
                String step = currentSteps.remove();

                if(currentPosition == null) {
                    currentPosition = new MapCoordinates(map.getNewPosition(currentRoom.getDoorCoordinates(0),step));
                    currentRoom = null;
                
                } else if(StepsLeftToRoom() == 0)
                    ReachedRoom();
                
                else
                    currentPosition = new MapCoordinates(map.getNewPosition(currentPosition, step));
                
                return step;
            }

            String TakeExit()
            {
                if(exitDoor.equals("-1"))
                    throw new IllegalStateException("Plan has no door");
                
                String door = exitDoor;
                currentPosition = new MapCoordinates(currentRoom.getDoorCoordinates(Integer.parseInt(exitDoor) - 1));
                exitDoor = "-1";
                currentRoom = null;
                return door;
            }
            
            public void TakePassage() {
                if(nextIsPassage == false)
                    throw new IllegalStateException("Taking a passage at the wrong time.");
                nextIsPassage = false;
                ReachedRoom();
                
            }
            
            private void SetSteps(String steps) {
                currentSteps.clear();

                if (steps.equals("p")) {
                    nextIsPassage = true;
                    return;
                }
                
                if(steps.matches("\\d.*")) {
                    exitDoor = steps.substring(0, 1);
                    steps = steps.substring(1);
                }
                else
                    exitDoor = "-1";
                
                for (char c : steps.toCharArray())
                    currentSteps.add(c + "");
            }
        }
    }
    
    class CardAgent {
        final CardMatrix characterMatrix;
        final CardMatrix weaponMatrix;
        final CardMatrix roomMatrix;
        final List<Question> questionList;
        final InferenceEngine iEngine;
        int currlogPos;
        
        CardAgent() {
            characterMatrix= new CardMatrix(Arrays.asList(Names.SUSPECT_NAMES), playersInfo.numPlayers());
            weaponMatrix= new CardMatrix(Arrays.asList(Names.WEAPON_NAMES), playersInfo.numPlayers());
            roomMatrix= new CardMatrix(Arrays.asList(Names.ROOM_CARD_NAMES), playersInfo.numPlayers());
            questionList = new ArrayList<Question>();
            iEngine = new InferenceEngine();
            currlogPos = 0;
            FillPlayerIndexMap();
            
            GetStartingCards();
        }
        
        boolean HasSolution()
        {
            return characterMatrix.isSolved && roomMatrix.isSolved && weaponMatrix.isSolved;
        }
        
        void GetStartingCards() {
            for(Card c : player.getCards()) {
                characterMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
                weaponMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
                roomMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
            }
            
            for(Card c : deck.getSharedCards()){
                characterMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
                weaponMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
                roomMatrix.CardFound(playerIndexMap.get(getName()), c.toString());
            }
            for(String card : Names.SUSPECT_NAMES)
                if(player.hasCard(card) == false)
                    characterMatrix.cardRow.get(card).DoseNotHave(playerIndexMap.get(getName()));
            
            for(String card : Names.WEAPON_NAMES)
                if(player.hasCard(card) == false)
                    weaponMatrix.cardRow.get(card).DoseNotHave(playerIndexMap.get(getName()));
            
            for(String card : Names.ROOM_CARD_NAMES)
                if(player.hasCard(card) == false)
                    roomMatrix.cardRow.get(card).DoseNotHave(playerIndexMap.get(getName()));
        }
        
        void UpdateCards() {
            ParseTheLog();
            ParseTheQuestions();
        }
        
        void AllMatricesPlayerDoseNotHave(int playerIndex, String cardName) {
            characterMatrix.PlayerDoseNotHave(playerIndex, cardName);
            weaponMatrix.PlayerDoseNotHave(playerIndex, cardName);
            roomMatrix.PlayerDoseNotHave(playerIndex, cardName);
        }
        
        void AllMatriciesCardFound(String player, String cardName ) {
            int playerIndex = playerIndexMap.get(player);
            
            characterMatrix.CardFound(playerIndex, cardName);
            weaponMatrix.CardFound(playerIndex, cardName);
            roomMatrix.CardFound(playerIndex, cardName);
        }
        
        void ParseTheLog() {
            Iterator<String> logIterator = log.iterator();
            int i = 0;
            
            //skip to the where we left off
            while(logIterator.hasNext() && i++ < currlogPos)
                logIterator.next();
                
            //iterate over what we have not seen yet
            while(logIterator.hasNext()) {
                Question currQ = new Question(questionList);
                currlogPos += 2;
                
                ParseAnouncment(logIterator.next(), currQ);
                ParseResponce(logIterator.next(), currQ);
                
                if(iEngine.InishalLogMessageCheck(currQ))
                    questionList.add(currQ);
            }
        }

        void ParseTheQuestions() {
            boolean loop = true;
            while(loop) {
                loop = false;
                for(Question currQ : questionList)
                    if(iEngine.ParseQuestion(currQ))
                        loop = true;
                DeleteTagedQuestions();
            }
                        
        }
        
        void DeleteTagedQuestions() {
            List<Question> temp = new ArrayList<Question>();
            temp.addAll(questionList);
            for(Question q : temp)
                if(q.tagForDeletion == true)
                    questionList.remove(q);
        }
                
        void ParseAnouncment(String logMessage, Question q){
            String[] part = logMessage.split(" the ");
            String[] playerParts = part[0].split("\\s+");
            
            q.asker = playerIndexMap.get(playerParts[0]);
            q.ressponder = playerIndexMap.get(playerParts[3]);
            q.characterCard = playerParts[6];
            q.weaponCard = part[1].substring(0, part[1].length() - 3);
            q.roomCard = part[2].substring(0, part[2].length() - 1);
        }

        void ParseResponce(String logMessage, Question q){
            String[] part = logMessage.split("\\s++");
            
            if(q.ressponder != playerIndexMap.get(part[0]))
                throw new RuntimeException("tryed to parse the responce to the wrong quetion");
                
            if(part[2].equals("showed")) 
                q.wasCardShowen = true;
            
            if(part[4].equals("card:"))
                q.cardShowen = part[5].substring(0, part[4].length()-1);
        }
        
        class InferenceEngine{
            boolean InishalLogMessageCheck(Question currQ){
                if(currQ.cardShowen != null) {
                    characterMatrix.CardFound(currQ.ressponder, currQ.cardShowen);
                    characterMatrix.CardFound(currQ.ressponder, currQ.cardShowen);
                    characterMatrix.CardFound(currQ.ressponder, currQ.cardShowen);
                } else if(currQ.wasCardShowen)
                    return true;
                
                else {
                    characterMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.characterCard);
                    weaponMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.weaponCard);
                    roomMatrix.PlayerDoseNotHave(currQ.ressponder,currQ.roomCard);
                }
                return false;
            }
            
            boolean ParseQuestion(Question currQ) {
                boolean HasCharacter = characterMatrix.cardRow.get(currQ.characterCard).players.get(currQ.ressponder).mightHave;
                boolean HasWeapon  = weaponMatrix.cardRow.get(currQ.weaponCard).players.get(currQ.ressponder).mightHave;
                boolean HasRoom  = roomMatrix.cardRow.get(currQ.roomCard).players.get(currQ.ressponder).mightHave;
                
                if(currQ.tagAllCardsChecked == false) {
                    if(!HasCharacter && !HasRoom) {
                        weaponMatrix.CardFound(currQ.ressponder, currQ.weaponCard);
                        currQ.tagAllCardsChecked = true;
                        return true;
                    }
                    else if(!HasRoom && !HasWeapon) {
                        characterMatrix.CardFound(currQ.ressponder, currQ.characterCard);
                        currQ.tagAllCardsChecked = true;
                        return true;
                    }
                    else if(!HasCharacter && !HasWeapon) {
                        roomMatrix.CardFound(currQ.ressponder, currQ.roomCard);
                        currQ.tagAllCardsChecked = true;
                        return true;
                    }
                }
                
                return false;
            }
        }
        
    }

    class CardMatrix{
        final java.util.Map<String,CardRow> cardRow;
        final java.util.Map<Integer,PlayerColum> playerCol;
        
        boolean isSolved;
        String solutionCard;
        
        CardMatrix(Collection<String> cards, int numPlayers){
            cardRow = new HashMap<String, CardRow>();
            playerCol = new HashMap<Integer,PlayerColum>();
            
            isSolved = false;
            
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
            if(cardRow.containsKey(c) && cardRow.get(c).isFound == false) {
                cardRow.get(c).Found(p);
                for(Position pos : 
                    cardRow.get(c).players)
                    if(pos.player != p) pos.mightHave = false;
            }
            CheckForSolution();
        }
        
        void PlayerDoseNotHave(int p, String c) {
            if(cardRow.containsKey(c) && cardRow.get(c).isFound == false)
                cardRow.get(c).DoseNotHave(p);
            CheckForSolution();
        }
    
        void CheckForSolution() {
            if(isSolved)
            {
                for(CardRow cr : cardRow.values()) {
                    if(cr.isFound)
                        continue;
                    
                    int numMightHave = 0;
                    Position lastFound = null;
                    for(Position p : cr.players) {
                        if(p.mightHave) {
                            numMightHave++;
                            lastFound = p;
                        }
                    }
                    if(numMightHave == 1)
                        cr.Found(lastFound.player);
                }
            }else {
                int numToFind = 0;
                CardRow lastToFind = null;
                
                for(CardRow cr : cardRow.values()) {
                    boolean noOneHas = true;
                    for(Position p : cr.players)
                        if(p.mightHave == true)
                            noOneHas = false;
                    if(noOneHas == true) {
                        cr.Found(playerIndexMap.get("Envelope"));
                        isSolved = true;
                        solutionCard = cr.name;
                        return;
                    }
                    
                    if(cr.isFound == false) {
                        lastToFind = cr;
                        numToFind++;
                        }
                }
                if(numToFind == 1) {
                    lastToFind.Found(playerIndexMap.get("Envelope"));
                    isSolved = true;
                    solutionCard = lastToFind.name;
                }
            }
                
        }
        
        public String toString() {
            String temp = "   " + indexPlayerMap.get(0) +" "
            + indexPlayerMap.get(1) +" "+ indexPlayerMap.get(2) + "\n";
            
            for(CardRow cr : cardRow.values()) {
                temp += String.format("%-13s %-30s |", cr.name, indexPlayerMap.get(cr.owner));
                
                for(int i = 0; i < playerCol.size(); i++)
                    temp += cr.players.get(i).mightHave + " ";
                
                temp += "\n";
            }
            
            return temp;
        }
        
        public class CardRow{
            final String name;
            final List<Position> players ;
            boolean isFound = false;
            int owner = -1;
            
            CardRow(String name){
                this.name = name;
                players = new ArrayList<Position>();
            }
            
            void DoseNotHave(int p){
                players.get(p).mightHave = false;
            }
            
            void Found(int p) {
                isFound = true;
                owner = p;
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
                final int player;
                final String card;
                
                public boolean mightHave = true;
                
                Position(int player, String card){
                    this.player = player;
                    this.card = card;
                }
            }
    }

    class Question {
        Collection<Question> questionList;
        
        int asker;
        int ressponder;
        String characterCard;
        String weaponCard;
        String roomCard;
        boolean wasCardShowen;
        String cardShowen;

        boolean tagAllCardsChecked;
        boolean tagForDeletion; 
   
        Question(Collection<Question> questionList){
            tagForDeletion = false;
            this.questionList = questionList;
        }
        
        public void Delete() {
            if(questionList.contains(this))
                tagForDeletion = true;
            else 
                throw new RuntimeException("Tryed to delete a question that is not in the list");
        }
        
        public String toString() {
            String temp = asker + " asked " + ressponder + " if they have " +  characterCard  
                    + ", " + weaponCard + ", " + roomCard;
            if(cardShowen != null)
                temp += ", they showed " + cardShowen;
            return temp;
        }
    
    }
} 