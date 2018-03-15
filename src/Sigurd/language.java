package Sigurd;
import java.util.*;
import java.io.*;
public class language 
{
	static String[] English=new String[100];
	public static void main() throws IOException
	{
		BufferedReader br=null;
		int i=0;
		try {
			br = new BufferedReader(new FileReader("/Users/anantshaw/Downloads/Sigurd-master 3/Assets/english.txt"));
		    String line = br.readLine();
		
		    while (line!=null) 
		    {
		        English[i]=line;
		        line = br.readLine();
		        i++;
		    }
			} 
			finally 
			{
				br.close();
			}
		
	}
	
	
	
}
