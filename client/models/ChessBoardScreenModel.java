package client.models;

import java.util.Observable;

public class ChessBoardScreenModel extends Observable {

	public void handleAddChessBoardTurnIcon(boolean whiteTurn, boolean boardFlipped) {
		
		String iconLocation = "";
		
		if ((whiteTurn && !boardFlipped) || (!whiteTurn && boardFlipped)) {
			
			iconLocation = "bottom";
			
		} else {
			
			iconLocation = "top";
			
		}
		
		setChanged();
		
		notifyObservers(iconLocation);
		
	}
	
}
