package Sigurd.BoardObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import Sigurd.Coordinates;
import Sigurd.Game;

/**
 * Players that are displayed to the screen.
 * @author Peter Major
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class PlayerObject extends BoardObject {
    
    String playerName;
    
    /**
     * @Summary Creates a player represented on the board with a colored circle
     * @param _x
     * @param _y
     * @param c
     * @param _playerName
     */
    public PlayerObject(Coordinates co, Color c, String _objectName, String _playerName) {
        super(co,(Image)null,_objectName);
        
        BufferedImage image = new BufferedImage(23, 23, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(c);
        g2d.fillOval(0, 0, 23, 23);
        
        SetImage(image);
        playerName=_playerName;
        Game.GetDisplay().sendMessage(playerName);
    }
    
    public String getPlayerName()
    {
        return playerName;
    }
    
    /**
     * moves the currently controlled player in the given direction
     */
    public void Move(Coordinates c) {
        MoveTo(GetCoordinates().add(c));
    }
}
