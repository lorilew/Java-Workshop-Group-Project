/**
 * FEN (Forsyth-Edwards Notation) parsing tools
 * see http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
 * 
 * TODO: maybe a FAN validator;
 */
package util;

public class Fen {
	
	/**
	 * Converts board state from FEN board part to char[][]
	 * See util.Cell for indexing example
	 * @param fenBoard, String containing ONLY the board part of the FEN
	 * @return char[][]
	 */
	public static char[][] fenBoardToArray(String fenBoard) {
		String[] fenRow = fenBoard.split("/");
		char[][] board = new char[8][8];
		int j = 0;
		for (int i = 0; i < 8; i++) {
			char[] row = fenRow[7-i].toCharArray();
			j = 0;
			for (char c : row){
				if (Character.isDigit(c)) {
					for (int k = 0; k < Integer.parseInt(Character.toString(c)); k++) {
						board[j][i] = '.';
						j++;
					}
				}else{
					board[j][i] = c;
					j++;
				}
			}
		}
		return board;
	}
	
	/**
	 * Converts board condition from char[][] to FEN board part
	 * See util.Cell for indexing example
	 * @param board, char[][] containing pieces characters
	 * @return fenBoard, String representing ONLY the board part of the FEN
	 */
	public static String arrayToFenBoard(char[][] board) {
		StringBuffer fenBuffer = new StringBuffer();
		int emptyCount = 0;
		for(int i=7; i >= 0; i--){
			char[] row =  board[i];
			for(int j=0; j<8; j++){
				char cell = board[j][i];
				if(cell == '.'){
					emptyCount++;
				}else{
					if(emptyCount == 0){
						fenBuffer.append(cell);
					}else{
						fenBuffer.append(emptyCount);
						fenBuffer.append(cell);
						emptyCount = 0;
					}
				}
			}
			if(emptyCount != 0){
				fenBuffer.append(emptyCount);
				emptyCount = 0;
			}
			fenBuffer.append('/');
		}
		return fenBuffer.substring(0, fenBuffer.length()-1);
	}
	
	
	/**
	 * Prints a char[][] board
	 * @param board, char[8][8] array 
	 */
	public static void printBoard(char[][] board) {
		System.out.println("    +-----------------+");
		for (int i = 7; i >= 0; i--) {
			System.out.print(i+" "+(i+1)+" | ");
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[j][i]+" ");
			}
			System.out.println("|");
		}
		System.out.println(" Y  +-----------------+");
		System.out.println("  X   a b c d e f g h  ");
		System.out.println("      0 1 2 3 4 5 6 7  ");
	}
	
	public static void main(String[] args) {
		String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		String[] fenData = fen.split(" ");
		
		System.out.println(fenData[0]);		
		char[][] board = new char[8][8];
		board = fenBoardToArray(fenData[0]);
		
		String fenBoard = arrayToFenBoard(board);
		System.out.println(fenBoard);	
		printBoard(board);
		System.out.println("First 3 cell of the board: ");
		System.out.println(board[0][0]+" "+board[0][1]+" "+board[0][2]);
		Cell c = new Cell(4, 0);
		System.out.println(c.toString() + " contains " + board[c.x][c.y]);
	}
	
}
