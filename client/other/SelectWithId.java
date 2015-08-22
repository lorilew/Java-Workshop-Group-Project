package client.other;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Adds select functionality to the JLabel to be used in the JoinGameScreen and MyGamesInProgressScreen.
 * @author Joaquin de la Sierra
 *
 */

public class SelectWithId extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private boolean active = false;
	
	private String gameId;
	
	public SelectWithId(String id) {
		
		super();
		
		this.setMinimumSize(new Dimension(671, 51));
		
		this.gameId = id;
		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		
		this.active = active;
		
		if (active) {
			
			this.setIcon(new ImageIcon("src/res/gameselected.png"));
			
		} else {
			
			this.setIcon(null);
			
		}
		
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
}
