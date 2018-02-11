package Sigurd;

import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays inputs back to the user.
 * @author Anant Shaw
 * Team: Sigurd
 */
@SuppressWarnings("serial")
public class DisplayPanel extends JPanel //class DisplayPanel acts a panel itself
{
    
    JTextArea text1=new JTextArea(); //using JTextArea for the inputs to be scrollable
    JScrollPane pane=new JScrollPane();
    public DisplayPanel() //constructor to set the display panel
    {
        this.setLayout(new BorderLayout(10,10)); 
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        text1.setEditable(false);
        text1.setColumns(30);
        pane=new JScrollPane(text1); //to make the panel scrollable
        add(pane); //adding the Scrollpane to the frame
        sendMessage("   HELLO!   ");
    }
    public static void main(String args[]) throws IOException //main function which takes the input string and sends it to the sendMessage function to be displayed in the panel, for testing
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); //for taking console input as a test
        DisplayPanel panel=new DisplayPanel();
        String st=br.readLine(); 
        for(int i=0;i<100;i++) //testing the code whether multiple inputs can be added to the panel
        {
            panel.sendMessage(st); //calling the sendMessage function
        }
        JFrame frame=new JFrame(); //creating a new JFrame for testing 
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public void sendMessage(String str) //function adds the string to the panel
    {
        text1.append("  " + str + System.getProperty("line.separator")); // adding the list contents to the JTextArea
    }
    
}
