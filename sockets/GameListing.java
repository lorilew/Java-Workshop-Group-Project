package sockets;

public class GameListing {
	
	String game_id;
	String white;
	String black;
	String lastActive;
	
	/**
	 * Standard constructor
	 * @param game_id the game ID as a String
	 * @param white the white player username as a String
	 * @param black the black player username as a String
	 * @param lastActive the time last active as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public GameListing(String game_id, String white, String black, String lastActive)
	{
		this.game_id = game_id;
		this.white = white;
		this.black = black;
		this.lastActive = lastActive;
	}
	
	/**
	 * Returns the game ID
	 * @return the game ID as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public String getGameID()
	{
		return this.game_id;
	}
	
	/**
	 * Returns the white player's username
	 * @return the white player's username as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public String getWhitePlayer()
	{
		return this.white;
	}
	
	/**
	 * Returns the black player's username
	 * @return the black player's username as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public String getBlackPlayer()
	{
		return this.black;
	}
	
	/**
	 * Returns the time the game was last active
	 * @return the time the game was last active as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public String getLastActive()
	{
		return this.lastActive;
	}

}
