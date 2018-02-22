package Sigurd;

public class Door extends Coordinates {
    private Coordinates outside;

    public Door(String s) {
        super(s.substring(1));
        char c = s.charAt(0);
        if (Character.isLetter(c) == false)
            throw new IllegalArgumentException(
                    "Coordnate string must be of format dx,y " + "where d is a letter, and x and y are ints");
        switch (c) {
        case 'u':
            outside = new Coordinates(0, -1);
            break;
        case 'd':
            outside = new Coordinates(0, 1);
            break;
        case 'l':
            outside = new Coordinates(-1, 0);
            break;
        case 'r':
            outside = new Coordinates(1, 0);
            break;
        default:
            throw new IllegalArgumentException(
                    "Coordnate string must be of format dx,y " + "where d is a letter, and x and y are ints");
        }
        
        outside = outside.add(this);
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
