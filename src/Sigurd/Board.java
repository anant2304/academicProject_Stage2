package Sigurd;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Sigurd.BoardObjects.BoardObject;

/**
 * 
 * @author Adrian Wennberg
 *
 */
public class Board {
    private static Board Instance = new Board(); // Singleton instance of the board class.
    private static final String BOARD_PATH = "Assets/Layout.txt"; // Path to the board layout.
    private boolean[][] boardArray; // Grid array, true if grid square is in the hallway.
    private List<BoardObject> boardObjectList; // List of objects on the board.
    private BoardPanel panel; // Panel where the board is dispayed.

    /**
     * Testing the board display
     * @param args
     */
    public static void main(String[] args) {
        Board b = GetBoard();
        b.display();
        JFrame frame = new JFrame();
        frame.add(b.GetBoardView());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Constructor to create the initial board.
     */
    private Board() {
        boardObjectList = new LinkedList<>();
        panel = new BoardPanel();
        LoadBoard();
    }

    /**
     * Gets the reference to the board object.
     * @return A reference to the board singleton instance.
     */
    public static Board GetBoard() {
        return Instance;
    }

    /**
     * Loads the board layout from a file and creates the boardArray.
     */
    private void LoadBoard() {
        boardArray = new boolean[24][25];
        
        try(Scanner layoutReader = new Scanner(new File(BOARD_PATH), "UTF-8"))
        {
            int row = 0;
            while(layoutReader.hasNext())
            {
                String line = layoutReader.next();
                for(int i = 0; i < line.length(); i++)
                {
                    boardArray[i][row] = (line.charAt(i) == '1');
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a position can be moved to.
     * @param x - an x coordinate on the grid
     * @param y - a y coordinate on the grid
     * @return true if the x, y grid position can be moved to.
     */
    public boolean IsPositionMovable(int x, int y) {
        boolean validX = x >= 0 && x < boardArray.length;
        boolean validY = y >= 0 && y < boardArray[0].length;
        return validX && validY && boardArray[x][y];
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
     * Adds a BoardObject to the board.
     * @param boardObject - Board Object to add.
     */
    public void AddMovable(BoardObject boardObject) {
        boardObjectList.add(boardObject);
    }

    /**
     * Gets the panel where the board is displayed.
     * @return The panel where the board is displayed
     */
    public JPanel GetBoardPanel() {
        return panel;
    }

    /**
     * A JPanel that displays the game board.
     * @author Adrian Wennberg
     */
    @SuppressWarnings("serial")
    private class BoardPanel extends JPanel {

        /**
         * File path to the board image.
         */
        private static final String BOARD_PATH = "Assets/cluedo board.jpg";
        /**
         * Grid corner x coordinate.
         */
        private static final int CORNER_X = 42; 
        /**
         * Grid corner y coordinate
         */
        private static final int CORNER_Y = 23; 
        /**
         * Grid cell size.
         */
        private static final int CELL_SIZE = 23; 

        /**
         *  Image of the game board.
         */
        private BufferedImage boardImage;
        /**
         * Preferred height of the board panel;
         */
        private int prefH;
        /**
         * Preferred width of the board panel;
         */
        private int prefW;

        /**
         * Constructor
         */
        public BoardPanel() {
            try {
                boardImage = ImageIO.read(new File(BOARD_PATH));
                prefW = boardImage.getWidth();
                prefH = boardImage.getHeight();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * Used by the swing system.
         * Gets the preferred size of the panel.
         */
        public Dimension getPreferredSize() {
            return new Dimension(prefW, prefH);
        }

        /**
         * Used by the swing system.
         * Decides how the panel is drawn.
         * @param g - Graphics object.
         */
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(boardImage, 0, 0, this);

            DrawBoardObjects(g2d);
        }

        /**
         * Draws BoardObjects added to the board.
         * @param g2d - Graphics2D object to draw on
         */
        private void DrawBoardObjects(Graphics2D g2d) {
            g2d.translate(CORNER_X, CORNER_Y);
            for (BoardObject m : boardObjectList) {
                if(m.GetImage() != null)
                    g2d.drawImage(m.GetImage(), m.GetX() * CELL_SIZE, m.GetY() * CELL_SIZE, this);
            }
        }
    }
}
