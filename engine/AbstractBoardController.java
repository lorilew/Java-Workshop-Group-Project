package engine;

import util.Cell;

/**
 * Abstract object for the chessboard controller
 */
 
public abstract class AbstractBoardController {

    protected ObservableBoardData gameData;

    public AbstractBoardController(ObservableBoardData gameData){
        this.gameData = gameData;
    }
    
    /**
     * Move a piece from one square to another if it is a legal move
     *
     * @param cellFrom The origin square
     * @param cellTo The destination square
     */
    public abstract void makeMove(Cell cellFrom, Cell cellTo);
    
    /**
     * Returns whether a particular move is a valid move
     *
     * @param cellFrom The origin square
     * @param cellTo The destination square
     */
    public abstract boolean isValidMove(Cell cellFrom, Cell cellTo);
    
    
}