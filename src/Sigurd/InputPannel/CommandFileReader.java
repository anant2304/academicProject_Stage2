package Sigurd.InputPannel;

import java.io.*;


public class CommandFileReader {
	
	File f = new File("CommandList");
	FileReader fr;
	BufferedReader br = new BufferedReader(fr);
	StringBuffer sb = new StringBuffer();
	
	public CommandFileReader(){
		
		try {
			fr = new FileReader(f);
			
			fr.close();
		}
		catch(IOException e){}
		}
	
	void ReadFile() {
		
	}
	
}
