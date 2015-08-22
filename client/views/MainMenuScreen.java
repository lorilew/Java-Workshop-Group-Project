package client.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Controller;
import client.other.ViewHelpers;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class MainMenuScreen extends JPanel{
	
	public static final long serialVersionUID = 1L;
	
	public MainMenuScreen(final Controller controller) {
		
		JPanel panel = new JPanel();
		
		JPanel contentArea = new JPanel();
		
		//contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.PAGE_AXIS));
		
		panel.setSize(766,577);
		
		JLabel container = new JLabel(new ImageIcon("src/res/bg.jpg"));
		panel.add(container);
		
		//container.setSize(766,577);
		
		//Set initial top space of 150 pixels to match login height.
		
		contentArea.add(Box.createRigidArea(new Dimension(0,35)));
		
		//Create the main menu buttons from method.
		JLabel logo = new JLabel(new ImageIcon("src/res/logo.png"));
		
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
				
		contentArea.add(logo);	
		
		contentArea.add(Box.createRigidArea(new Dimension(0,70)));
		
		//Create the main menu buttons from method.
		JButton createGameButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/creategame.png", 400, 72);
		
		contentArea.add(createGameButton);	
		
		//Add space of 30 pixels between buttons.
		
		contentArea.add(Box.createRigidArea(new Dimension(0,40)));
		
		JButton joinGameButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/joingame.png", 400, 72);
		
		contentArea.add(joinGameButton);	
		
		//Add space of 30 pixels between buttons.
		
		contentArea.add(Box.createRigidArea(new Dimension(0,40)));
		
		JButton myGamesInProgressButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/gamesinprogress.png", 400, 72);
		
		contentArea.add(myGamesInProgressButton);

		contentArea.setOpaque(false);
		
		contentArea.setPreferredSize(new Dimension(766,577));
		
		container.add(contentArea);
		
		contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
		
		container.setLayout(new FlowLayout());
		
		//Listeners
		
		createGameButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.createGame();
	        }
	    });
		
		joinGameButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.joinGameButton();
	        }
	    });
		
		myGamesInProgressButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.myGamesInProgress();
	        }
	    });
		
		this.add(container);
		
	}
	
}
