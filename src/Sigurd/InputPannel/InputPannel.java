package Sigurd.InputPannel;

import javax.swing.*;
import java.awt.*;

public enum act {};

public class InputPannel {

	JPanel panel;
	
	public InputPannel() {
		panel = new JPanel(new GridBagLayout());
		FlowLayout LM = new FlowLayout();
		panel.setLayout(LM);
		panel.setSize(600, 300);
	
	}
	
	public void AddButton(String text){		
		JButton button = new JButton(text);
		
		panel.add(button);
	}
	
	
	public static void main(String[] args) {
		InputPannel ipannel = new InputPannel();
		ipannel.AddButton("Hi");
		ipannel.AddButton("Hello");
		
		
		JFrame frame = new JFrame();
		frame.add(ipannel.panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
}
