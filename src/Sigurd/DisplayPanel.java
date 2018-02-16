package Sigurd;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.text.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.text.Style;
/**
 * Panel that displays inputs back to the user.
 * @author Anant Shaw
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
@SuppressWarnings("serial")
public class DisplayPanel extends JPanel //class DisplayPanel acts a panel itself
{
    JTextPane tpane=new JTextPane();//using JTextArea for the inputs to be scrollable
    JScrollPane pane=new JScrollPane();
    SimpleAttributeSet set=new SimpleAttributeSet();
    StyledDocument text1=tpane.getStyledDocument();
    public DisplayPanel() //constructor to set the display panel
    {
        this.setLayout(new BorderLayout(10,10)); 
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        this.setBackground(Color.RED);
        tpane.setEditable(false);
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        tpane.setParagraphAttributes(set, true);
        tpane.setOpaque(true);
        tpane.setBackground(Color.gray);
        pane=new JScrollPane(tpane); 
        pane.setPreferredSize( new Dimension(363,632));
        add(pane);
        sendMessage("GAME BEGINS");
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
        frame.setBackground(Color.red);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public void sendMessage(String str) //function adds the string to the panel
    {
        try
        {
            text1.insertString(text1.getLength(), str+"\n\n", null);
        }
        catch(Exception exe) 
        { 
            System.out.println(exe); 
        }
    }
}

