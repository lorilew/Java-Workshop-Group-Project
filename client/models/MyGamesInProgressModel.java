package client.models;

import sockets.ClientSocketManager;
import sockets.GameListing;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class MyGamesInProgressModel extends java.util.Observable {
	
	ClientSocketManager csm;

	public MyGamesInProgressModel(ClientSocketManager csm) {
		
		this.csm = csm;
		
	}
	
	public void updateMyGamesInProgress() {
		
		try {
			
			GameListing[] games = csm.getMyGames();
		
			setChanged();
			
			notifyObservers(games);
			
		} catch (InterruptedException e) {
			
			
			
		}
		
	}
	
}
