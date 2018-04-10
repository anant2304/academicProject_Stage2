package Sigurd.BoardObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import Sigurd.Coordinates;
import Sigurd.Room;

/**
 * Objects on the screen that are controlled by players
 * Also referred to as characters
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Adrian Wennberg, Peter Major
 */
public class PlayerObject extends BoardObject {

    private boolean hasPlayer = false;
    private boolean hasBeenMoved = false;
    
    
    public PlayerObject(Coordinates co, Color c, String _objectName) {
        super(co, (Image) null, _objectName);

        BufferedImage image = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(c);
        g2d.fillOval(0, 0, 23, 23);

        SetImage(image);
    }
    
    public boolean HasPlayer()
    {
        return hasPlayer;
    }
    
    public void SetPlayer()
    {
        if(HasPlayer())
            throw new IllegalStateException(GetObjectName() + " allready has a player");
        hasPlayer = true;
    }
    
    public boolean HasMovedAfterLastTurn()
    {
        return hasBeenMoved;
    }
    
    public void ResetHasMoved()
    {
        hasBeenMoved = false;
    }
    
    @Override
    public void MoveToRoom(Room r) {
        super.MoveToRoom(r);
        hasBeenMoved = true;
    }

    /**
     * moves the currently controlled player in the given direction
     */
    public void Move(Coordinates c) {
        MoveTo(GetCoordinates().Add(c));
    }
}
