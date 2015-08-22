package sockets;

import java.io.IOException;
import java.net.*;

public class ServerSocketManager {
	
	private ServerSocket serverSocket;
	private ServerMessageHandlerInterface handler;
	private int port;
	private boolean running;
	
	/**
	 * Constructor for the ServerSocketManager. Takes the port number to use as an argument.
	 * @param port the port number to use as an int
	 * @param handler the ServerMessageHandler to pass client messages to
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public ServerSocketManager(int port, ServerMessageHandlerInterface handler)
	{
		this.port = port;
		this.handler = handler;
	}
	
	/**
	 * Begins the loop to look for new client connections, and give them their own threads.
	 * Returns if there is some sort of problem, or the execution should halt.
	 * @param mc the maximum total number of connections to allow. 0 or below allows all connections. Used for testing.
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public void listen(int mc)
	{
		int connections = 0;
		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch(IOException e)
		{
			return;
		}
		
		try
		{
			serverSocket.setSoTimeout(250);
			running = true;
			
			while(running)
			{
				try
				{
					Socket clientSocket = serverSocket.accept();
					// Create a new thread to listen to the socket
					new ServerSocketConnection(clientSocket, this.handler);
					// Here we can allow one connection only for testing
					// running = false;
					connections++;
					if(mc >= 1 && connections >= mc)
					{
						running = false;
					}
				}
				catch(SocketTimeoutException ste)
				{
					/* 
					 * Nothing to worry about here, this just (intentionally) stops the ServerSocket from
					 * waiting once in a while, so that we can occasionally check other things.
					 */
				}
			}
			
		}
		catch(IOException e)
		{
			// Something bad has happened here, so we've broken out of the loop
		}
		
		try
		{
			serverSocket.close();
		}
		catch(IOException io)
		{
			// Something bad has happened here
			io.printStackTrace();
		}
	}

}
