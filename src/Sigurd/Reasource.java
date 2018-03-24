package Sigurd;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Reasource {
	
	private static List<String> characterCach;
	private static List<String> roomCach;
	private static List<String> weaponCach;

	private Reasource() {}
	
	public static List<String> GetCharacterData(){
		if(characterCach == null) {
			characterCach = LoadFileLines("characters");
		}
		
		return characterCach;
	}
	
	public static List<String> GetRoomData(){
		if(roomCach == null) {
			roomCach = LoadFileLines("rooms");
		}
		
		return roomCach;
	}
	
	public static List<String> GetWeaponData(){
		if(weaponCach == null) {
			weaponCach = LoadFileLines("weapons");
		}
		
		return weaponCach;
	}
	
	public static List<String> GetCharacterNames(){
		return GetNamesFromData(GetCharacterData());
	}
	
	public static List<String> GetRoomNames(){
		return GetNamesFromData(GetRoomData());
	}
	
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
			while((string = buffReader.readLine()) != null)
			temp.add(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return temp;
	}
	
	public static void main(String[] args) {
		Game.GetDisplay().SendMessage(Reasource.GetCharacterData().get(0));
		Game.GetDisplay().SendMessage(Reasource.GetRoomData().get(0));
		Game.GetDisplay().SendMessage(Reasource.GetWeaponData().get(0));
		
		Game.GetDisplay().SendMessage(Reasource.GetCharacterNames().get(0));
	}
}
