package client.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;




import sockets.GameListing;
import client.Controller;
import client.other.ViewHelpers;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class ChessBoardScreen extends JPanel implements java.util.Observer{
	
	JLabel topIcon;
    JLabel bottomIcon;
	
	public static final long serialVersionUID = 1L;

	public ChessBoardScreen(final Controller controller, ChessBoard chessBoard) {
		
		JPanel panel = new JPanel();

		JLabel container = new JLabel(new ImageIcon("src/res/bg.jpg"));
		
		panel.setSize(766,577);
		
		panel.setLayout(new BorderLayout());
		
		panel.add(container);
		
		//Add the Screen Components

		container.setLayout(new FlowLayout());
		
		//Add space to horizontally center.
		container.add(Box.createRigidArea(new Dimension(300, 45))); //Large width so it will stack above.
		
        JPanel horizontalLayout = new JPanel();
        horizontalLayout.setOpaque(false);
        horizontalLayout.setLayout(new BoxLayout(horizontalLayout, BoxLayout.LINE_AXIS));

        JPanel chessBoardTurnIcons = new JPanel();
        chessBoardTurnIcons.setPreferredSize(new Dimension(25, 400));
        chessBoardTurnIcons.setLayout(new BoxLayout(chessBoardTurnIcons, BoxLayout.PAGE_AXIS));
        
        ImageIcon icon = new ImageIcon("src/res/redicon.png");
        
        topIcon = new JLabel(icon);
        bottomIcon = new JLabel(icon);
        
        chessBoardTurnIcons.add(topIcon);
        chessBoardTurnIcons.add(Box.createRigidArea(new Dimension(0, 400)));
        chessBoardTurnIcons.add(bottomIcon);
        chessBoardTurnIcons.setOpaque(false);
        
        horizontalLayout.add(chessBoardTurnIcons);
        
        horizontalLayout.add(Box.createRigidArea(new Dimension(10, 0)));
        
        chessBoard.setOpaque(false); //Remove border from chessboard. Not sure if I prefer with or without border.
        
        horizontalLayout.add(chessBoard); //Add the chess board.
        
        horizontalLayout.add(Box.createRigidArea(new Dimension(20, 0)));
        
		JLabel rightPanel = new JLabel(new ImageIcon("src/res/rightpanel.png"));
		horizontalLayout.add(rightPanel);
		
		horizontalLayout.add(Box.createRigidArea(new Dimension(20, 0)));
		
		rightPanel.setLayout(new FlowLayout());
		
		rightPanel.add(Box.createRigidArea(new Dimension(170, 15)));
		
		JButton resignButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/resign.png", 158, 39);
		
		JButton offerDrawButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/offerdraw.png", 158, 39);
		
		JButton flipBoardButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/flipboard.png", 158, 39);
		
		JButton backButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/backbutton.png", 85, 33);
		
		offerDrawButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.offerDraw();
	        }
	    });
		
		resignButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.resign();
	        }
	    });
		
		flipBoardButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.flipBoard();
	        }
	    });
		
		backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            controller.back();
	        }
	    });
		
		JPanel capturedPieces = new JPanel();
		
		capturedPieces.add(Box.createRigidArea(new Dimension(170, 120)));
		
		capturedPieces.setOpaque(false);
		
		rightPanel.add(resignButton);
		
		rightPanel.add(Box.createRigidArea(new Dimension(170, 8)));
		
		rightPanel.add(offerDrawButton);
		
		rightPanel.add(Box.createRigidArea(new Dimension(170, 8)));

		rightPanel.add(flipBoardButton);
		
		container.add(Box.createRigidArea(new Dimension(25, 5)));
		
		rightPanel.add(capturedPieces); //I might add a box with all captured pieces here in the future.
		
		rightPanel.add(Box.createRigidArea(new Dimension(170, 95)));
		
		rightPanel.add(backButton);
		
		rightPanel.add(Box.createRigidArea(new Dimension(70, 15)));
		
		container.add(horizontalLayout);
		
		this.add(container);
		
	}

	@Override
	public void update(Observable o, Object arg) {

		ImageIcon icon = new ImageIcon("src/res/redicon.png");
		
		if (arg instanceof String) {

			if ((String)arg == "top") {
				
				topIcon.setIcon(icon);
				bottomIcon.setIcon(null);
				
			} else if ((String)arg == "bottom") {
				
				topIcon.setIcon(null);
				bottomIcon.setIcon(icon);
				
			} else {
				
				topIcon.setIcon(null);
				bottomIcon.setIcon(null);
				
			}
			
			repaint();
			revalidate();
		
			
		} else {

			

		}
		
	}
	
	
}
