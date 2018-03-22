package Sigurd;
import java.util.*;
import java.io.*;
public class language
{
    static String[] English=new String[100];
    public static void main() throws IOException, ClassNotFoundException
    {
        int i=0;
        try
        {
            Scanner br= new Scanner(language.class.getResource("/english.txt").openStream());
            String line = br.nextLine();
            
            while (line!=null && br.hasNextLine())
            {
                English[i]=line;
                line = br.nextLine();
                i++;
            }
            
            
            br.close();
        }
        finally {
            
        }
        
        
    }
    
    
    
}
