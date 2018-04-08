package Sigurd;

/**
 * Door position on the board
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Adrian Wennberg
 */

public class Door extends Coordinates {
    private Coordinates outsidePosition;
    private Room room;

    public Door(String s) {
        super(s.substring(1));
        char c = s.charAt(0);
        if (Character.isLetter(c) == false)
            throw new IllegalArgumentException(
                    "Coordnate string must be of format \"dx,y\" " + "where d is a letter, and x and y are integers");
        switch (c) {
        case 'u':
            outsidePosition = Coordinates.UP;
            break;
        case 'd':
            outsidePosition = Coordinates.DOWN;
            break;
        case 'l':
            outsidePosition = Coordinates.LEFT;
            break;
        case 'r':
            outsidePosition = Coordinates.RIGHT;
            break;
        default:
            throw new IllegalArgumentException(
                    "Coordnate string must be of format \"dx,y\" " + "where d is a letter, and x and y are integers");
        }
        
        outsidePosition = outsidePosition.Add(this);
    }
    
    public void SetRoom(Room r)
    {
        if(room != null)
            throw new IllegalStateException("This door allready has a room");
        room = r;
    }
    
    public Room GetRoom()
    {
        if(room == null)
            throw new IllegalStateException("This door does not have a room");
        return room;
    }
    
    public Coordinates GetOutside()
    {
        return outsidePosition;
    }
}
