package engine;

import java.util.Observable;

public abstract class ObservableBoardData extends Observable{
    
    // ####################
    // #     Getters      #
    // ####################

    /**
     * Returns a 2D character array representing the 8x8 board
     * <br>
     * PNBRKQ -> Pawn, Knight, Bishop, Rook, King, Queen, with upper-case representing white.
     *
     * @return A 2D character array representing the 8x8 board
     */
    public abstract char[][] getBoard();
    
    
    /**
     * Returns the name of the White Player
     *
     * @return The name
     */
    public abstract String getWhitePlayer();
    
    /**
     * Returns the name of the Black Player
     *
     * @return The name
     */
    public abstract String getBlackPlayer();
    
    /**
     * Returns whether or not it is white's turn
     *
     * @return whether or not it is white's turn
     */
    public abstract boolean isWhiteTurn();
    
    /**
     * Gets the current turn count. 
     * The turn count starts at 1 and is incremented at the end of blacks turn.
     *
     * @return The turn count
     */
    public abstract int getTurnCount();
    
    
    /**
     * Gets the current half turn count. 
     * This is the total number of moves since a piece hasn't been taken
     * or a pawn hasn't been advanced. At 50, stalemate is declared.
     *
     * @return The half turn count
     */
    public abstract int getHalfTurnCount();
    
    /**
     * Returns a string representation of available castling moves. <br>
     * <br>
     * K = White king-side castling is allowed <br>
     * Q = White king-side castling is allowed <br>
     * k = Black king-side castling is allowed <br>
     * q = Black king-side castling is allowed <br>
     *
     * @return A string representation of available castling moves
     */
    public abstract String getCastlingData();
    
    /**
     * Returns a string representing the cell currently avaliable for enpassant
     * 
     * @return a string representing the cell currently avaliable for enpassant
     */
    public abstract String getEnPassant();
    
    /**
     * Returns whether the sate of the game is drawn
     * 
     * @return If the game is drawn
     */
    public abstract boolean isDraw();
    
    /**
     * Returns whether the sate of the game is won
     * 
     * @return If the game is won
     */
    public abstract boolean isWin();
    
    /**
     * Returns whether the sate of the game is lost
     * 
     * @return If the game is lost
     */
    public abstract boolean isLoss();

    
    
    // ####################
    // #     Setters      #
    // ####################
    
    /**
     * Forcefully sets the model to match the FEN
     *
     * @param fen The FEN to update to
     */
    public abstract void setFen(String fen);
    
    /**
     * Sets the current board layout
     * <br>
     * PNBRKQ -> Pawn, Knight, Bishop, Rook, King, Queen, with upper-case representing white.
     *
     * @param board A 2D character array representing the 8x8 board
     */
    public abstract void setBoard(char[][] board);
    
    
    /**
     * Sets the name of the White Player
     *
     * @param name The name
     */
    public abstract void setWhitePlayer(String name);
    
    /**
     * Sets the name of the Black Player
     *
     * @param name The name
     */
    public abstract void setBlackPlayer(String name);
    
    /**
     * Sets the game to White's turn
     */
    public abstract void setWhiteTurn();
    
    /**
     * Sets the game to Black's turn
     */
    public abstract void setBlackTurn();
    
    /**
     * Sets the current turn count. 
     * The turn count starts at 1 and is incremented at the end of blacks turn.
     *
     * @param turnCount The turn count
     */
    public abstract void setTurnCount(int turnCount);
    
    
    /**
     * Sets the current half turn count. 
     * This is the total number of moves since a piece hasn't been taken
     * or a pawn hasn't been advanced. At 50, stalemate is declared.
     *
     * @param halfTurnCount The half turn count
     */
    public abstract void setHalfTurnCount(int halfTurnCount);
    
    /**
     * Sets the string representation of available castling moves. <br>
     * <br>
     * K = White king-side castling is allowed <br>
     * Q = White king-side castling is allowed <br>
     * k = Black king-side castling is allowed <br>
     * q = Black king-side castling is allowed <br>
     *
     * @param castlingData A string representation of available castling moves
     */
    public abstract void setCastlingData(String castlingData);
    
    /**
     * Sets the string representing the cell currently avaliable for enpassant
     * 
     * @param enPassant The string representing the cell currently avaliable for enpassant
     */
    public abstract void setEnPassant(String enPassant);
    
    /**
     * Updates the game state to a draw
     */
    public abstract void setDraw();
    
    /**
     * Updates the game state to a win
     */
    public abstract void setWin();
    
    /**
     * Updates the game state to a loss
     */
    public abstract void setLoss();
    
}
