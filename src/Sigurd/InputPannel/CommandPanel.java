package Sigurd.InputPannel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class CommandPanel {

	public JPanel panel;
	JTextField commandLine;
	JButton enterButton;
	Commands commands;
	
	public CommandPanel() {
		panel = new JPanel();
		commands = new Commands();
		
		AddCommandLine();
		AddEnterButton();
	}
	
	void AddCommandLine() {
		commandLine = new JTextField(20);
		
		//action listener for when user presses return key
		commandLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ExicuteCommand(commandLine.getText());
			}
		});
		
		//adding listener for when someone types something into the text box
		commandLine.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}
			
			public void removeUpdate(DocumentEvent e) {
				FindPartialMatch(commandLine.getText());
			}
			
			public void insertUpdate(DocumentEvent e) {
				FindPartialMatch(commandLine.getText());
			}
		});
		
		panel.add(commandLine);
	}
	
	void AddEnterButton() {
		enterButton = new JButton("Enter");
		
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExicuteCommand(commandLine.getText());
			}
		});
		
		panel.add(enterButton);
	}
	
	void FindPartialMatch(String part) {
		//check for enmpty string as this would return the full commmand list
		if(part.length() == 0) return;
		
		LinkedList<String> list = commands.FindCommands(part);
		for(String s : list){
			System.out.println(s);//for debug reasons
			//TODO : put this on screen 
		}
	}
	
	public void ExicuteCommand(String command) {
		switch (command) {
		default :
			System.out.println("Command Not Recodnised");
		} 
		commandLine.setText("");
	}
	
	public static void main(String[] args) {
		CommandPanel cpannel = new CommandPanel();
		
		JFrame frame = new JFrame();
		frame.add(cpannel.panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
