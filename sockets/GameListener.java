package sockets;

public interface GameListener {
	
	public void makeMove(String fen);
	
	public void setWin();
	public void setLoss();
	public void setDraw();

}
