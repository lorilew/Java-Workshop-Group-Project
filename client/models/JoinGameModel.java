package client.models;

import sockets.ClientSocketManager;
import sockets.GameListing;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class JoinGameModel extends java.util.Observable {
	
	ClientSocketManager csm;

	public JoinGameModel(ClientSocketManager csm) {
		
		this.csm = csm;
		
	}
	
	public void updateJoinGame() {
		
		try {
			
			GameListing[] games = csm.getOpenGames(); //Enable when server ready.
			
			//GameListing[] games = new GameListing[]{new GameListing("3", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today"), new GameListing("5", "White", "Black", "Today")};
			
			//GameListing[] games = new GameListing[]{};
		
			setChanged();
			
			notifyObservers(games);
			
		} catch (InterruptedException e) {
			
			
			
		}
		
	}
	
}
