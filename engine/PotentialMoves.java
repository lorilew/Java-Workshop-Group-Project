package engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.Cell;


public class PotentialMoves {

    ObservableBoardData gameData;

    public PotentialMoves(ObservableBoardData gameData){
        this.gameData = gameData;
    }
    
    public Set<Cell> getKnightMoves(Cell cell){
        char[][] board = gameData.getBoard();
        char piece = board[cell.x][cell.y];
        
        Set<Cell> potentialMoves = new HashSet<>();
        // Return empty set if no valid piece on the square
        if (piece == '.'){
            return potentialMoves;
        }
        
        boolean isWhite = Character.isUpperCase(piece);
        
        // Go in layers top to bottom, left to right       
        int[][] allMoves = new int[8][2];
        allMoves[0][0] = cell.x - 1; allMoves[0][1] = cell.y + 2;
        allMoves[1][0] = cell.x + 1; allMoves[1][1] = cell.y + 2;
        allMoves[2][0] = cell.x - 2; allMoves[2][1] = cell.y + 1;
        allMoves[3][0] = cell.x + 2; allMoves[3][1] = cell.y + 1;
        allMoves[4][0] = cell.x - 2; allMoves[4][1] = cell.y - 1;
        allMoves[5][0] = cell.x + 2; allMoves[5][1] = cell.y - 1;
        allMoves[6][0] = cell.x - 1; allMoves[6][1] = cell.y - 2;
        allMoves[7][0] = cell.x + 1; allMoves[7][1] = cell.y - 2;
        
        // Filter out all cells which do not exist on the board
        Arrays.stream(allMoves)
                .filter((move) -> (move[0] >= 0 && move[0] <= 7))
                .filter((move) -> (move[1] >= 0 && move[1] <= 7))
                .forEach((move) -> {
            char dest = board[move[0]][move[1]];
            
            // Ensure you're not moving onto a square you already occupy
            if (dest == '.' || (Character.isUpperCase(dest) ^ isWhite)){
                potentialMoves.add(new Cell(move[0], move[1]));
            }
        });
        return potentialMoves;
    }
    
    public Set<Cell> getPawnMoves(Cell cell){
        char[][] board = gameData.getBoard();
        char piece = board[cell.x][cell.y];
        
        Set<Cell> potentialMoves = new HashSet<>();
        // Return empty set if no valid piece on the square
        if (piece == '.'){
            return potentialMoves;
        }
        
        boolean isWhite = Character.isUpperCase(piece);
        
        if(isWhite){
            // First check they're not at the end of the board
            if (cell.y < 7){
                // First check forward movement
                char fwdDest = board[cell.x][cell.y + 1];
                if (fwdDest == '.'){
                    potentialMoves.add(new Cell(cell.x, cell.y + 1));
                }
                
                // Check double forward
                if (cell.y == 1){
                    char fwdDest2 = board[cell.x][cell.y + 2];
                    if (fwdDest2 == '.'){
                        potentialMoves.add(new Cell(cell.x, cell.y + 2));
                    }
                }
                
                // Check for en passant and place a face oppenent piece on 
                // this square 
                if (!gameData.getEnPassant().equals("-")){
                    char fileChr = gameData.getEnPassant().charAt(0);
                    int rank = Character.getNumericValue(gameData.getEnPassant().charAt(1) - 1);
                    int file = fileChr - 'a';
                    board[file][rank] = 'p';
//                    System.out.println("Rank: " + rank + ", File: " + file);
                }
                
                // Now check for diagonal attacks
                if (cell.x < 7){
                    char right = board[cell.x + 1][cell.y + 1];
                    if (right != '.' && !Character.isUpperCase(right)){
                        potentialMoves.add(new Cell(cell.x + 1, cell.y + 1));
                    }
                }
                if (cell.x > 0){
                    char left = board[cell.x - 1][cell.y + 1];
                    if (left != '.' && !Character.isUpperCase(left)){
                        potentialMoves.add(new Cell(cell.x - 1, cell.y + 1));
                    }
                }
                
            }
        }else{
            // Repeat for black:
            
            // First check they're not at the end of the board
            if (cell.y > 0){
                // First check forward movement
                char fwdDest = board[cell.x][cell.y - 1];
                if (fwdDest == '.'){
                    potentialMoves.add(new Cell(cell.x, cell.y - 1));
                }
                
                // Check double forward
                if (cell.y == 6){
                    char fwdDest2 = board[cell.x][cell.y - 2];
                    if (fwdDest2 == '.'){
                        potentialMoves.add(new Cell(cell.x, cell.y - 2));
                    }
                }
                
                // Check for en passant and place a face oppenent piece on 
                // this square 
                if (!gameData.getEnPassant().equals("-")){
                    char fileChr = gameData.getEnPassant().charAt(0);
                    int rank = Character.getNumericValue(gameData.getEnPassant().charAt(1)) - 1;
                    int file = fileChr - 'a';
                    board[file][rank] = 'P';
                }
                
                // Now check for diagonal attacks
                if (cell.x < 7){
                    char right = board[cell.x + 1][cell.y - 1];
                    if (right != '.' && Character.isUpperCase(right)){
                        potentialMoves.add(new Cell(cell.x + 1, cell.y - 1));
                    }
                }
                if (cell.x > 0){
                    char left = board[cell.x - 1][cell.y - 1];
                    if (left != '.' && Character.isUpperCase(left)){
                        potentialMoves.add(new Cell(cell.x - 1, cell.y - 1));
                    }
                }
                
            }
        }
        return potentialMoves;
    }
    
    public Set<Cell> getRookMoves(Cell cell){
    	char[][] board = gameData.getBoard();
        char piece = board[cell.x][cell.y];        
        Set<Cell> potentialMoves = new HashSet<>();
        
//        // Return empty set if no valid piece on the square
//        if ( !(piece == 'r' || piece == 'R') )  {
//            return potentialMoves;
//        }
        
        boolean isWhite = Character.isUpperCase(piece);
        
        // up
        for(int j=cell.y+1; j<8; j++){
        	Cell c = new Cell(cell.x,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        //down
        for(int j=cell.y-1; j>=0; j--){
        	Cell c = new Cell(cell.x,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        //left
        for(int i=cell.x-1; i>=0; i--){
        	Cell c = new Cell(i,cell.y);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        //right
        for(int i=cell.x+1; i<8; i++){
        	Cell c = new Cell(i,cell.y);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
    	
        return potentialMoves;
    }
    
    public Set<Cell> getBishopMoves(Cell cell){
    	char[][] board = gameData.getBoard();
        char piece = board[cell.x][cell.y];        
        Set<Cell> potentialMoves = new HashSet<>();
        
//        // Return empty set if no valid piece on the square
//        if ( !(piece == 'b' || piece == 'B') )  {
//            return potentialMoves;
//        }
        
        boolean isWhite = Character.isUpperCase(piece);
        
        // up-right
        int i=cell.x, j=cell.y;
        i++; j++;
        while (i<8 && j<8) {
        	Cell c = new Cell(i,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		i++; j++;
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        // up-left
        i=cell.x;
        j=cell.y;
        i--; j++;
        while (i>=0 && j<8) {
        	Cell c = new Cell(i,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		i--; j++;
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        // down-right
        i=cell.x;
        j=cell.y;
        i++; j--;
        while (i<8 && j>=0) {
        	Cell c = new Cell(i,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		i++; j--;
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        // down-left
        i=cell.x;
        j=cell.y;
        i--; j--;
        while (i>=0 && j>=0) {
        	Cell c = new Cell(i,j);
        	char square = board[c.x][c.y];
        	// empty square
        	if (square == '.') {
        		potentialMoves.add(c);
//        		System.out.println(c);
        		i--; j--;
        		continue;
        	// friendly piece on square
        	}else if(Character.isUpperCase(square) == isWhite){
        		break;
        	// enemy piece on square
        	}else{
        		potentialMoves.add(c);
//        		System.out.println(c);
        		break;
        	}
        }
        
        return potentialMoves;
    }
    
    public Set<Cell> getQueenMoves(Cell cell){
    	
        Set<Cell> moves = getRookMoves(cell);
        moves.addAll(getBishopMoves(cell));
        return moves;
    }
    
    // NOT COMPLETED, ONLY DONE FOR SINGLE MOVES
    public Set<Cell> getKingMoves(Cell cell){
    	char[][] board = gameData.getBoard();
    	
        char piece = board[cell.x][cell.y];
        boolean isWhite = Character.isUpperCase(piece);
        
        Set<Cell> potentialMoves = new HashSet<>();
        
//        // Return empty set if no valid piece on the square
//        if ( !(piece == 'k' || piece == 'K') )  {
//            return potentialMoves;
//        }
        
        // Go in layers left to right      
        int[][] allMoves = new int[8][2];
        allMoves[0][0] = cell.x - 1; allMoves[0][1] = cell.y - 1;
        allMoves[1][0] = cell.x - 1; allMoves[1][1] = cell.y;
        allMoves[2][0] = cell.x - 1; allMoves[2][1] = cell.y + 1;
        allMoves[3][0] = cell.x; allMoves[3][1] = cell.y + 1;
        allMoves[4][0] = cell.x; allMoves[4][1] = cell.y - 1;
        allMoves[5][0] = cell.x + 1; allMoves[5][1] = cell.y - 1;
        allMoves[6][0] = cell.x + 1; allMoves[6][1] = cell.y;
        allMoves[7][0] = cell.x + 1; allMoves[7][1] = cell.y + 1;
        
        // Filter out all cells which do not exist on the board
        for (int[] move : allMoves){
        	if(move[0] >= 0 && move[0] <= 7){
        		if((move[1] >= 0 && move[1] <= 7)){
        			char square = board[move[0]][move[1]];
        			if(square == '.' || Character.isUpperCase(square) != isWhite){ 		// && !isCellThreatened(new Cell(move[0],move[1]), isWhite)
//                		// Make the move
//                		char[][] tempBoard = gameData.getBoard();
//                		
//                    	tempBoard[move[0]][move[1]] = tempBoard[cell.x][cell.y];
//                    	tempBoard[cell.x][cell.y] = '.';
//                    	gameData.setBoard(tempBoard);
//                    	
//                    	if (!isCheck(Character.isUpperCase(piece))){
                    		potentialMoves.add(new Cell(move[0], move[1]));
//                    	}
//                  		// Revert board
//                        gameData.setBoard(board);
                	}
        		}
        	}
        }        
        
        // Castling
        // Check the FEN
        String castling = gameData.getCastlingData();
        System.out.println(castling);
        System.out.println(isWhite);
        if (isWhite){
        	if (castling.contains("K")){
        		// Check if path is clear
        		if(board[5][0]=='.' && board[6][0]=='.'){
        			// Check if cells are threatened
            		if(!isCellThreatened(new Cell(5,0), isWhite) && !isCellThreatened(new Cell(6,0), isWhite)){
            			potentialMoves.add(new Cell(6, 0));
            		}
        		}
        	}
        	if (castling.contains("Q")){
        		// Check if path is clear
        		if(board[3][0]=='.' && board[2][0]=='.'){
        			// Check if cells are threatened
            		if(!isCellThreatened(new Cell(3,0), isWhite) && !isCellThreatened(new Cell(2,0), isWhite)){
            			potentialMoves.add(new Cell(2, 0));
            		}
        		}
        	}
        }else{
        	if (castling.contains("k")){
        		// Check if path is clear
        		if(board[5][7]=='.' && board[6][7]=='.'){
        			// Check if cells are threatened
            		if(!isCellThreatened(new Cell(5,7), isWhite) && !isCellThreatened(new Cell(6,7), isWhite)){
            			potentialMoves.add(new Cell(6, 7));
            		}
        		}
        	}
        	if (castling.contains("q")){
        		// Check if path is clear
        		if(board[3][7]=='.' && board[2][7]=='.'){
        			// Check if cells are threatened
            		if(!isCellThreatened(new Cell(3,7), isWhite) && !isCellThreatened(new Cell(2,7), isWhite)){
            			potentialMoves.add(new Cell(2, 7));
            		}
        		}
        	}
        }
        
        
        
        return potentialMoves;
    }
      
    public boolean isCellThreatened(Cell cell, boolean isWhite){
        // Store original piece
        char[][] board = gameData.getBoard();
        char oldPiece = board[cell.x][cell.y];
        // Set square to correct team, safe as getBoard() return a copy
        board[cell.x][cell.y] = isWhite ? 'P' : 'p';
        
        gameData.setBoard(board);
        
        // First check for knight attacks
        Set<Cell> knightMoves = getKnightMoves(cell);
        for (Cell move : knightMoves) {
            char piece = Character.toLowerCase(board[move.x][move.y]);
            if (piece == 'n'){
                board[cell.x][cell.y] = oldPiece;
                gameData.setBoard(board);
                return true;
            }
        }
        
        // Check for rooks and queens
        Set<Cell> rookMoves = getRookMoves(cell);
        for (Cell move : rookMoves) {
            char piece = Character.toLowerCase(board[move.x][move.y]);
            if (piece == 'r'){
                board[cell.x][cell.y] = oldPiece;
                gameData.setBoard(board);
                return true;
            }
            if (piece == 'q'){
                board[cell.x][cell.y] = oldPiece;
                gameData.setBoard(board);
                return true;
            }
            
        }
        
        // Check for rooks and queens
        Set<Cell> bishopMoves = getBishopMoves(cell);
        for (Cell move : bishopMoves) {
            char piece = Character.toLowerCase(board[move.x][move.y]);
            if (piece == 'b'){
                board[cell.x][cell.y] = oldPiece;
                return true;
            }
            if (piece == 'q'){
                board[cell.x][cell.y] = oldPiece;
                gameData.setBoard(board);
                return true;
            }
            
        }
        
        // Manually check for kings
        // Go in layers left to right      
        int[][] allKingMoves = new int[8][2];
        allKingMoves[0][0] = cell.x - 1; allKingMoves[0][1] = cell.y - 1;
        allKingMoves[1][0] = cell.x - 1; allKingMoves[1][1] = cell.y;
        allKingMoves[2][0] = cell.x - 1; allKingMoves[2][1] = cell.y + 1;
        allKingMoves[3][0] = cell.x; allKingMoves[3][1] = cell.y + 1;
        allKingMoves[4][0] = cell.x; allKingMoves[4][1] = cell.y - 1;
        allKingMoves[5][0] = cell.x + 1; allKingMoves[5][1] = cell.y - 1;
        allKingMoves[6][0] = cell.x + 1; allKingMoves[6][1] = cell.y;
        allKingMoves[7][0] = cell.x + 1; allKingMoves[7][1] = cell.y + 1;
        
        // Filter out all cells which do not exist on the board
        for (int[] move : allKingMoves){
            if(move[0] >= 0 && move[0] <= 7){
                if((move[1] >= 0 && move[1] <= 7)){
                    // Only deal with it if the piece is a king
                    char piece = board[move[0]][move[1]];
                    if (piece == 'k' || piece == 'K'){
                        boolean pieceWhite = Character.isUpperCase(piece);
                        // If the king is on the opposite team, threatened!
                        if (isWhite ^ pieceWhite){
                            board[cell.x][cell.y] = oldPiece;
                            gameData.setBoard(board);
                            return true;
                        }
                    }
                }
            }
        }
        
        // Manually check for pawns
        if(isWhite){
            if (cell.y < 7){
                if (cell.x < 7){
                    if (board[cell.x + 1][cell.y + 1] == 'p'){
                        board[cell.x][cell.y] = oldPiece;
                        gameData.setBoard(board);
                        return true;
                    }
                }
                if(cell.x > 0){
                    if (board[cell.x - 1][cell.y + 1] == 'p'){
                        board[cell.x][cell.y] = oldPiece;
                        gameData.setBoard(board);
                        return true;
                    }
                }
            }
        }else{
            if (cell.y > 0){
                if (cell.x < 7){
                    if (board[cell.x + 1][cell.y - 1] == 'P'){
                        board[cell.x][cell.y] = oldPiece;
                        gameData.setBoard(board);
                        return true;
                    }
                }
                if(cell.x > 0){
                    if (board[cell.x - 1][cell.y - 1] == 'P'){
                        board[cell.x][cell.y] = oldPiece;
                        gameData.setBoard(board);
                        return true;
                    }
                }
            }
        }
        
        board[cell.x][cell.y] = oldPiece;
        gameData.setBoard(board);
        return false;
        
    }
    
    public boolean isCellThreatened(Cell cell){
        // Find out teams colour
        char piece = gameData.getBoard()[cell.x][cell.y];
        
        // If no piece then just return false (invalid query)
        if (piece == '.'){
            return false;
        }
        
        // Else check with reference to which team the piece belongs to
        return isCellThreatened(cell, Character.isUpperCase(piece));
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
    
	public static void main(String[] args) {
		String fen = "rnbqkbnr/pp1ppppp/8/2p5/4K3/5N2/1PPP1PPP/RNBQK2R b KQkq - 1 2";
		Game chess = new Game(fen);
		chess.printBoard();
		System.out.println(chess);
		System.out.println(chess.toFen().equals(fen));
		
		PotentialMoves moves = new PotentialMoves(chess);
//		moves.getRookMoves(new Cell(4,3));
//		moves.getBishopMoves(new Cell(4,3));
//		moves.getQueenMoves(new Cell(4,3));
		Set<Cell> availableMoves = moves.getKingMoves(new Cell(4,0));
		for (Cell c : availableMoves){
			System.out.println(c);
		}
	}
    
}
