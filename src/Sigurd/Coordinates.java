package Sigurd;

import java.util.zip.DataFormatException;

public class Coordinates {

	public final static Coordinates UP 	= 	new	Coordinates	(0, -1);
	public final static Coordinates DOWN = 	new	Coordinates	(0, 1);
	public final static Coordinates RIGHT = new Coordinates	(1, 0);
	public final static Coordinates LEFT = 	new Coordinates	(-1, 0);
	
    private int row, col;

    Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
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
    
    public static Coordinates parse(String s) throws DataFormatException
    {
        String[] cooArray = s.trim().split(",");
        if(cooArray.length != 2)
            throw new DataFormatException("Coordnate string must be of format x,y where x and y are ints");
       
        try {
            return new Coordinates(Integer.parseInt(cooArray[0]),
                                   Integer.parseInt(cooArray[1]));
        }
        catch(NumberFormatException e)
        {
            throw new DataFormatException("Coordnate string must be of format x,y where x and y are ints");
        }
    }
}
