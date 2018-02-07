package Sigurd;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class DisplayPanel extends JPanel //class DisplayPanel acts a panel itself
{
    
    JTextArea text1=new JTextArea(); //using JTextArea for the inputs to be scrollable
    JScrollPane pane=new JScrollPane();
    ArrayList<String> list=new ArrayList<String>();
    public static void main(String args[]) throws IOException //the user calls the main function it calls the add function, which stores the input and in return calls the disp function which sends the output to the display panel
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); //for taking console input as a test
        String st=br.readLine(); //for testing
        DisplayPanel panel=new DisplayPanel();
        panel.setLayout(new BorderLayout(10,10)); //specifications of the panel
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        panel.add(st);
        JFrame frame=new JFrame();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public void add(String str) //this function takes the string input and adds it to a file, and then creates and ArrayList,and then passes it to the function disp.
    {
        try
        {
            String move="moveList.txt"; 
            
            if(str.equals("E")||str.equals("e"))
            {
                FileWriter fileWriter=new FileWriter(move); //to write to the file, while deleting the contents of the file
                fileWriter.write("End of game"+"\n"); //adding the string to the file
                fileWriter.close();
                Scanner file=new Scanner(new File("moveList.txt"));
                while (file.hasNext()) 
                {
                    list.add(new String(file.next())); //adds the file contents to the list
                }
                disp(list);
                FileWriter fileWriter2=new FileWriter(move); //to write to the file, while deleting the contents of the file
                fileWriter2.write(""); //emptying the file
                fileWriter.close();
                
            }
            else
            {
                FileWriter fileWriter=new FileWriter(move, true); //to write to the file, while not deleting the contents of the file
                fileWriter.write(str+"\n"); //adding the string to the file
                fileWriter.close();
                Scanner file=new Scanner(new File("moveList.txt"));
                while (file.hasNext()) 
                {
                    list.add(new String(file.next())); //adds the file contents to the list
                }
                disp(list);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public void disp(ArrayList<String> list2) //this function displays the contents of the Arraylist onto the panel
    {
        for(String a:list2) 
        {
            text1.append(a+System.getProperty("line.separator")); // adding the list contents to the JTextArea
        }
        text1.append(System.getProperty("line.separator"));
        text1.setSize(200,200);
        pane=new JScrollPane(text1); //to make the panel scrollable
        add(pane); //adding the Scrollpane to the frame
    }
}
