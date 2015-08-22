/**
 * Game class that describes the current state of the game from the client perspective
 * 
 * TODO: JUnit tests
 */
package engine;

import java.util.ArrayList;

import util.Fen;

public class Game extends ObservableBoardData{
	private String whitePlayer;
	private String blackPlayer;
	private boolean playerWhite;
	private char[][] board;	// See util.Cell for indexing example
	private boolean whiteTurn;	
	private String castling;
	private String enPassant;
	private int halfmove;
	private int fullmove;
        
        // Win state data
        private boolean win = false;
        private boolean loss = false;
        private boolean draw = false;

	/**
	 * Empty constructor
	 */
	public Game(){
		
	}
	
	/**
	 * FEN constructor
	 * @param fen, String of FEN notation
	 */
	public Game(String fen){
            String[] fenData = fen.split(" ");
            this.board = Fen.fenBoardToArray(fenData[0]);
            this.whiteTurn = fenData[1].equals("w");
            this.castling = fenData[2];
            this.enPassant = fenData[3];
            this.halfmove = Integer.parseInt(fenData[4]);
            this.fullmove = Integer.parseInt(fenData[5]);
	}
	
	/**
	 * Fields constructor
	 * @param whitePlayer
	 * @param blackPlayer
	 * @param playerWhite
	 * @param board
	 * @param whiteTurn
	 * @param castling
	 * @param enPassant
	 * @param halfmove
	 * @param fullmove
	 */
	public Game(String whitePlayer, String blackPlayer, boolean playerWhite, char[][] board, boolean whiteTurn, String castling, String enPassant, int halfmove, int fullmove) {
		super();
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.playerWhite = playerWhite;
		this.board = board;
		this.whiteTurn = whiteTurn;
		this.castling = castling;
		this.enPassant = enPassant;
		this.halfmove = halfmove;
		this.fullmove = fullmove;
	}
        
        /**
         * Forcefully sets the model to match the given fen, to be used when
         * called from the server
         * 
         * @param fen The FEN to update to
         */
        @Override
        public void setFen(String fen){
            String[] fenData = fen.split(" ");
            this.board = Fen.fenBoardToArray(fenData[0]);
            this.whiteTurn = fenData[1].equals("w");
            this.castling = fenData[2];
            this.enPassant = fenData[3];
            this.halfmove = Integer.parseInt(fenData[4]);
            this.fullmove = Integer.parseInt(fenData[5]);
            
            this.setChanged();
        }
	
	/**
	 * Makes a naive move
	 * Takes the content of a square pastes it on the destination and replaces the origin with empty
	 * See util.Cell for indexing example
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void makeMove(int x1, int y1, int x2, int y2){

		this.board[x2][y2] = board[x1][y1];
		this.board[x1][y1] = '.';
                this.setChanged();

	}

        @Override
	public String getWhitePlayer() {
		return whitePlayer;
	}

        @Override
	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

        @Override
	public String getBlackPlayer() {
		return blackPlayer;
	}

        @Override
	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public boolean isPlayerWhite() {
		return playerWhite;
	}

	public void setPlayerWhite(boolean playerWhite) {
		this.playerWhite = playerWhite;
	}

        @Override
	public char[][] getBoard() {
                // Cloned for safety (only way to edit is to setBoard();

                // Clone the 'shallow' structure of array
                char[][] newArray = board.clone();
                
                // Clone the 'deep' structure of array
                for(int row=0;row < board.length;row++){
                    newArray[row]= board[row].clone();
                }

                return newArray;
	}

        @Override
	public void setBoard(char[][] board) {
		this.board = board;
                this.setChanged();
	}
	
	public void printBoard(){
		Fen.printBoard(board);
	}

        @Override
	public boolean isWhiteTurn() {
		return whiteTurn;
	}

        @Override
	public void setWhiteTurn() {
		this.whiteTurn = true;
	}
        
        @Override
        public void setBlackTurn() {
                this.whiteTurn = false;
        }

        @Override
	public String getCastlingData() {
		return castling;
	}

        @Override
	public void setCastlingData(String castling) {
		this.castling = castling;
	}

        @Override
	public String getEnPassant() {
		return enPassant;
	}

	public void setEnPassant(String enPassant) {
		this.enPassant = enPassant;
	}

	public int getHalfTurnCount() {
		return halfmove;
	}

	public void setHalfTurnCount(int halfmove) {
		this.halfmove = halfmove;
	}

	public int getTurnCount() {
		return fullmove;
	}

        @Override
	public void setTurnCount(int fullmove) {
		this.fullmove = fullmove;
	}

    @Override
    public boolean isWin() {
        return win;
    }

    @Override
    public void setWin() {
        this.win = true;
        this.setChanged();
    }

    @Override
    public boolean isLoss() {
        return loss;
    }

    @Override
    public void setLoss() {
        this.loss = true;
        this.setChanged();
    }

    @Override
    public boolean isDraw() {
        return draw;
    }

    @Override
    public void setDraw() {
        this.draw = true;
        this.setChanged();
    }
        
        
        
	
	public String toFen() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(Fen.arrayToFenBoard(board));
		if(whiteTurn) list.add("w");
		else list.add("b");
		list.add(castling);
		list.add(enPassant);
		list.add(Integer.toString(halfmove));
		list.add(Integer.toString(fullmove));		
		return String.join(" ", list);
	}

	@Override
	public String toString() {
		return "Game [player1=" + whitePlayer + ", player2=" + blackPlayer + ", playerWhite=" + playerWhite + ", whiteTurn=" + whiteTurn
				+ ", castling=" + castling + ", enPassant=" + enPassant + ", halfmove=" + halfmove + ", fullmove=" + fullmove + "]";
	}

	public static void main(String[] args) {
		String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		Game chess = new Game(fen);
		chess.printBoard();
		System.out.println(chess);
		System.out.println(chess.toFen().equals(fen));
	}



}
