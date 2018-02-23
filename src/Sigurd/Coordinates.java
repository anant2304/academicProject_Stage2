
package Sigurd;

public class Coordinates {

    private int row, col;
    
    public static final Coordinates UP = new Coordinates(0,-1);
    public static final Coordinates DOWN = new Coordinates(0,1);
    public static final Coordinates LEFT = new Coordinates(-1,0);
    public static final Coordinates RIGHT = new Coordinates(1,0);

    Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    /**
     *  Constructor that takes in a string of the form "row,col".
     */
    Coordinates(String s)
    {
        String[] cooArray = s.trim().split(",");
        if(cooArray.length != 2)
            throw new IllegalArgumentException("Coordnate string must be of format x,y where x and y are ints");
       
        try {
            col = Integer.parseInt(cooArray[0]);
            row = Integer.parseInt(cooArray[1]);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Coordnate string must be of format x,y where x and y are ints");
        }
    }

    public Coordinates add(Coordinates coordinates) {
        return new Coordinates(col + coordinates.getCol(), row + coordinates.getRow());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        
        if(this.getClass().isAssignableFrom(o.getClass())  == false)
            return false;
        
        Coordinates co = (Coordinates) o;
        
        return (co.col == col && co.row == row); 
    }
    
    public int hashCode()
    {
        return col * 30 + row;
        
    }
}
