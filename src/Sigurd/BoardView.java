package Sigurd;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardView extends JPanel {

    private static final String BOARD_PATH = "Assets/cluedo board.jpg";
    private static final int CORNER_X = 42;
    private static final int CORNER_Y = 23;
    private static final int SQUARE_WIDTH = 23;
    

    private BufferedImage boardImage;
    private int prefH;
    private int prefW;
    private List<Shape> shapes;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BoardView view = new BoardView();
        frame.add(view);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public BoardView() {
        shapes = new LinkedList<Shape>();
        try {
            boardImage = ImageIO.read(new File(BOARD_PATH));
            prefW = boardImage.getWidth();
            prefH = boardImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(prefW, prefH);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(boardImage, 0, 0, this);
    }
}
