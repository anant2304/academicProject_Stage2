
package Sigurd;

/**
 * Coordinates on the board
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Adrian Wennberg
 */

public class Coordinates {

    private final int row, col;
    
    public static final Coordinates UP = new Coordinates(0,-1);
    public static final Coordinates DOWN = new Coordinates(0,1);
    public static final Coordinates LEFT = new Coordinates(-1,0);
    public static final Coordinates RIGHT = new Coordinates(1,0);

    
    Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    /**
     * Constructor that takes in a string of the form "row,col" i.e. "3,4".
     */
    Coordinates(String s)
    {
        String[] coArray = s.trim().split(",");
        if(coArray.length != 2)
            throw new IllegalArgumentException("Coordnate string must be of format x,y where x and y are ints");
       
        try {
            col = Integer.parseInt(coArray[0]);
            row = Integer.parseInt(coArray[1]);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Coordnate string must be of format x,y where x and y are ints");
        }
    }

    public Coordinates Add(Coordinates coordinates) {
        return new Coordinates(col + coordinates.GetCol(), row + coordinates.GetRow());
    }

    public int GetRow() {
        return row;
    }

    public int GetCol() {
        return col;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        
        if(this.getClass().isAssignableFrom(o.getClass())  == false)
            return false;
        
        Coordinates co = (Coordinates) o;
        
        return (co.col == col && co.row == row); 
    }

    @Override
    public int hashCode()
    {
        return col * 30 + row;
        
    }
}
