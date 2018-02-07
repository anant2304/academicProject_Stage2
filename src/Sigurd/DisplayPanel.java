package Sigurd;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class DisplayPanel extends JPanel
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
    public static void disp(ArrayList<String> list2) //this function displays the contents of the Arraylist onto the panel
    {
        
        
        DisplayPanel panel=new DisplayPanel();
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
        JScrollPane pane=new JScrollPane(panel);
        JPanel panel2=new JPanel();
        panel2.add(pane);
        //peter.setContentPane(panel2);      
        
    }
}

