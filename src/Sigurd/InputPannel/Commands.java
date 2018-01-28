package Sigurd.InputPannel;

import java.io.*;
import java.util.*;


public class Commands {
	
	ArrayList<String> list = new ArrayList<String>();
	
	public Commands(){
		GetCommands();
		}
	
	void GetCommands() {
		try {
			File f = new File("src/Sigurd/InputPannel/CommandList.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			//loop through file and load each line in as a command
			//TODO : add formating so it can be broken into commands and description
			String line;
			while((line = br.readLine()) != null){
				list.add(line);
			}
			
			fr.close();
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public LinkedList<String> FindCommands(String part){
		//check for empty string as this would return the full command list, instead return empty list
		if(part.length() == 0) return new LinkedList<String>();
		
		LinkedList<String> temp = new LinkedList<String>();
		for(String s: list) {
			if(s.contains(part)) temp.add(s);
		}
		
		return temp;
	}
	
	public static void main(String args[]) {
		Commands c = new Commands();
	}
	
}
