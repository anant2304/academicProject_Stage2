package Sigurd;

import java.io.*;
import java.util.*;

/** Team: Sigurd
* Student Numbers:
* 16751195, 16202907, 16375246
*/

public class Reasource {
	
	private static List<String> characterCach;
	private static List<String> roomCach;
	private static List<String> weaponCach;
	public static final String BASEMENTNAME = "basement";

	private Reasource() {}

	/**
	 * @return a list of lines from the character txt 
	 */
	public static List<String> GetCharacterData(){
		if(characterCach == null) {
			characterCach = LoadFileLines("characters");
		}
		
		return characterCach;
	}
	
	/**
	 * @return a list of lines from the room txt 
	 */
	public static List<String> GetRoomData(){
		if(roomCach == null) {
			roomCach = LoadFileLines("rooms");
		}
		
		return roomCach;
	}
	
	/**
	 * @return a list of lines from the weapon txt 
	 */
	public static List<String> GetWeaponData(){
		if(weaponCach == null) {
			weaponCach = LoadFileLines("weapons");
		}
		
		return weaponCach;
	}
	
	/**
	 * @return a list of names of all the characters in the characters txt 
	 */
	public static List<String> GetCharacterNames(){
		return GetNamesFromData(GetCharacterData());
	}
	
	/**
	 * @return a list of names of all the rooms in the rooms txt 
	 */
	public static List<String> GetRoomNames(){
	    List<String> roomNames = GetNamesFromData(GetRoomData());
	    for (String string : roomNames) {
            String[] roomNameParts = string.split("_");
            string = roomNameParts[0];
            for (int i = 1; i < roomNameParts.length; i++)
                string += " " + roomNameParts[i];
        }
		return roomNames;
	}
	
	/**
	 * @return a list of names of all the weapons in the weapons txt 
	 */
	public static List<String> GetWeaponNames(){
		return GetNamesFromData(GetWeaponData());
	}
	
	private static List<String> GetNamesFromData(List<String> data){
		List<String> list = new ArrayList<String>();
		
		for(String s : data) {
			String[] temp = s.split("\\s+");
			list.add(temp[0]);
		}
		
		return list;
	}

	private static List<String> LoadFileLines(String fileName) {
		List<String> temp = new ArrayList<String>();
		
		Reader reader = new InputStreamReader(
				Reasource.class.getResourceAsStream("/" + fileName + ".txt"));
		BufferedReader buffReader = new BufferedReader(reader);
		
		String string;
		
		try {
			while((string = buffReader.readLine()) != null) {
			temp.add(string.toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return temp;
	}
	
	public static void main(String[] args) {
		System.out.println(Reasource.GetCharacterData().get(0));
		System.out.println(Reasource.GetRoomData().get(0));
		System.out.println(Reasource.GetWeaponData().get(0));
		
		System.out.println(Reasource.GetCharacterNames().get(0));
	}
}
