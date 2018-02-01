package Sigurd.InputPannel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Peter Major
 *	
 * @Summary contains a JPanel named panel that contains the command line
 * 
 */
public class CommandPanel {

	public JPanel panel;//main panel that will be extracted
	JTextField commandLine;
	JButton enterButton;

	/**
	 * @Summary Constructor 
	 */
	public CommandPanel() {
		panel = new JPanel();
		
		AddCommandLine();
		AddEnterButton();
	}
	
	/**
	 * @Summary adds the command line to the main panel
	 */
	void AddCommandLine() {
		commandLine = new JTextField(20);
		
		//action listener for when user presses return key
		commandLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Command.Exicute(commandLine.getText());
				commandLine.setText("");
			}
		});
		
		//adding listener for when someone types something into the text box
		commandLine.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {}
			public void removeUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {}
		});
		
		panel.add(commandLine);
	}
	
	/**
	 * @Summary adds the enter button to the main panel
	 */
	void AddEnterButton() {
		enterButton = new JButton("Enter");
		
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Command.Exicute(commandLine.getText());
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
