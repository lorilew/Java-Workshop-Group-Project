package database;

import java.sql.Timestamp;

public class Game extends GameAbstract{
	private int game_id;
	private String white, black;
	private Timestamp started, ended, last_active;
	private String gameInfo;
	private String state;
	public Game(int game_id, String white, String black, Timestamp started, 
			Timestamp ended, Timestamp last_active, String gameInfo, String state){
		this.game_id = game_id;
		this.white = white;
		this.black = black;
		this.started = started;
		this.ended = ended;
		this.last_active = last_active;
		this.gameInfo = gameInfo;
		this.state = state;
	}
	@Override
	public void updateBoard(String boardInfo, DatabaseManager db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnded(String winner, DatabaseManager db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getGameID() {
		return this.game_id;
	}

	@Override
	public String getWhite() {
		return this.white;
	}

	@Override
	public String getBlack() {
		return this.black;
	}

	@Override
	public Timestamp getStart() {
		return this.started;
	}

	@Override
	public Timestamp getEnd() {
		return this.ended;
	}

	@Override
	public Timestamp getLastActive() {
		return this.last_active;
	}

	@Override
	public String getGameInfo() {
		return this.gameInfo;
	}

	@Override
	public String getState() {
		return this.state;
	}
	
}
