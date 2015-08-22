package server;

import sockets.ServerSocketManager;

public class ServerTestThread extends Thread {

	int maxConnections;
	
	protected ServerTestThread(int mc)
	{
		maxConnections = mc;
	}
	
	@Override
	public void run()
	{
		ServerSocketManager ssm = new ServerSocketManager(4444, new GameManager());
		ssm.listen(maxConnections);
	}
	
}
