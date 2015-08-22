package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSocketConnection {
	
	/*
	 * Despite the name, this class deals with server-side Sockets, as opposed to ServerSockets.
	 * 
	 * This class stores together all the relevant data for a particular Socket, and handles
	 * sending messages through that Socket. It is mainly used as data storage, with some extra
	 * wrapper and tidy-up methods.
	 */

	private Socket socket;
	private PrintWriter outputStream;
	private ServerSocketListener listener;
	private ServerMessageHandlerInterface handler;
	private boolean connected;

	/**
	 * Constructor for the ServerConnection
	 * @param socket the Socket that this connection represents
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public ServerSocketConnection(Socket socket, ServerMessageHandlerInterface handler)
	{
		this.handler = handler;
		try
		{
			this.socket = socket;
			this.outputStream = new PrintWriter(socket.getOutputStream(), true);
			this.listener = new ServerSocketListener(new BufferedReader(
					new InputStreamReader(socket.getInputStream())), this);
			this.listener.start();
			
			this.connected = true;
		}
		catch(IOException e)
		{
			this.connected = false;
			try
			{
				socket.close();
			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the state of the connection
	 * @return the state of the connection as a boolean
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public boolean isConnected()
	{
		return this.connected;
	}
	
	/**
	 * Sends a message through the stored socket
	 * @param msg the message to send, as a ServerMessage
	 * @param argument the arguments of the message, as a String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public void sendMessage(ServerMessage msg, String argument) {
		if(this.connected)
		{
			outputStream.println(msg.toString() + ServerMessage.BREAKCHAR + argument);
		}
	}
	
	/**
	 * Passes the input from a listener on to the ServerMessageHandler, after parsing it
	 * @param input the input as an unparsed String
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public void handleInput(String input)
	{
		if(this.handler != null)
		{
			ClientMessage message = ClientMessage.C_TEXT;
			String[] argument;
			
			int firstBreak = input.indexOf(ServerMessage.BREAKCHAR);
			
			if(firstBreak == -1)
			{
				message = ClientMessage.valueOf(input);
				argument = new String[1];
				argument[0] = "";
			}
			else
			{
				message = ClientMessage.valueOf(input.substring(0, firstBreak));
				argument = input.substring(firstBreak + 1, input.length()).split(ServerMessage.BREAKCHAR + "");
			}
			
			handler.handleMessage(this, message, argument);
		}
	}
	
	/**
	 * Disconnect from the client.
	 * This *should* be called automatically by the garbage collector if it hasn't already been
	 * called manually. A manual call once you've finished with an object is still advisable though.
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public void disconnect()
	{
		if(this.connected)
		{
			try
			{
				//outputStream.println("disconnect");
				socket.getInputStream().close();

				outputStream.close();
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println("There was a failure while disconnecting the ClientSocketManager.");
				e.printStackTrace();
			}

			this.connected = false;
		}
	}
	
	public void finalize()
	{
		if(this.connected)
		{
			disconnect();
		}
	}
	
	public boolean equals(Object other)
	{
		return this == other;
	}

}
