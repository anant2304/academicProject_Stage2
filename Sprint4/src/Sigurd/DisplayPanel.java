package Sigurd;

import java.util.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
/**
 * Panel that displays inputs back to the user.
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 * @author Anant Shaw
 */
@SuppressWarnings("serial")
public class DisplayPanel extends JPanel //class DisplayPanel acts a panel itself
{
    JTextPane textPane;
    JScrollPane scrollPane;
    SimpleAttributeSet attribureSet;
    StyledDocument styledDocument;
    Style style;
    List<String> logList;
    
    public DisplayPanel() //constructor to set the display panel
    {
        textPane = new JTextPane();
        attribureSet = new SimpleAttributeSet();
        scrollPane = new JScrollPane(textPane);
        styledDocument = textPane.getStyledDocument();
        logList = new ArrayList<String>();
        
        this.setLayout(new BorderLayout(10,10));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        textPane.setEditable(false);
        StyleConstants.setAlignment(attribureSet, StyleConstants.ALIGN_LEFT);
        textPane.setMargin(new Insets(10, 20, 20, 10));
        
        textPane.setParagraphAttributes(attribureSet, true);
        textPane.setOpaque(true);
        textPane.setBackground(Color.gray);
        ((DefaultCaret) textPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        scrollPane.setPreferredSize( new Dimension(363,632));
        add(scrollPane);
        
        style = styledDocument.addStyle("defualt", null);
        
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
    
    public void SendMessage(String str)
    {
        try{
            styledDocument.insertString(styledDocument.getLength(), str + "\n\n", style);
        } catch(Exception exe){
            exe.printStackTrace();
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
        textPane.setText("");
    }
    
    public void log(String logMessage)
    {
        logList.add(logMessage);
    }
}

