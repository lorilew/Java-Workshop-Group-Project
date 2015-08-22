package sockets;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerSocketListener extends Thread {
	
	/*
	 * Despite the name, this class deals with server-side Sockets, as opposed to ServerSockets.
	 */
	
	private BufferedReader inputStream;
	private ServerSocketConnection connection;
	private boolean running;
	
	public ServerSocketListener(BufferedReader inputStream, ServerSocketConnection connection)
	{
		super("ServerSocketListener");
		this.inputStream = inputStream;
		this.connection = connection;
		this.running = false;
	}
	
	@Override
	public void run()
	{
		this.running = true;
		
		try
		{
			while(running)
			{
				// TODO: Decide exactly how to handle input in this case. Blocking queue to pass
				// all input to a single thread? Or synchronised method in a `Handler' class to
				// allow each thread to deal with it's own message?
				// I'm currently leaning towards the latter. The former was only originally used
				// in the client so that only I have to deal with breaking out a thread waiting
				// for a BufferedReader.
				String input = inputStream.readLine();
				//int firstBreak = input.indexOf(ServerMessage.BREAKCHAR);
				//if(firstBreak == -1)
				//{
				//	// Handle
				//}
				//else
				//{
				//	// Handle
				//}
				if(input == null)
				{
					this.running = false;
				}
				else
				{
					connection.handleInput(input);
				}
			}
		}
		catch(IOException e)
		{
			/* 
			 * Error catching here
			 * Currently has none, as throwing an exception by closing the input stream of the socket
			 * seems to be the only/best way of stopping the read loop.
			 * 
			 * If anyone comes up with a better method, let me know.
			 * 
			 * Oddly enough, this doesn't always seem to function the way it appears it should.
			 * In general, readLine() seems to return null rather than throwing an exception.
			 */
		}
		finally
		{
			this.running = false;
		}
	}

}
