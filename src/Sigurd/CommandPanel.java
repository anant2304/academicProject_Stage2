package Sigurd;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Peter Major
 * @Summary contains a JPanel that contains the command line
 * Team: Sigurd
 * Student Numbers:
 * 16751195, 16202907, 16375246
 */
public class CommandPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JTextField commandLine;
	JButton enterButton;
	Controler controler;

	public CommandPanel() {
		controler = Game.GetControlerInstance();
		commandLine.setBackground(Color.gray); //adding color to command panel
        	commandLine.setOpaque(true);
		AddCommandLine();
		AddEnterButton();
	}
	
	/**
	 * @Summary adds the command line to the main panel
	 */
	void AddCommandLine() {
		commandLine = new JTextField(20);
		
		//action listener for when user presses return key
		commandLine.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							controler.ExicuteCommand(commandLine.getText());
							commandLine.setText("");
							}
				}
		);
		
		add(commandLine);
	}
	
	/**
	 * @Summary adds the enter button to the main panel
	 */
	void AddEnterButton() {
		enterButton = new JButton("Enter");
		
		//when button is pressed
		enterButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						controler.ExicuteCommand(commandLine.getText());
						commandLine.setText("");
					}
				}
		);
		
		add(enterButton);
	}
	
	/**
	 * testing main
	 * @param args
	 */
	public static void main(String[] args) {
		CommandPanel cpannel = new CommandPanel();
		
		JFrame frame = new JFrame();
		frame.add(cpannel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
