package Sigurd;

import java.util.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
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
    Style style;
    List<String> logList = new ArrayList<String>();
    
    public DisplayPanel() //constructor to set the display panel
    {
        this.setLayout(new BorderLayout(10,10));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        tpane.setEditable(false);
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        tpane.setParagraphAttributes(set, true);
        tpane.setOpaque(true);
        tpane.setBackground(Color.gray);
        DefaultCaret c=(DefaultCaret)tpane.getCaret();
        c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        pane=new JScrollPane(tpane);
        pane.setPreferredSize( new Dimension(363,632));
        add(pane);
        
        style = text1.addStyle("defualt", null);
        
        SendMessage("GAME BEGINS");
    }
    public static void main(String args[]) throws IOException //main function which takes the input string and sends it to the sendMessage function to be displayed in the panel, for testing
    {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); //for taking console input as a test
        DisplayPanel panel=new DisplayPanel();
        String st=br.readLine();
        for(int i=0;i<100;i++) //testing the code whether multiple inputs can be added to the panel
        {
            panel.SendMessage(st); //calling the sendMessage function
        }
        panel.SendError(st);
        JFrame frame=new JFrame(); //creating a new JFrame for testing
        frame.setBackground(Color.red);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public void SendMessage(String str) //function adds the string to the panel
    {
        try
        {
            text1.insertString(text1.getLength(), str+"\n", style);
        }
        catch(Exception exe)
        {
            System.out.println(exe);
        }
    }
    public void SendDevMessage(String str)
    {
        StyleConstants.setForeground(style, Color.red);
        SendMessage(str);
        StyleConstants.setForeground(style, Color.black);
    }
    
    public void SendError(String str) {
        StyleConstants.setForeground(style, Color.white);
        SendMessage(str);
        StyleConstants.setForeground(style, Color.black);
    }
    public void clearScreen()
    {
        tpane.setText("");
    }
    public void log(String logMessage)
    {
        logList.add(logMessage);
    }
    
}

