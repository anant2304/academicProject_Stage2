package Sigurd.InputPannel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandPanel {

	public JPanel panel;
	JTextField commandLine;
	JButton enterButton;
	
	public CommandPanel() {
		panel = new JPanel();
		
		AddCommandLine();
		AddEnterButton();
	}
	
	void AddCommandLine() {
		commandLine = new JTextField(20);
		commandLine.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void removeUpdate(DocumentEvent e) {
				FindPartialMatch(commandLine.getText());
			}
			public void insertUpdate(DocumentEvent e) {
				FindPartialMatch(commandLine.getText());
			}
		});
		panel.add(commandLine);
	}
	
	public void ExicuteCommand(String command) {
		switch (command) {
		default :
			System.out.println("Command Not Recodnised");
		} 
	}
	
	void FindPartialMatch(String s) {
		
	}
	
	void AddEnterButton() {
		enterButton = new JButton("Enter");
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExicuteCommand(commandLine.getText());;
				commandLine.setText("");
			}
		});
		panel.add(enterButton);
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
