
package engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import util.Cell;

public class BoardController extends AbstractBoardController{

    public BoardController(ObservableBoardData gameData) {
        super(gameData);
    }

    @Override
    public void makeMove(Cell cellFrom, Cell cellTo) {
        if (!isValidMove(cellFrom, cellTo)){
            return;
        }
        char[][] board = gameData.getBoard();
        char piece = board[cellFrom.x][cellFrom.y];
        char destinationPiece = board[cellTo.x][cellTo.y];
        
        instantMove(cellFrom, cellTo);
        
        // Castling:
        // if the king has been moved
        if(Character.toLowerCase(piece) == 'k'){
        	// if it has been moved more than one square
        	if(Math.abs(cellFrom.x - cellTo.x)>1){
        		// if castling to the right (kingside)
        		if(cellFrom.x < cellTo.x){
        			// move the rook to the left
        			instantMove(new Cell(7,cellFrom.y), new Cell(5,cellFrom.y));
        		// castling to the left (queenside)
        		}else{
        			// move the rook to the right
        			instantMove(new Cell(0,cellFrom.y), new Cell(2,cellFrom.y));
        		}
        	}
        }

        // 50 turn counter
    	// check if a figure was not taken
        if (destinationPiece == '.') gameData.setHalfTurnCount(gameData.getHalfTurnCount() + 1);
        // if it was, reset the counter
        else gameData.setHalfTurnCount(0);
        // if a pawn advanced reset the counter
        if (Character.toLowerCase(piece) == 'p') gameData.setHalfTurnCount(0);   
        
        
        // check if the enemy has valid moves
        
        // get the new board
        board = gameData.getBoard();
        // get the cells of all enemy pieces
        ArrayList<Cell> enemyCells = new ArrayList<Cell>();
        for (int i=0; i<8; i++){
        	for (int j=0; j<8; j++){
        		if (gameData.isWhiteTurn() == Character.isLowerCase(board[i][j]) )
        			enemyCells.add(new Cell(i,j));
        	}
        }
        System.out.println("Enemy Cells: ");
        for(Cell c : enemyCells) System.out.println(c);
        // start getting the available moves for each one of them
        PotentialMoves potentialMoves = new PotentialMoves(gameData);
        Set<Cell> moveList = new HashSet();
        for (Cell enemyCell : enemyCells){
        	switch(Character.toLowerCase(board[enemyCell.x][enemyCell.y])){
        	case 'n':
        		moveList.addAll(potentialMoves.getKnightMoves(enemyCell));
        		break;
        	case 'p':
        		moveList.addAll(potentialMoves.getPawnMoves(enemyCell));
        		break;
        	case 'k':
        		moveList.addAll(potentialMoves.getKingMoves(enemyCell));
        		break;
        	case 'q':
        		moveList.addAll(potentialMoves.getQueenMoves(enemyCell));
        		break;
        	case 'b':
        		moveList.addAll(potentialMoves.getBishopMoves(enemyCell));
        		break;
        	case 'r':
        		moveList.addAll(potentialMoves.getRookMoves(enemyCell));
        		break;
        	}
        	// if there are moves break
        	if(!moveList.isEmpty()) break;
        }
        System.out.println("Enemy moves: ");
        for(Cell c : moveList) System.out.println(c);
        // if there are still no available moves for the enemy
        if(moveList.isEmpty()){
            // Check if the enemy is in check
            if (isCheck(!gameData.isWhiteTurn()))
            	System.out.println("Checkmate!");
            else 
            	System.out.println("Stalemate!");
        }
        
        // Change turn state + increment turn counter
        if(gameData.isWhiteTurn()){
            gameData.setBlackTurn();
        }else{
            gameData.setWhiteTurn();
            gameData.setTurnCount(gameData.getTurnCount() + 1);
        }
        
        gameData.notifyObservers();
    }
    
    private void instantMove(Cell cellFrom, Cell cellTo){
        char[][] board = gameData.getBoard();
        
        board[cellTo.x][cellTo.y] = board[cellFrom.x][cellFrom.y];
        board[cellFrom.x][cellFrom.y] = '.';
        
        gameData.setBoard(board);
    }

    @Override
    public boolean isValidMove(Cell cellFrom, Cell cellTo) {
        char[][] board = gameData.getBoard();
        char piece = board[cellFrom.x][cellFrom.y];
        
        // Ensure a piece is on the square
        if (piece == '.'){
            return false;
        }
        
        // Ensure it's the right turn to play the given piece
        if (Character.isUpperCase(piece) ^ gameData.isWhiteTurn()){
            return false;
        }
        
        // Now determine the valid character
        char generalPiece = Character.toLowerCase(piece);
        PotentialMoves potentialMoves = new PotentialMoves(gameData);
        
        Set<Cell> moveList = new HashSet<Cell>();
        switch(generalPiece){
            case 'n':
                moveList = potentialMoves.getKnightMoves(cellFrom);
                break;
            case 'p':
                moveList = potentialMoves.getPawnMoves(cellFrom);
                break;
            case 'k':
                moveList = potentialMoves.getKingMoves(cellFrom);
                break;
            case 'q':
                moveList = potentialMoves.getQueenMoves(cellFrom);
                break;
            case 'b':
                moveList = potentialMoves.getBishopMoves(cellFrom);
                break;
            case 'r':
                moveList = potentialMoves.getRookMoves(cellFrom);
                break;
            default:
                moveList.add(cellTo);  // Added for testing only
        }
        
        if (!moveList.contains(cellTo)){
            return false;
        }
        
        //Make the move
        instantMove(cellFrom, cellTo);
        
        // Check for check
        if (isCheck(Character.isUpperCase(piece))){
            // Revert board and return false
            gameData.setBoard(board);
            return false;
        }
        
        // Revert the board from any changes made
        gameData.setBoard(board);
        return true;
    }
    
    public boolean isCheck(boolean white){
        PotentialMoves potentialMoves = new PotentialMoves(gameData);
        
        Cell cell = findKing(white);
        return potentialMoves.isCellThreatened(cell);
    }
    
    private Cell findKing(boolean white){
        char[][] board = gameData.getBoard();
        char searchChar = white ? 'K' : 'k';
        
        for (int i = 0; i < board.length; i++) {
            char[] file = board[i];
            for (int j = 0; j < file.length; j++) {
                char cell = file[j];
                if (cell == searchChar){
                    return new Cell(i, j);
                }
            }
        }
        return null;
    }
    
}
