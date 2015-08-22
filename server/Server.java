package server;

import sockets.ServerSocketManager;

public class Server {

	/**
	 * Standard main method
	 * @param args the command line arguments as an array of Strings
	 */
	public static void main(String[] args)
	{
		ServerSocketManager ssm = new ServerSocketManager(4444, new GameManager());
		ssm.listen(0);
	}
	
}
