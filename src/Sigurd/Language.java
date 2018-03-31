package Sigurd;
import java.util.*;

public class Language
{
    static String[] English=new String[100];
    public static void load()
    {
        int i=0;
        try
        {
            Scanner br= new Scanner(Language.class.getResource("/english.txt").openStream());
            String line = br.nextLine();
            
            while (line!=null && br.hasNextLine())
            {
                English[i]=line;
                line = br.nextLine();
                i++;
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
