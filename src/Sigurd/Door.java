package Sigurd;

public class Door extends Coordinates {
    private Coordinates outside;
    private Room room;

    public Door(String s) {
        super(s.substring(1));
        char c = s.charAt(0);
        if (Character.isLetter(c) == false)
            throw new IllegalArgumentException(
                    "Coordnate string must be of format \"dx,y\" " + "where d is a letter, and x and y are integers");
        switch (c) {
        case 'u':
            outside = Coordinates.UP;
            break;
        case 'd':
            outside = Coordinates.DOWN;
            break;
        case 'l':
            outside = Coordinates.LEFT;
            break;
        case 'r':
            outside = Coordinates.RIGHT;
            break;
        default:
            throw new IllegalArgumentException(
                    "Coordnate string must be of format \"dx,y\" " + "where d is a letter, and x and y are integers");
        }
        
        outside = outside.add(this);
    }
    
    public void SetRoom(Room r)
    {
        if(room != null)
            throw new IllegalStateException("This door allready has a room");
        room = r;
    }
    
    public Room GetRoom()
    {
        return room;
    }
    
    public Coordinates GetOutside()
    {
        return outside;
    }
    
    public boolean HasOutside(Coordinates co)
    {
        return (outside.getCol() == co.getCol() && outside.getRow() == co.getRow());
    }

}
