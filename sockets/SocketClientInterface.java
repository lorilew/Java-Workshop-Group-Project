package sockets;

import engine.Game;
import util.Cell;

//import java.util.concurrent.LinkedBlockingQueue;

public interface SocketClientInterface {
	
	/*
	 *  The argument part of a message is a String.
	 *  If multiple pieces of information are provided, they will be split up by a breaking character
	 *  (to be decided, I'll use `#' for the time being).
	 *  eg, if the breaking character is `#' the argument for OUT_MAKEMOVE will look something like:
	 *  "3#5#3#7"
	 *  where a piece is being moved from position (3, 5) to (3, 7).
	 *  
	 *  A lot of output messages use a game_id to know which game they're referring to. Alternatively
	 *  I can have them not need to pass an argument, and just assume they are referring to the currently
	 *  active game for the client (as set by OUT_RESUMEGAME). If I did set it up this way, the only
	 *  time game_id would be passed as an argument by the client would be for OUT_JOINGAME and
	 *  OUT_RESUMEGAME. Let me know if this is easier/better.
	 *  
	 *  In general, it may be up to the client to ignore some messages. Any input messages it deems
	 *  unnecessary should be safe to ignore (obviously anything relevant to the current game or an
	 *  error should probably be handled).
	 *  
	 *  Things are probably useful message types missing from this list, so let me know if you need
	 *  anything extra. Similarly, there are probably redundant/unnecessary message types too, so let
	 *  me know if you think something is unneeded.
	 *  
	 *  The enumerated messages can be found in SocketInputMessages.java and SocketOutputMessages.java.
	 *  
	 *  ClientSocketManager implements this interface.
	 */
	
	/**
	 * Returns true if there have been no unresolved errors with the connection
	 * @return true if there have been no unresolved errors with the connection
	 */
	public boolean isConnected();
	
	/**
	 * Attempts to connect to the target server designated by setIP()
	 * @param ip the ip of the target server as a String
	 * @param port the port of the target server as an int
	 */
	public void connect(String ip, int port);
	
	/**
	 * Attempts to disconnect from the server.
	 * Should only be called if currently connected (isConnected() returns true).
	 * Should occur automatically upon garbage collection if needed.
	 */
	public void disconnect();
	
	/**
	 * Sends a message to the server.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @param msg the type of message to send, as defined in SocketOutputMessage.java
	 * @param argument the argument(s) of the message, as a String
	 */
	public void sendMessage(ClientMessage msg, String argument);
	
	/**
	 * Creates a game challenge.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @return returns the game id as a String if successful, an empty String otherwise.
	 */
	public String createGame() throws InterruptedException;
	
	/**
	 * Joins a game challenge made by another player.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @param game_id the game id of the game to join, as a String
	 * @return returns true if the server processed the request correctly.
	 */
	public boolean joinGame(String game_id) throws InterruptedException;
	
	/**
	 * Logs the user in to the server.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @param username the username to use.
	 * @param password the corresponding password (or hashed password).
	 * @return true if the server reports a correct login. False otherwise.
	 */
	public boolean login(String username, String password) throws InterruptedException;
	
	/**
	 * Returns a list of all open games on the server which the user may join.
	 * Returns null if there is a problem.
	 * This is temporarily returning an array of Strings, but will return a different class later.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @return an array of _ containing all open games (excluding the player's own).
	 */
	public GameListing[] getOpenGames() throws InterruptedException;
	
	/**
	 * Returns a list of all games the user is taking part in on the server.
	 * Returns null if there is a problem.
	 * This is temporarily returning an array of Strings, but will return a different class later.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @return an array of _ containing all of the users games underway.
	 */
	public GameListing[] getMyGames() throws InterruptedException;
	
	/**
	 * Informs the server that the client is viewing a particular game, and should receive updates.
	 * Once the client is done viewing this game, leaveGame() should be called.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @param game_id the game id of the game to join, as a String.
	 * @return a reference to the resulting game object.
	 */
	public Game resumeGame(String game_id) throws InterruptedException;
	
	/**
	 * Informs the server that the client is no longer viewing a particular game.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @return true if the server responds to the request.
	 */
	public boolean leaveGame() throws InterruptedException;
	
	/**
	 * Informs the server that the client wishes to abandon the current game.
	 * Also calls leaveGame();
	 * Should only be called if currently connected (isConencted() returns true).
	 * @return true if the server responds to the request.
	 */
	public boolean abandonGame() throws InterruptedException;
	
	/**
	 * Informs the server that the client wishes to perform a particular move.
	 * Should only be called if currently connected (isConencted() returns true).
	 * @param from the Cell to move a piece from.
	 * @param to the Cell to move a piece to.
	 * @return true if the server responds to the request.
	 */
	public boolean makeMove(Cell from, Cell to) throws InterruptedException;
	
	/**
	 * Provides a reference to the message queue for incoming messages from the server.
	 * You may pull messages from the queue as instances of InputMessage, and read both
	 * the type and arguments of each message.
	 * 
	 * By attempting to read a message while there is none on the queue, the reading thread will
	 * sleep until another message is added.
	 * 
	 * Adding new messages to the queue manually is technically possible, but highly discouraged.
	 * 
	 * Subject to discussion, this may be replaced with helper functions that hide the queue but provide
	 * the same intended functionality (and possibly pre-parse the information for you).
	 * @return a reference to the message queue
	 */
	//public LinkedBlockingQueue<InputMessage> getMessageQueue();

}
