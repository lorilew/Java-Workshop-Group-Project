package database;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DatabaseManagerInterface {
	
	/**
	 * Given a Player's username and password, the databaseManager will
	 * query the database for the username. 
	 * If the username does not exist, a new entry will be made with given
	 * username and password. Then returns the info as a Player object.
	 * If username and password do not match the method will return a null.
	 * If username and password do match the method will update the last_active
	 * variable and return the info as a Player object.
	 * 
	 * Returns: the player's information as a Player object, or null.
	 */
	public Player login(String username, String password);
	/**
	 * When a user wants to create a new game and wait for an opponent.
	 * If the user already has an open game, this replaces this game - only one open game per user.
	 * Creates a new game with state INWAITING, the user is randomly assigned black or white
	 * @param player_id
	 * @return int game_id or 0 if method has failed.
	 */
	public int newGame(String player_id);
	/**
	 * Searches the database for all games that are INWAITING and only have one player. 
	 * The String contains: user_id#white_username#black_username#last_active# for each game.
	 * Example: 3#null#bodum#2015-03-05 14:33:14.284556#4#null#lorilew#2015-03-05 14:33:14.284556#...
	 * 
	 * Returns: an String of all games in waiting info. 
	 */
	public String getGamesInWaiting();
	/**
	 * Given a username and password, this method checks that the game_id is available for
	 * username. If available, it will determine whether the new player is black or white and
	 * add their username to the chess_game table. It will update the the game_start and last_active attributes.
	 * If the game is unavailable for given user and game_id this method returns null.
	 * 
	 * Returns: A Game object containing all the information for the updated game with game_id given.
	 */
	public Game joinGame(String username, int game_id);
	/**
	 * Given a username search the database all the games that the user is either black or white player
	 * and the game in INPROGRESS. 
	 * Add game information to a string in the format game_id#white_username#black_username#last_active#
	 * and add each game info to the string.  
	 * list is order by last_active, in descending order.
	 * 
	 * Returns: A String containing information about the current games.
	 */
	public String getCurrentGames(String username);
	/**
	 * When a user wants to continue a game, first check that the username is a player in game, game_id.
	 * If valid game then update the last_active attribute for the game and return the information
	 * as a Game Object. Else if user not part of game return null.
	 * 
	 * Returns: A Game object for the game with game_id.
	 */
	public Game continueGame(String username, int game_id);
	/**
	 * Gets the all the information from the database for a given username and 
	 * returns the information as a Player Object.
	 * 
	 * @param username The user's username.
	 * @return A player object or null if username does not exist.
	 */
	public Player getPlayerInfo(String username);
	/**
	 * Gets the all the information from the database for a given game_id and 
	 * returns the information as a Game Object.
	 * 
	 * @param game_id An int value referring to a game entry in the database.
	 * @return A Game object or null if game_id does not exist.
	 */
	public Game getGameInfo(int game_id);
	/**
	 * When a move is made this method should be called to update the board_info 
	 * in the chess_game table.
	 * 
	 * @param game_id An int game_id.
	 * @param fen A String containing the fen of the updated board.
	 * @return The updated game object.
	 */
	public Game updateGame(int game_id, String fen);
	/**
	 * When a game has finished call this method to update the database. 
	 * Arguments include the game_id and the result in format:
	 * WHITEWIN - white player wins the game.
	 * BLACKWIN - black player wins the game.
	 * DRAW - game ends in a draw.
	 * If result format is incorrect this method does returns 0,
	 * if successful returns 1.
	 * @param game_id Int representing the game_id.
	 * @param result A string as specified above.
	 * @return an int indicating whether the method was successful.
	 */
	public int endGame(int game_id, String result);
	/**
	 * Searches database for number of games, wins, losses, draws for a given username.
	 * Returns a string in the format numOfGamesTotal#numOfWins#numOfLosses#numOfDraws#
	 * Example: 3#1#1#1#
	 * 
	 * @param username A String, the username to search for.
	 * @return a String representing the stats for the given username.
	 */
	public String getUserStats(String username);
}
