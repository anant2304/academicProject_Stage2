package Sigurd;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Sigurd.BoardObjects.BoardObject;
import Sigurd.BoardObjects.PlayerObject;

/**
 * @author Adrian Wennberg Team: Sigurd Student Numbers: 16751195, 16202907,
 *         16375246
 */
public class Board {
    private static final String BOARD_PATH = "/Layout.txt";
    private static final String ROOMS_PATH = "/RoomInfo.txt";
    private boolean[][] boardArray; // Grid array, true if grid square is in the
                                    // hallway.
    private Map<Coordinates, Door> doorPositions;
    private List<BoardObject> boardObjectList;
    private BoardPanel panel;
    private Room[] rooms;
    private Room selectedRoom;

    /**
     * Testing the board display
     * 
     * @param args
     */
    public static void main(String[] args) {
        Board b = new Board();
        b.SetRoom(b.rooms[1]);
        // b.display();
        JFrame frame = new JFrame();
        frame.add(b.GetBoardPanel());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Constructor to create the initial board.
     */
    public Board() {
        boardObjectList = new LinkedList<>();
        panel = new BoardPanel();
        LoadBoard();
        LoadRooms();
    }

    public void SetRoom(Room r) {
        selectedRoom = r;
    }

    public void ResetRoom() {
        selectedRoom = null;
    }

    /**
     * Loads the board layout from a file and creates the boardArray.
     */
    private void LoadBoard() {
        boardArray = new boolean[24][25];

        try (Scanner layoutReader = new Scanner(Board.class.getResource(BOARD_PATH).openStream(), "UTF-8")) {
            int row = 0;
            while (layoutReader.hasNext()) {
                String line = layoutReader.next();
                for (int i = 0; i < line.length(); i++) {
                    boardArray[i][row] = (line.charAt(i) == '1');
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadRooms() {
        rooms = new Room[10];
        doorPositions = new HashMap<Coordinates, Door>();
        try (Scanner layoutReader = new Scanner(Board.class.getResource(ROOMS_PATH).openStream(), "UTF-8")) {
            int roomIndex = 0;
            while (layoutReader.hasNext()) {
                String line = layoutReader.nextLine();
                String[] lineParts = line.split("\\s+");

                String[] roomNameParts = lineParts[0].split("_");
                String roomName = roomNameParts[0];
                for (int i = 1; i < roomNameParts.length; i++)
                    roomName += " " + roomNameParts[i];

                String[] doorStrings = lineParts[1].split("\\|");
                Door[] doorCoordinates = new Door[doorStrings.length];

                for (int i = 0; i < doorStrings.length; i++) {
                    doorCoordinates[i] = new Door(doorStrings[i]);
                }

                Coordinates roomCentrePosition = (lineParts.length > 2 ? new Coordinates(lineParts[2]) : null);

                rooms[roomIndex] = new Room(roomName, doorCoordinates, roomCentrePosition);

                for (Door c : doorCoordinates)
                    doorPositions.put((Coordinates) c, c);

                roomIndex++;
            }

            rooms[0].SetPassageRoom(rooms[5]);
            rooms[5].SetPassageRoom(rooms[0]);

            rooms[2].SetPassageRoom(rooms[7]);
            rooms[7].SetPassageRoom(rooms[2]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Room[] GetRooms()
    {
        return rooms;
    }

    public boolean IsPositionMovable(Coordinates current, Coordinates to) {
        boolean validX = to.getCol() >= 0 && to.getCol() < boardArray.length;
        boolean validY = to.getRow() >= 0 && to.getRow() < boardArray[0].length;
        return validX && validY
                && !IsPlayerPosition(to) && (boardArray[to.getCol()][to.getRow()] || (IsDoor(to) && doorPositions.get(to).HasOutside(current)));
    }

    public boolean IsPlayerPosition(Coordinates co) {
        for (BoardObject p : boardObjectList) {
            if (p.GetCoordinates().equals(co) && p.getClass().isAssignableFrom(PlayerObject.class))
                return true;
        }
        return false;
    }

    public boolean IsDoor(Coordinates co) {
        return doorPositions.containsKey(co);
    }

    public Room GetDoorRoom(Coordinates co) {
        return doorPositions.get(co).GetRoom();
    }

    /**
     * Adds a BoardObject to the board.
     * 
     * @param boardObject
     *            - Board Object to add.
     */
    public void AddMovable(BoardObject boardObject) {
        boardObjectList.add(boardObject);
    }

    /**
     * Gets the panel where the board is displayed.
     * 
     * @return The panel where the board is displayed
     */
    public JPanel GetBoardPanel() {
        return panel;
    }

    /**
     * Prints the contents of the board array in 1s and 0s.
     */
    private void display() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < boardArray[0].length; i++) {
            for (int j = 0; j < boardArray.length; j++) {
                str.append(boardArray[j][i] ? '1' : '0');
            }
            str.append('\n');
        }
        System.out.println(str.toString());
    }

    /**
     * A JPanel that displays the game board.
     * 
     * @author Adrian Wennberg
     * 
     */
    @SuppressWarnings("serial")
    private class BoardPanel extends JPanel {

        private static final String BOARD_PATH = "/cluedo board.jpg";
        private static final int CORNER_X = 42;
        private static final int CORNER_Y = 23;
        private static final int CELL_SIZE = 23;

        private BufferedImage boardImage;

        private int prefH;
        private int prefW;

        public BoardPanel() {
            try {
                boardImage = ImageIO.read(BoardPanel.class.getResource(BOARD_PATH));
                prefW = boardImage.getWidth();
                prefH = boardImage.getHeight();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Used by the swing system. Gets the preferred size of the panel.
         */
        public Dimension getPreferredSize() {
            return new Dimension(prefW, prefH);
        }

        /**
         * Used by the swing system. Decides how the panel is drawn.
         * 
         * @param g
         *            - Graphics object.
         */
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(boardImage, 0, 0, this);

            DrawBoardObjects(g2d);

            if (selectedRoom != null)
                DrawRoomDoors(g2d, selectedRoom);
        }

        /**
         * Draws BoardObjects added to the board.
         * 
         * @param g2d
         *            - Graphics2D object to draw on
         */
        private void DrawBoardObjects(Graphics2D g2d) {
            g2d.translate(CORNER_X, CORNER_Y);
            for (BoardObject m : boardObjectList) {
                if (m.GetImage() != null)
                    g2d.drawImage(m.GetImage(), m.GetCoordinates().getCol() * CELL_SIZE,
                            m.GetCoordinates().getRow() * CELL_SIZE, this);
                else {
                    System.err.println("WARNNING : No image found for : " + m.GetObjectName());
                }
            }
        }

        private void DrawRoomDoors(Graphics2D g2d, Room r) {
            Font f = new Font("Arial", Font.BOLD, 23);
            g2d.setFont(f);
            Door[] doors = r.GetDoors();
            for (int i = 0; i < doors.length; i++) {
                Coordinates outsideDoor = doors[i].GetOutside();
                g2d.drawString(" " + (i + 1), outsideDoor.getCol() * CELL_SIZE, (1 + outsideDoor.getRow()) * CELL_SIZE);
            }
        }
    }
}
