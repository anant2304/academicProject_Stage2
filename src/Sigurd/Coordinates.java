
package Sigurd;

public class Coordinates {

    private int row, col;

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
            row = Integer.parseInt(cooArray[0]);
            col = Integer.parseInt(cooArray[1]);
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
}
