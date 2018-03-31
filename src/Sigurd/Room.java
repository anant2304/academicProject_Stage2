package Sigurd;

import Sigurd.BoardObjects.*;

/**
 * 
 * @author Adrian
 *
 *         Team: Sigurd Student Numbers: 16751195, 16202907, 16375246
 */

public class Room {
    private static final String[] PLAYER_OFFSETS = { "1,0", "1,-1", "2,-1", "2,0", "2,1", "1,1" };
    private static final String[] WEAPON_OFFSETS = { "-1,0", "-1,-1", "-2,-1", "-2,0", "-2,1", "-1,1" };
    private Door[] doors;
    private Coordinates roomCentrePos;
    private PlayerObject[] players;
    private WeaponObject[] weapons;
    private String name;
    private Room passageRoom;

    public Room(String name, Door[] doors, Coordinates roomCentrePos) {
        this.name = name;
        this.doors = doors;
        this.roomCentrePos = roomCentrePos;
        players = new PlayerObject[6];
        weapons = new WeaponObject[6];

        for (Door d : this.doors)
            d.SetRoom(this);
    }

    public String GetName() {
        return name;
    }

    public void SetPassageRoom(Room r) {
        if (passageRoom != null)
            throw new IllegalStateException("There is allready a passage from this room: " + name);

        passageRoom = r;
    }

    public boolean HasPassage() {
        return passageRoom != null;
    }

    public Room GetPassageRoom() {
        if (HasPassage() == false)
            throw new IllegalStateException("Getting passage of room with no passage");
        return passageRoom;
    }

    private int GetPlayerIndex(PlayerObject p) {
        int i = -1;
        while (!p.equals(players[++i]) && i < 5)
            ;
        if (!p.equals(players[i]))
            throw new IllegalArgumentException(
                    "Trying to get player: " + p.GetObjectName() + " while player not in room: " + name);

        return i;
    }

    private int GetWeaponIndex(WeaponObject w) {
        int i = -1;
        while (!w.equals(weapons[++i]) && i < 5)
            ;
        if (!w.equals(weapons[i]))
            throw new IllegalArgumentException(
                    "Trying to get weapon: " + w.GetObjectName() + " while weapon not in room: " + name);

        return i;
    }

    public void AddObject(BoardObject o) {
        BoardObject[] arr;
        if (o.getClass() == PlayerObject.class)
            arr = players;
        else if (o.getClass() == WeaponObject.class)
            arr = weapons;
        else {
            throw new IllegalArgumentException("Argument not player or weapon");
        }
        
        int i = -1;
        while (arr[++i] != null && i < 5) ;

        if (arr[i] != null)
            throw new IllegalStateException("More than 6 of same object in room");
        arr[i] = o;
            
    }

    public void RemoveObject(BoardObject o) {
        if (o.getClass() == WeaponObject.class) {
            weapons[GetWeaponIndex((WeaponObject) o)] = null;
        } else if (o.getClass() == PlayerObject.class) {
            players[GetPlayerIndex((PlayerObject) o)] = null;
        } else {
            throw new IllegalArgumentException("Argument not player or weapon");
        }
    }

    public Coordinates GetObjectPosition(BoardObject o) {
        if (o.getClass() == PlayerObject.class) {
            return roomCentrePos.add(new Coordinates(PLAYER_OFFSETS[GetPlayerIndex((PlayerObject) o)]));
        } else if (o.getClass() == WeaponObject.class) {
            return roomCentrePos.add(new Coordinates(WEAPON_OFFSETS[GetWeaponIndex((WeaponObject) o)]));
        } else {
            throw new IllegalArgumentException("Argument not player or weapon");
        }
    }

    public Door[] GetDoors() {
        return doors;
    }
}
