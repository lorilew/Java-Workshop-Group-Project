package client.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Controller;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class JoinGameScreen extends AbstractJoinScreen{

	private static final long serialVersionUID = 1L;

	public JoinGameScreen (final Controller controller) {
		
		JPanel panel = new JPanel();
		
		JLabel container = new JLabel(new ImageIcon("src/res/bg.jpg"));
		
		panel.setSize(766,577);
		
		panel.setLayout(new BorderLayout());
		
		panel.add(container);
		
		//Add the Screen Components
		
		container.add(Box.createRigidArea(new Dimension(0, 80)));
		
		JLabel title = new JLabel(new ImageIcon("src/res/joingametitle.png"));
		
		container.add(title);
		
		container.add(Box.createRigidArea(new Dimension(450, 0)));
		
		gamesListBg = new JLabel(new ImageIcon("src/res/gameslistbg.png"));
		
		gamesListBg.setLayout(new FlowLayout());
			
		container.add(gamesListBg);
		
		addElementsToContainer(container, controller);
		
		this.add(container);
	
	}
	
}
