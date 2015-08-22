package util;

import java.util.ArrayList;

public class DataProcessor {
	/**
	 * Given a String containing a list of games and their info, split 
	 * each game into a separate string and add to a list.
	 * Each element in the String should be separated by #.
	 * @param gameString A String containing a list of game info.
	 * @return A list of Strings, each string for each game info.
	 */
	public static ArrayList<String> GameStringToList(String gameString){
		ArrayList<String> gameList = new ArrayList<String>();
		String[] str = gameString.split("#");
		StringBuffer buffer = new StringBuffer();
		
		// Each game info has four elements. 
		for(int i=0; i<str.length; i+=4){
			buffer.append(str[i]+"#"+str[i+1]+"#"+str[i+2]+"#"+str[i+3]+"#");
			gameList.add(buffer.toString());
			buffer.delete(0, buffer.length());
		}
		return gameList;
	}
}
