package sockets;

public interface ServerMessageHandlerInterface {
	
	/**
	 * Handles a message sent from the client to the server.
	 * 
	 * The arguments parameter will currently be passed with all arguments, including a single empty
	 * argument in messages without any. It is up to the user to check the correct number and format
	 * are provided, and account for any extra arguments if necessary.
	 * @param connection the connection from which the message came, as a ServerSocketConnection
	 * @param message the message to handle, as a ClientMessage
	 * @param argument the argument(s) of the message, as an array of Strings
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public void handleMessage(ServerSocketConnection connection, ClientMessage message, String[] arguments);

}
