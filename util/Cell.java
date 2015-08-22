/**
 * Cell class that combines to X and Y coordinates together
 * Once initialised can not be overwritten because properties are final
 * X and Y are also public for easy access
 */
package util;

public class Cell {
	public final int x;
	public final int y;
	
	/**
	 * 			Indexing example
	 * 
	 *         +-----------------+
	 *     7 8 | r n b q k b n r |
	 *     6 7 | p p . p p p p p |
	 *     5 6 | . . . . . . . . |
	 *     4 5 | . . p . . . . . |
	 *     3 4 | . . . . P . . . |
	 *     2 3 | . . . . . N . . |
	 *     1 2 | P P P P . P P P |
	 *     0 1 | R N B Q K B . R |
	 *      Y  +-----------------+
	 *       X   a b c d e f g h  
	 *           0 1 2 3 4 5 6 7 
     *     
     *     board[X][Y]
     *     board[4][0] -> [e1] -> K
	 */
	
	/**
	 * Constructor
	 * @param x, int
	 * @param y, int
	 */
	public Cell(int x, int y) {
		if(x<0 || x>7 || y<0 || y>7 ) throw new IndexOutOfBoundsException("Each of the coordinates should be in the [0:7] range");
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Equals method
	 * @param other, Cell
	 * @return True if both coordinates are the same
	 */
	public boolean equals(Cell other){
		if(this.x == other.x && this.y == other.y) return true;
		return false;
	}
	
	/**
	 * To string method
	 * TODO: use standard Chess notation
	 */
	@Override
	public String toString() {
		
		return "[" + (char)(97+x) + (y+1) + "]";
	}


		@Override
        public int hashCode() {
                int hash = 7;
                hash = 31 * hash + this.x;
                hash = 31 * hash + this.y;
                return hash;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Cell other = (Cell) obj;
                if (this.x != other.x) {
                    return false;
                }
                if (this.y != other.y) {
                    return false;
                }
                return true;
        }
        
        /**
         * Getter for X
         * @return integer value of X
         */
        
        public int getX() {
    		return x;
    	}
        
        /**
         * Getter for Y
         * @return integer value of Y
         */

    	public int getY() {
    		return y;
    	}

        

}
