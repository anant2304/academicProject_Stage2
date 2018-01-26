package Sigurd;

import java.io.*;
import java.util.Scanner;

/**
 * 
 * @author Adrian Wennberg
 *
 */
public class Board {
    private boolean[][] boardArray;
    
    /**
     * Test main.
     */
    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b);
    }
    
    public Board() {
        LoadBoard();
    }

    /**
     * Loads the board layout from a file and creates the boardArray.
     */
    private void LoadBoard(){
        boardArray = new boolean[25][24];
        
        try(Scanner layoutReader = new Scanner(new File("Assets/Layout.txt"), "UTF-8"))
        {
            int row = 0;
            while(layoutReader.hasNext())
            {
                String line = layoutReader.next();
                for(int i = 0; i < line.length(); i++)
                {
                    boardArray[row][i] = (line.charAt(i) == '1');
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
    }
    /**
     * Prints the contents of the board array in 1s and 0s.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < boardArray.length; i++)
        {
            for (int j = 0; j < boardArray[i].length; j++) {
                str.append( boardArray[i][j]?'1':'0');
            }
            str.append('\n');
        }
        return str.toString();
    }
    
    
}
