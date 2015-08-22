package sockets;

public class InputMessage {
	
	ServerMessage message;
	String argument;
	
	/**
	 * Constructor for the message
	 * @param msg the message type, as a SocketInputMessage
	 * @param argument the message's argument, as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-02
	 */
	public InputMessage(ServerMessage msg, String argument)
	{
		this.message = msg;
		this.argument = argument;
	}
	
	/**
	 * Returns the message type of the message
	 * @return the message type as a SocketInputMessage
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-02
	 */
	public ServerMessage getMessage()
	{
		return this.message;
	}
	
	/**
	 * Returns the message's arguments as a single unparsed String
	 * @return the message's arguments as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-02
	 */
	public String getArgument()
	{
		return this.argument;
	}
	
	/**
	 * Returns the message's arguments as an array of type String
	 * @return the message's arguments as an array of type String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-06
	 */
	public String[] getArgumentArray()
	{
		return this.argument.split("#");
	}
	
	public void setAll(ServerMessage msg, String argument)
	{
		this.message = msg;
		this.argument = argument;
	}
}
