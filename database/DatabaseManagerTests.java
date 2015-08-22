package database;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class DatabaseManagerTests {
	DatabaseManager dbmanager = new DatabaseManager();
	/*
	 * login with a new user.
	 */
	@Test
	public void logintest1() {	
		Player testplayer = dbmanager.login("testname", "testpassword");
		String username = testplayer.getUsername();
		dbmanager.removeUser("testname", "testpassword");
		assertEquals(username, "testname");
	}
	/*
	 * login with a correct username and password
	 */
	@Test
	public void logintest2() {	
		Player testplayer = dbmanager.login("testname2", "testpassword2");
		String username = testplayer.getUsername();
		assertEquals(username, "testname2");
	}
	/*
	 * login with an incorrect username and password.
	 */
	@Test
	public void logintest3() {	
		Player testplayer2 = dbmanager.login("lorilew", "anotherpassword");
		assertTrue(testplayer2==null);
	}
	/*
	 * Get user info - user exists
	 */
	@Test
	public void getPlayerInfoTest1(){
		Player test = dbmanager.getPlayerInfo("testname2");
		String resultpassword = test.getPassword();
		assertEquals("testpassword2", resultpassword);
	}
	/*
	 * Get user info - user does not exist
	 */
	@Test
	public void getPlayerInfoTest2(){
		Player test = dbmanager.getPlayerInfo("testname");
		assertNull(test);
	}
	/*
	 * Get game info - game exists
	 */
	@Test
	public void getGameInfoTest1(){
		dbmanager.newGame("testname2");
		Game test = dbmanager.getGameInfo(6);
		assertEquals(test.getBlack(), "testname2");
	}
	/*
	 * Get game info - game does not exist 0
	 */
	@Test
	public void getGameInfoTest2(){
		Game test = dbmanager.getGameInfo(0);
		assertNull(test);
	}
	/*
	 * testname2 makes a new game and getGamesInWaiting
	 */
	@Test
	public void newGameAndGetGamesInWaiting1(){
		dbmanager.newGame("testname2");
		ArrayList list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		String[] thisgame = list.get(0).toString().split("#");
		assertEquals(thisgame[0],"6");
	}
	/*
	 * testname2 makes a new game and getGamesInWaiting
	 * test- ordered by last_active descending.
	 */
	@Test
	public void newGameAndGetGamesInWaiting2(){
		dbmanager.newGame("testname2");
		ArrayList list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		String[] thisgame = list.get(0).toString().split("#");
		Timestamp ts1 = Timestamp.valueOf(thisgame[3]);
		if(list.size()>1){
			String[] nextgame = list.get(1).toString().split("#");
			Timestamp ts2 = Timestamp.valueOf(nextgame[3]);
			assertTrue(ts2.before(ts1));
		}
	}
	/*
	 * Test join game
	 * create a game by one player
	 * join by another player
	 * remove game
	 * Test - game is in progress
	 */
	@Test
	public void joinGameTest1(){
		dbmanager.login("test3", "hello");
		dbmanager.login("test4", "yo");
		dbmanager.newGame("test3");
		ArrayList<String> list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		int game_id=0;
		for(String s:list){
			String[] info = s.split("#");
			if(info[1].equals("test3") || info[2].equals("test3")){
				game_id= Integer.parseInt(info[0]);
				break;
			}
		}
		dbmanager.joinGame("test4", game_id);
		Game testgame = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertEquals(testgame.getState(),"INPROGRESS");
	}
	/*
	 * Test join game
	 * create a game by one player
	 * join by another player
	 * remove game
	 * test white and black = username
	 */
	@Test
	public void joinGameTest2(){
		
		dbmanager.login("test3", "hello");
		dbmanager.login("test4", "yo");
		dbmanager.newGame("test3");
		ArrayList<String> list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		int game_id=Integer.parseInt((list.get(0).split("#"))[0]);
		dbmanager.joinGame("test4", game_id);
		Game testgame = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);	
	}
	/*
	 * Test joinGame - check  last_active is updated
	 */
	@Test
	public void joinGameTest3(){
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		dbmanager.login("test3", "hello");
		dbmanager.login("test4", "yo");
		dbmanager.newGame("test3");
		ArrayList<String> list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		int game_id=0;
		for(String s:list){
			String[] info = s.split("#");
			if(info[1].equals("test3") || info[2].equals("test3")){
				game_id= Integer.parseInt(info[0]);
				break;
			}
		}
		dbmanager.joinGame("test4", game_id);
		Game testgame = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(testgame.getLastActive().after(ts));
	}
	/*
	 * Test joinGame - check  timestarted is updated
	 */
	@Test
	public void joinGameTest4(){
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		dbmanager.login("test3", "hello");
		dbmanager.login("test4", "yo");
		dbmanager.newGame("test3");
		ArrayList<String> list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		int game_id=0;
		for(String s:list){
			String[] info = s.split("#");
			if(info[1].equals("test3") || info[2].equals("test3")){
				game_id= Integer.parseInt(info[0]);
				break;
			}
		}
		dbmanager.joinGame("test4", game_id);
		Game testgame = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(testgame.getStart().after(ts));
	}
	/*
	 * Test joinGame - join game you are in
	 */
	@Test
	public void joinGameTest5(){
		
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		dbmanager.login("test3", "hello");
		dbmanager.newGame("test3");
		ArrayList<String> list = util.DataProcessor.GameStringToList(dbmanager.getGamesInWaiting());
		int game_id=0;
		for(String s:list){
			String[] info = s.split("#");
			if(info[1].equals("test3") || info[2].equals("test3")){
				game_id= Integer.parseInt(info[0]);
				break;
			}
		}
		dbmanager.joinGame("test3", game_id);
		Game testgame = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		
		assertTrue(testgame.getStart()==null);
		
	}
	/*
	 * Test get current games - get's the correct game with game_id 7
	 */
	@Test 
	public void getCurrentGames1(){
		ArrayList<String> games = util.DataProcessor.GameStringToList(dbmanager.getCurrentGames("test3"));
		String[] result = games.get(0).split("#");
		assertEquals(result[0], "7");
	}
	/*
	 * Test get current games - check ordered by last_active
	 */
	@Test 
	public void getCurrentGames2(){
		ArrayList<String> games = util.DataProcessor.GameStringToList(dbmanager.getCurrentGames("test3"));
		String[] firstgame = games.get(0).split("#");
		String[] secondgame = games.get(1).split("#");
		Timestamp ts1 = Timestamp.valueOf(firstgame[3]);
		Timestamp ts2 = Timestamp.valueOf(secondgame[3]);
		assertTrue(ts1.after(ts2));
	}
	/*
	 * Test continue game
	 * Test that the last_active is updated.
	 */
	@Test 
	public void continueGametest1(){
		Date date = new Date();
		Timestamp then = new Timestamp(date.getTime());
		Game game = dbmanager.continueGame("test3", 7);
		assertTrue(then.before(game.getLastActive()));
	}
	/*
	 * Test updateGame - fen is added correctly.
	 */
	@Test
	public void updateGameTest1(){
		Game gamestart = dbmanager.getGameInfo(7);
		String oldfen = gamestart.getGameInfo();
		String newfen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
		Game gameNow = dbmanager.updateGame(7, newfen);
		dbmanager.updateGame(7, oldfen);
		assertEquals(newfen, gameNow.getGameInfo());
	}
	/*
	 * Test updateGame - last_active is updated
	 */
	@Test
	public void updateGameTest2(){
		Game gamestart = dbmanager.getGameInfo(7);
		Timestamp ts1 = gamestart.getLastActive();
		String oldfen = gamestart.getGameInfo();
		String newfen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
		Game gameNow = dbmanager.updateGame(7, newfen);
		dbmanager.updateGame(7, oldfen);
		Timestamp ts2 = gameNow.getLastActive();
		assertTrue(ts1.before(ts2));
	}
	/*
	 * Test endGame - game_end updated?
	 */
	@Test
	public void endGameTest1(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(game_id, "DRAW");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(ts1.before(game.getEnd()));
	}
	/*
	 * Test endGame - last_active updated?
	 */
	@Test
	public void endGameTest2(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(9, "DRAW");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(ts1.before(game.getLastActive()));
	}
	/*
	 * Test endGame - State DRAW
	 */
	@Test
	public void endGameTest3(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(game_id, "DRAW");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(game.getState().equals("DRAW"));
	}
	/*
	 * Test endGame - State WHITEWIN
	 */
	@Test
	public void endGameTest4(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(game_id, "WHITEWIN");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(game.getState().equals("WHITEWIN"));
	}
	/*
	 * Test endGame - State BLACKWIN
	 */
	@Test
	public void endGameTest5(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(game_id, "BLACKWIN");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(game.getState().equals("BLACKWIN"));
	}
	/*
	 * Test endGame - State invalid string
	 */
	@Test
	public void endGameTest6(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		dbmanager.endGame(9, "HELLOOOO");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(game.getState().equals("INPROGRESS"));
	}
	/*
	 * Test endGame - State invalid string - check returns 0;
	 */
	@Test
	public void endGameTest7(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		int result = dbmanager.endGame(9, "HELLOOOO");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(result == 0);
	}
	/*
	 * Test endGame - State valid string - check returns 1;
	 */
	@Test
	public void endGameTest8(){
		Date date = new Date();
		Timestamp ts1 = new Timestamp(date.getTime());
		dbmanager.newGame("test5");
		String[] gamesAvailable = dbmanager.getGamesInWaiting().split("#");
		int game_id = Integer.parseInt(gamesAvailable[0]);
		dbmanager.joinGame("test4", game_id);
		int result = dbmanager.endGame(9, "DRAW");
		Game game = dbmanager.getGameInfo(game_id);
		dbmanager.removeGame(game_id);
		assertTrue(result == 1);
	}
	/*
	 * Test getUserStats - number of total games finished
	 */
	@Test
	public void getUserStatsTest1(){
		String[] stats = dbmanager.getUserStats("test8").split("#");
		assertEquals(stats[0], "4");
	}
	/*
	 * Test getUserStats - number of wins
	 */
	@Test
	public void getUserStatsTest2(){
		String[] stats = dbmanager.getUserStats("test8").split("#");
		assertEquals(stats[1], "1");
	}
	/*
	 * Test getUserStats - number of wins
	 */
	@Test
	public void getUserStatsTest3(){
		String[] stats = dbmanager.getUserStats("test8").split("#");
		assertEquals(stats[2], "2");
	}
	/*
	 * Test getUserStats - number of wins
	 */
	@Test
	public void getUserStatsTest4(){
		String[] stats = dbmanager.getUserStats("test8").split("#");
		assertEquals(stats[3], "1");
	}
	/*
	 * Test getUserStats - user does not exist
	 */
	@Test
	public void getUserStatsTest5(){
		String stats = dbmanager.getUserStats("idontexist");
		assertEquals(stats, "0#0#0#0#");
	}
	
}

