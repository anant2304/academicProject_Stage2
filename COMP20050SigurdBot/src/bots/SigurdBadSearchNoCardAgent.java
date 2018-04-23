package bots;

import java.util.*;

import bots.SigurdBadSearchNoCardAgent.PathfinderAgent.*;
import gameengine.*;
import gameengine.Map;

public class SigurdBadSearchNoCardAgent implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the board or the player objects
    // It may only inspect the state of the board and the player objects

    Player player;
    PlayersInfo playersInfo;
    Map map;
    Dice dice;
    Log log;
    Deck deck;
    ControllerAgent controller;

    public SigurdBadSearchNoCardAgent(Player player, PlayersInfo playersInfo, Map map, Dice dice, Log log, Deck deck) {
        this.player = player;
        this.playersInfo = playersInfo;
        this.map = map;
        this.dice = dice;
        this.log = log;
        this.deck = deck;
        controller = new ControllerAgent();
    }

    public String getName() {
        return "Sigurd";
    }

    public String getCommand() {
        return controller.GetCommand();
    }

    public String getMove() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controller.GetMove(false);
    }

    public String getSuspect() {
        if(controller.hasSolution)
            return controller.solution[0];
        return controller.GetCommand();
    }

    public String getWeapon() {
        if(controller.hasSolution)
            return controller.solution[1];
        return controller.GetCommand();
    }

    public String getRoom() {
        if(controller.hasSolution)
            return controller.solution[2];
        return controller.GetCommand();
    }

    public String getDoor() {
        return controller.GetMove(true);
    }

    public String getCard(Cards matchingCards) {
        return matchingCards.iterator().next().toString();
    }

    public void notifyResponse(Log response) {
        controller.ParseResponse(response);

    }

    class ControllerAgent {
        PathfinderAgent pathfinder;
        CardAgent cardAgent;

        Queue<String> turnCommands;
        String[] solution;
        List<String> questionCards;
        Set<String> suspectedCards;
        
        MovePlan movePlan;
        int turnNumber;
        boolean hasSolution;
        boolean turnStarted;

        ControllerAgent() {
            cardAgent  = new CardAgent();
            pathfinder = new PathfinderAgent();

            turnCommands = new LinkedList<>();
            solution     = new String[3];
            suspectedCards = new HashSet<>();
            
            turnNumber = 1;
            
            hasSolution = false;
            turnStarted = false;
            
            movePlan = pathfinder.GetStartPath(player.getToken().getPosition());
        }
        
        String GetCommand() {
            if (turnCommands.isEmpty()) {
                if(turnStarted == false) {
                    TurnStart();
                }
                else {
                    PlanTurnEnd();
                }
                turnStarted = !turnStarted;
            }

            return turnCommands.remove();
        }

        private void Display(String string) {
            System.out.println(player + " Turn: " + turnNumber + "\n " + string);
        }
        
        void TurnStart() {
            PlanMovement();

            if (movePlan.IsNextPassage()) {
                movePlan.TakePassage();
                turnCommands.add("passage");
            }
            else
                turnCommands.add("roll");
        }
        
        private Set<Room> GetInterestingRooms() {
            Set<Room> unknownRooms = new HashSet<>();
            for (String s : Names.ROOM_CARD_NAMES) {
                if ((player.hasCard(s) || player.hasSeen(s) || deck.isSharedCard(s)) == false && unknownRooms.contains(map.getRoom(s)) == false)
                    unknownRooms.add(map.getRoom(s));
            }
            return unknownRooms;
        }

        String GetMove(boolean door) {
            if(door)
                return movePlan.GetExitDoor();
            else
                return movePlan.TakeStep();
        }

        void PlanMovement() {
            if (movePlan.IsDone() == false && movePlan.IsStillValid())
                return;

            
            if(hasSolution)
                movePlan = pathfinder.new MovePlan(Names.ROOM_NAMES[Names.ROOM_NAMES.length - 1]);
            
            else
                movePlan = pathfinder.MoveToNearest(GetInterestingRooms());
            
        }
        
        void PlanTurnEnd()
        {

            if (IsInRoom()) {
                if(GetCurrentRoom().hasName("Cellar"))
                    MakeAccusation();
                
                else if(hasSolution == false)
                    AskQuestion();
            }

            turnCommands.add("done");
            turnNumber++;
        }

        void MakeAccusation() {
            Display("Making accusation on turn: " + turnNumber);
            turnCommands.add("accuse");
            questionCards = new LinkedList<>();
            for (String s : solution)
                turnCommands.add(s);
        }

        void AskQuestion() {
            questionCards = new LinkedList<>();
            turnCommands.add("question");
            String suspectCard = FindInterestingCard(Names.SUSPECT_NAMES);
            questionCards.add(suspectCard);
            turnCommands.add(suspectCard);
            String weaponCard = FindInterestingCard(Names.WEAPON_NAMES);
            questionCards.add(weaponCard);
            turnCommands.add(weaponCard);
            questionCards.add(GetCurrentRoom().toString());
        }

        void ParseResponse(Log response) {
            String last = "";
            for (String string : response) {
                last = string;
            }
            if (last.length() > 23
                    && last.substring(last.length() - 6, last.length()).equals("cards."))
                PossibleAnswer();
        }

        void PossibleAnswer() {
            for (String s : questionCards) {
                if ((player.hasCard(s) || player.hasSeen(s) || deck.isSharedCard(s)) == false)
                    suspectedCards.add(s);
            }
            ProposeSolution(suspectedCards);
        }

        void ProposeSolution(Set<String> suspectedCards) {
            if (suspectedCards.size() < 3)
                return;
            else if (suspectedCards.size() > 3)
                throw new IllegalStateException("more than 3 known cards");

            String weapon = "", room = "", suspect = "";
            for (String s : Names.SUSPECT_NAMES)
                if (suspectedCards.contains(s))
                    suspect = s;

            for (String s : Names.WEAPON_NAMES)
                if (suspectedCards.contains(s))
                    weapon = s;

            for (String s : Names.ROOM_CARD_NAMES)
                if (suspectedCards.contains(s))
                    room = s;

            if (room.equals("") || weapon.equals("") || suspect.equals(""))
                throw new IllegalStateException("cards not all 3 types");

            solution[0] = suspect;
            solution[1] = weapon ;
            solution[2] = room   ;
            hasSolution = true;
        }

        String FindInterestingCard(String[] cards) {
            for (String s : cards) {
                if ((player.hasCard(s) || player.hasSeen(s) || deck.isSharedCard(s)) == false)
                    return s;
            }
            throw new IllegalStateException("no interesting cards");
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
        int[] totalPossibleOutcomes = {36, 35, 33, 30, 26, 21, 15, 10, 6, 3, 1};
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

        public MovePlan MoveToNearest(Set<Room> interestingRooms) {
            List<MovePlan> plans = new LinkedList<>(); 
            for (Room r : interestingRooms) {
                plans.add(new MovePlan(FindPathTo(r)));
            }
            MovePlan bestPlan = plans.get(0);
            float minCost = bestPlan.GetCost();
            for (MovePlan movePlan : plans) {
                if(movePlan.GetCost() < minCost)
                    bestPlan = movePlan;
            }
   
            return bestPlan;
        }

        private Deque<RoomPath> FindPathTo(Room destination) {
            if(player.getToken().getRoom().equals(destination))
                return PathToSameRoom(destination);
            
            
            HashMap<Room, RoomPath> shortestPaths = new HashMap<>();
            HashMap<Room, Integer> shortestDistance = new HashMap<>();
            Queue<Room> searching = new LinkedList<>();
            boolean foundPath = false;

            searching.add(player.getToken().getRoom());
            shortestPaths.put(player.getToken().getRoom(), null);
            shortestDistance.put(player.getToken().getRoom(), 0);

            while (foundPath == false && searching.isEmpty() == false) {
                for (RoomPath pathsFrom : GetRoomPaths(searching.peek())) {
                    if (shortestDistance.containsKey(pathsFrom.endRoom) == false || shortestDistance
                            .get(pathsFrom.endRoom) > shortestDistance.get(searching.peek()) + pathsFrom.length) {
                        if (shortestDistance.containsKey(pathsFrom.endRoom) == false)
                            searching.add(pathsFrom.endRoom);

                        shortestPaths.put(pathsFrom.endRoom, pathsFrom);
                        shortestDistance.put(pathsFrom.endRoom,
                                shortestDistance.get(searching.peek()) + pathsFrom.length);

                        if (pathsFrom.endRoom.hasName(destination.toString())) {
                            foundPath = true;
                        }
                    }
                }
                searching.remove();
            }

            Stack<RoomPath> reversePath = new Stack<>();
            Room current = destination;
            while (shortestDistance.get(current) > 0) {
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
            roomPaths[4].add(new RoomPath(map.getRoom(Names.ROOM_NAMES[4]), map.getRoom(Names.ROOM_NAMES[2]), 14, "2ullluuuuuuuuuru"));
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
            
            public float GetCost() {              
 
                float cost = 0;
                if(currentSteps.isEmpty() == false)
                    cost += GetPathCost(currentSteps.size());

                for (RoomPath p : paths) {
                    cost += p.cost;
                }
                        
                return cost;
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

            boolean IsNextPassage()
            {
                return nextIsPassage;
            }
            
            boolean IsDone()
            {
                return paths.isEmpty() && currentSteps.isEmpty() && nextIsPassage == false;
            }
            
            String TakeStep()
            {
                if(IsNextPassage())
                    throw new IllegalStateException("Taking steps on passage");
                if(currentSteps.isEmpty()) {
                    System.out.println(paths);
                    System.out.println(currentDestination);
                    System.out.println(destination);
                    throw new IllegalStateException("No more steps to take");
                    
                }
                String step = currentSteps.remove();
                
                if(currentPosition == null) {
                    currentPosition = new MapCoordinates(map.getNewPosition(currentRoom.getDoorCoordinates(0),step));
                    currentRoom = null;
                
                }else {
                    Coordinates newPos = map.getNewPosition(currentPosition, step);
                    
                    if(StepsLeftToRoom() == 0)
                        ReachedRoom();
                    else
                        currentPosition = new MapCoordinates(newPos);
                }
                
                return step;
            }

            String GetExitDoor()
            {
                String door = exitDoor;
                currentPosition = new MapCoordinates(currentRoom.getDoorCoordinates(Integer.parseInt(exitDoor) - 1));
                exitDoor = "-1";
                currentRoom = null;
                return door;
            }
            
            Room GetDestiantion() {
                return destination;
            }

            public Room GetCurrentDestination() {
                return paths.peek().endRoom;
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

            public void TakePassage() {
                if(IsNextPassage() == false)
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

    
}
