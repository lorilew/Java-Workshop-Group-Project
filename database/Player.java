package database;

import java.sql.Timestamp;

public class Player extends PlayerAbstract {
	private int player_id;
	private String username;
	private String password;
	private Timestamp last_active, reg_date;
	
	public Player(int player_id, String username, String password, Timestamp last_active, Timestamp reg_date){
		this.player_id = player_id;
		this.username = username;
		this.password = password;
		this.last_active = last_active;
		this.reg_date = reg_date;
	}
	
	public String getStats(DatabaseManager db) {
		return null;
	}
	@Override
	public int getPlayerID() {
		return this.player_id;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
	@Override
	public Timestamp getLastActive() {
		return this.last_active;
	}
	@Override
	public Timestamp getRegDate() {
		return this.reg_date;
	}

}
