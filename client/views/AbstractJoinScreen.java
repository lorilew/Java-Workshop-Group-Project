package client.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sockets.GameListing;
import client.Controller;
import client.other.SelectWithId;
import client.other.ViewHelpers;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public abstract class AbstractJoinScreen extends JPanel implements java.util.Observer{

	protected JLabel gamesListBg;
	protected SelectWithId activeSelection;
	protected JButton joinGameButton;
	boolean isJoinGame = true;

	public static final long serialVersionUID = 1L;

	public void generateGameListAndUpdate(GameListing[] listOfGames) {

		//Rebuild the GamesListArea every time it's updated (loaded or there might be a refresh button in the future).

		int counter = 0;

		gamesListBg.removeAll(); //Rebuild completely

		JLabel joinGameMenu = new JLabel(new ImageIcon("src/res/innergametitle.png"));

		Border paddingBorder = BorderFactory.createEmptyBorder(25,25,0,0);

		joinGameMenu.setBorder(BorderFactory.createCompoundBorder(getBorder(),paddingBorder));

		gamesListBg.add(joinGameMenu);

		gamesListBg.add(Box.createRigidArea(new Dimension(339,0)));

		gamesListBg.add(Box.createRigidArea(new Dimension(500,15)));

		if (listOfGames.length > 0) {

			for (GameListing game : listOfGames) {

				counter++;

				if (counter == 6) {

					break; //Right now only six games are supported. I might make it scrollable in the future.

				}

				SelectWithId gameSelected = new SelectWithId(game.getGameID());

				if (counter == 1) {

					gameSelected.setActive(true);

					activeSelection = gameSelected;

				}

				//Add listener to game selected

				gameSelected.addMouseListener(new MouseAdapter() {

					//override
					public void mousePressed(MouseEvent arg0) {

						SelectWithId s = (SelectWithId)arg0.getComponent();

						activeSelection.setActive(false);

						activeSelection = s;

						activeSelection.setActive(true);


					}
				});

				gameSelected.setLayout(new FlowLayout(FlowLayout.LEFT));

				gameSelected.setCursor(new Cursor(Cursor.HAND_CURSOR));

				gameSelected.setPreferredSize(new Dimension(671, 51));

				//gamesListBg.add(Box.createRigidArea(new Dimension (200, 25)));

				gamesListBg.add(gameSelected);

				String[] parts = game.getLastActive().split(" ");
				String first = parts[0];

				//JLabel lastActive = new JLabel(first);

				JLabel label = new JLabel(first + "         " + generateGameTitle(game.getWhitePlayer(), game.getBlackPlayer()));

				label.setForeground(Color.WHITE);

				label.setFont(label.getFont().deriveFont(17.0f));

				Border titleBorder = BorderFactory.createEmptyBorder(10,21,0,0);

				label.setBorder(BorderFactory.createCompoundBorder(getBorder(),titleBorder));

				gameSelected.add(label);

			}

		} else {

			//There are no games.

			joinGameButton.setIcon(null);

		}

	}

	public String generateGameTitle(String player1, String player2) {

		String output = "";

		if (!player1.equals("null")) {

			output = "Game created by " + player1;

		} else {

			output = "Game created by " + player2;

		}

		return output;

	}

	public void addElementsToContainer(JLabel container, Controller controller) {

		//Add the Screen Buttons

		joinGameButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/joingameinner.png", 158, 39);

		container.add(joinGameButton);

		JButton createGameButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/creategameinner.png", 158, 39);

		container.add(createGameButton);

		JButton cancelGameButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/cancel.png", 158, 39);

		//Adding listeners

		joinGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (getIsJoin()) {
				
					controller.joinGame(activeSelection.getGameId());
				
				} else {
					
					controller.resumeGame(activeSelection.getGameId());
					
				}
			}
		});

		createGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.createGame();
			}
		});

		cancelGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.cancel();
			}
		});

		JPanel horizontalButtons = new JPanel();

		horizontalButtons.setOpaque(false);

		horizontalButtons.setLayout(new BoxLayout(horizontalButtons, BoxLayout.LINE_AXIS));

		horizontalButtons.add(Box.createRigidArea(new Dimension(180, 0)));

		horizontalButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		horizontalButtons.add(Box.createHorizontalGlue());

		horizontalButtons.add(joinGameButton);

		horizontalButtons.add(Box.createRigidArea(new Dimension(10, 0)));

		horizontalButtons.add(createGameButton);

		horizontalButtons.add(Box.createRigidArea(new Dimension(10, 0)));

		horizontalButtons.add(cancelGameButton);

		horizontalButtons.setPreferredSize(new Dimension(705, 100));

		container.add(horizontalButtons);

		container.setLayout(new FlowLayout());

	}
	
	public boolean getIsJoin() {
		
		return this.isJoinGame;
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg1 instanceof GameListing[]) {

			generateGameListAndUpdate((GameListing[])arg1);

		} else {

			throw new IllegalArgumentException();

		}

	}

}
