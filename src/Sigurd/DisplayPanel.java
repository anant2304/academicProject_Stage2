import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class DisplayPanel
{
    public static void main(String args[])
    {
    }
    public static void add(String str) //this function takes the string input and adds it to a file, and then creates and ArrayList,and then passes it to the function disp.
    {
        try
        {
            String move="moveList.txt"; 
            FileWriter fileWriter=new FileWriter(move, true);
            fileWriter.write(str+"\n");
            fileWriter.close();
            Scanner file=new Scanner(new File("moveList.txt"));
            ArrayList<String> list=new ArrayList<String>();
        
            while (file.hasNext()) 
            {
                list.add(new String(file.next())); //adds the text to the list
            }
            disp(list);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public static void disp(ArrayList<String> list2) //this function displays the contents of the arraylist onto the panel
    {
        JFrame frame=new JFrame("Moves");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout(50,50));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        JTextArea text1=new JTextArea();
        for(String a:list2)
        {
            text1.append(a+System.getProperty("line.separator"));
        }
        text1.append(System.getProperty("line.separator"));
        text1.setSize(200,200);
        panel.add(text1,BorderLayout.LINE_START);
        frame.setContentPane(panel);      
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }
}
