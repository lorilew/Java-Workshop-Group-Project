package client.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Cell;
import client.Controller;
import client.User;
import engine.Game;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class ChessBoard extends JPanel implements java.util.Observer, MouseListener, MouseMotionListener {

	public static final long serialVersionUID = 1L;

	private JPanel[] squares = new JPanel[64];
	private JLabel chessPiece;
	private Cell initialSquareMouse;
	private boolean mouseDragged = false; //True if mouse has been dragged since clicking on a piece.
	private boolean boardFlipped = false; //Determines whether user chose to flip board, regardless of whether user is black or white.
	private boolean draggingPiece = false; //Default value (not dragging piece)
	private JPanel squarePieceIsOn; //The last square the mouse was over.
	private Game currentGame; //An object containing the current game
	private Controller controller; //Reference to controller.
	private JPanel chessBoard;
	private User user;
	private boolean isWhitePlayer;
	private JLayeredPane subChessBoard;
	private int mouseX;
	private int mouseY;

	public ChessBoard (final Controller controller, User user) {

		this.controller = controller;
                this.user = user;
		createBoard();

	}

	/**
	 * Creates the board by adding individual squares to the gridlayout.
	 */

	private void createBoard () {

		Dimension boardSize = new Dimension(472, 472);
		Dimension squareSize = new Dimension(59, 59);

		subChessBoard = new JLayeredPane();

		subChessBoard.setPreferredSize(boardSize);

		subChessBoard.addMouseListener(this );

		subChessBoard.addMouseMotionListener(this);

		chessBoard = new JPanel();
		chessBoard.setLayout(new GridLayout(8,8));

		chessBoard.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		chessBoard.setPreferredSize( boardSize );

		chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

		subChessBoard.setBorder(new EmptyBorder(0, 0, 0, 0));

		subChessBoard.add(chessBoard, JLayeredPane.DEFAULT_LAYER);

		int count = 0;

		//Map the position chess squares are added to the Grid Layout to the actual position chess pieces should be added.

		Hashtable<Integer, Integer> positionData = new Hashtable<Integer, Integer>();

		positionData.put(0, 7);
		positionData.put(1, 15);
		positionData.put(2, 23);
		positionData.put(3, 31);
		positionData.put(4, 39);
		positionData.put(5, 47);
		positionData.put(6, 55);
		positionData.put(7, 63);
		positionData.put(8, 6);
		positionData.put(9, 14);
		positionData.put(10, 22);
		positionData.put(11, 30);
		positionData.put(12, 38);
		positionData.put(13, 46);
		positionData.put(14, 54);
		positionData.put(15, 62);
		positionData.put(16, 5);
		positionData.put(17, 13);
		positionData.put(18, 21);
		positionData.put(19, 29);
		positionData.put(20, 37);
		positionData.put(21, 45);
		positionData.put(22, 53);
		positionData.put(23, 61);
		positionData.put(24, 4);
		positionData.put(25, 12);
		positionData.put(26, 20);
		positionData.put(27, 28);
		positionData.put(28, 36);
		positionData.put(29, 44);
		positionData.put(30, 52);
		positionData.put(31, 60);
		positionData.put(32, 3);
		positionData.put(33, 11);
		positionData.put(34, 19);
		positionData.put(35, 27);
		positionData.put(36, 35);
		positionData.put(37, 43);
		positionData.put(38, 51);
		positionData.put(39, 59);
		positionData.put(40, 2);
		positionData.put(41, 10);
		positionData.put(42, 18);
		positionData.put(43, 26);
		positionData.put(44, 34);
		positionData.put(45, 42);
		positionData.put(46, 50);
		positionData.put(47, 58);
		positionData.put(48, 1);
		positionData.put(49, 9);
		positionData.put(50, 17);
		positionData.put(51, 25);
		positionData.put(52, 33);
		positionData.put(53, 41);
		positionData.put(54, 49);
		positionData.put(55, 57);
		positionData.put(56, 0);
		positionData.put(57, 8);
		positionData.put(58, 16);
		positionData.put(59, 24);
		positionData.put(60, 32);
		positionData.put(61, 40);
		positionData.put(62, 48);
		positionData.put(63, 56);

		/**
		 * Iterate through all 64 squares, create each square as a new JPanel and add them to chess board.
		 */

		for (int a = 0; a < 8; a++) {     

			for (int b = 0; b < 8; b++) {

				if ((b + a) % 2 == 0) { //Even number. Light square.

					squares[positionData.get(count)] = new JPanel();

					squares[positionData.get(count)].setBackground(new Color(0xfceeeb));

				} else { //Odd number. Dark square.

					squares[positionData.get(count)] = new JPanel();

					squares[positionData.get(count)].setBackground(new Color(0xa57d76));

				}

				squares[positionData.get(count)].setName("" + positionData.get(count)); //Save name of square as the square number.

				squares[positionData.get(count)].setLayout(new BorderLayout()); 

				squares[positionData.get(count)].setPreferredSize(squareSize);

				chessBoard.add(squares[positionData.get(count)]);

				count++;

			}

		}

		this.add(subChessBoard);

	}

	/**
	 * This method updates the board after each move (or after the opponent moves a piece)
	 * @param game Game object to update the board from. Assumes a non-empty Game object with all required fields.
	 */

	private void updateBoard(Game game) {

		//Determine player first.

		this.isWhitePlayer = determinePlayer(user, game);

		//First remove all existing items on squares.

		for (JPanel s : squares) {

			s.removeAll();

		}

		this.currentGame = game;

		char[][] board = game.getBoard();

		if ((boardFlipped && isWhitePlayer) || (!boardFlipped && !isWhitePlayer)) {

			board = flipBoard(board);

		}

		int count = 0;

		JLabel pieceToAdd;

		for (char[] piece : board) {

			for (char piece2: piece) {

				switch(piece2) {

				case('R'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wr.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('P'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wp.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('B'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wb.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('N'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wn.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('Q'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wq.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('K'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/wk.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('p'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/bp.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('r'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/br.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;


				case ('b'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/bb.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;

				case ('n'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/bn.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;


				case ('q'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/bq.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;


				case ('k'):

				pieceToAdd = new JLabel(new ImageIcon("src/res/pieces/bk.png"));

				pieceToAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

				squares[count].add(pieceToAdd);

				break;
				}

				count++;

			}

		}

		//showMissingPieces(board); //Might implement this if there's time

		revalidate(); //Very important to update changes.
		repaint(); //Needed to avoid "ghost" pieces.

	}

	/**
	 * Flips the actual content of the board multidimensional array. The squares remain the same and that's handled using the flipCell method.
	 * @param board The non-empty multidimensional array containing the board acquired from the Game object.
	 * @return Returns an inverse of the multidimensional array. 
	 */

	private char[][] flipBoard(char[][] board) {

		char[][] inverseBoard = new char[8][8];

		for (int a = 0; a < board.length; a++) {

			for (int b = 0; b < board[a].length; b++) {

				inverseBoard[a][Math.abs(7 - b)] = board[a][b]; //Flips the piece.

			}

		}

		return inverseBoard;

	}

	/**
	 **  Add the selected chess piece to the dragging layer so it can be moved
	 */
	public void mousePressed(MouseEvent e)
	{

		//Find out the original square.

		Component initialSquare =  chessBoard.findComponentAt(e.getX(), e.getY());

		Component tempSquare;

		if (initialSquare instanceof JLabel) {

			initialSquare.setVisible(false);

			//It's a piece. We need to get the square under the piece.

			tempSquare =  chessBoard.findComponentAt(e.getX(), e.getY());

			initialSquare.setVisible(true);

		} else {

			//It's a square.

			tempSquare = initialSquare;

		}

		chessPiece = null;

		Component c =  this.findComponentAt(e.getX(), e.getY()); //Find the piece that mouse is on.

		if (c instanceof JPanel) {

			//It's a square. Do nothing.

			return;
		}

		draggingPiece = true; //Piece is being dragged...

		//Cast the original square to JPanel so we can get its name and derive its location from that.

		JPanel initial = (JPanel)tempSquare;

		initialSquareMouse = stringToCell(initial.getName());

		Point parent = c.getParent().getLocation();

		mouseX = parent.x - e.getX();

		mouseY = parent.y - e.getY();

		chessPiece = (JLabel)c;

		chessPiece.setLocation(e.getX() + mouseX, e.getY() + mouseY);

		subChessBoard.add(chessPiece, JLayeredPane.DRAG_LAYER);

	}

	/**
	 **  Activated when the mouse is being dragged. Uses draggingPiece variable to know whether an actual piece is being dragged, or not.
	 */

	public void mouseDragged(MouseEvent event)
	{

		if (draggingPiece) {

			mouseDragged = true;

			int xPos = event.getX() + mouseX;

			int yPos = event.getY() + mouseY;

			//Find the target square

			Component targetSquare =  chessBoard.findComponentAt((xPos + (chessPiece.getWidth() / 2)), (yPos + (chessPiece.getHeight() / 2)));

			if (targetSquare instanceof JLabel) {

				targetSquare.setVisible(false);

				//It's a piece. We need to get the square under the piece.

				squarePieceIsOn =  (JPanel)chessBoard.findComponentAt((xPos + (chessPiece.getWidth() / 2)), (yPos + (chessPiece.getHeight() / 2)));

				targetSquare.setVisible(true);

			} else if (targetSquare instanceof JPanel) {

				squarePieceIsOn = (JPanel)targetSquare;

			} else {

				//Do nothing as mouse is outside board

			}

			//Update the chess piece position.

			int chessBoardTopX = subChessBoard.getWidth() - chessPiece.getWidth();

			xPos = Math.min(xPos, chessBoardTopX);

			xPos = Math.max(xPos, 0);

			int chessBoardTopY = subChessBoard.getHeight() - chessPiece.getHeight();

			yPos = Math.min(yPos, chessBoardTopY);

			yPos = Math.max(yPos, 0);

			chessPiece.setLocation(xPos, yPos); //Chess piece should never leave chess board.

		}

	}

	/*
	 **  Drop the chess piece back onto the chess board
	 */
	public void mouseReleased(MouseEvent event)
	{

		if (mouseDragged) {

			draggingPiece = false;

			subChessBoard.setCursor(null);

			if (chessPiece == null) return; //If chess piece is null means that we're not dropping anything.

			chessPiece.setVisible(false);

			subChessBoard.remove(chessPiece);

			chessPiece.setVisible(true);

			//  This is important, make sure the piece is released within the chess board.

			int chessBoundaryX = subChessBoard.getWidth() - chessPiece.getWidth();

			int xPos = Math.min(event.getX(), chessBoundaryX);

			xPos = Math.max(xPos, 0);

			int chessBoundaryY = subChessBoard.getHeight() - chessPiece.getHeight();

			int yPos = Math.min(event.getY(), chessBoundaryY);

			yPos = Math.max(yPos, 0);

			JPanel initial = (JPanel)squarePieceIsOn;

			//Check if valid move, etc...

			Cell targetCell = stringToCell(initial.getName());

			makeMove(initialSquareMouse, targetCell); //Make the move and update the board.

			squarePieceIsOn = null; //Reseting this variable once finished using it.

		}

		mouseDragged = false;

	}

	/**
	 * Required methods
	 */

	public void mouseEntered(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {} 

	/**
	 * Flips the board retaining all coordinates, etc. Called from model.
	 */

	public void flipBoard() {

		boardFlipped = !boardFlipped; //Change board flipped status.

		updateBoard(currentGame); //Update board with current game now that the status is changed.

	}

	/**
	 * Handles making a move and sending it to the controller
	 */

	private void makeMove(Cell from, Cell to) {

                //Only perform make move if from and to are different, otherwise keep board the same.
		if (!from.equals(to) && currentGame.isWhiteTurn() == determinePlayer(user, currentGame)) { 

			if ((boardFlipped && isWhitePlayer) || (!boardFlipped && !isWhitePlayer)) {

				controller.makeMove(invertCell(from), invertCell(to));

			} else {

				controller.makeMove(from, to);

			}

		} else {

			updateBoard(this.currentGame); //Always update board when making move, even if piece dropped onto its own square.

		}

	}

	/**
	 * Inverts cell so inverted board works when sending new move.
	 */

	private Cell invertCell (Cell cell) {

		return new Cell(cell.getX(), Math.abs(7-cell.getY()));

	}

	/**
	 * Converts a string like 00 to a cell object
	 * @param The string containing the square number.
	 * @return Returns a cell that corresponds to square number.
	 */

	private Cell stringToCell(String square) {

		//Quick formula for turning the square order into cell x and y values.

		int s = Integer.parseInt(square);

		int xPosition = s / 8;

		int yPosition = s - ((s / 8) * 8);

		return new Cell(xPosition, yPosition);

	}

	/**
	 * Determines whether user is black or white.
	 * @param user The user object to retrieve the username from.
	 * @param game The game object that must contain white's and black's username.
	 * @return Returns true if player is white, false if player is black.
	 */

	private boolean determinePlayer(User user, Game game) {

		if (user.getUsername().equals(game.getWhitePlayer())) {

			return true;

		} else if (user.getUsername().equals(game.getBlackPlayer())) {

			return false;

		} else {

			//Player is neither black nor white. There's obviously an error somewhere. Throw exception and debug.

			throw new IllegalStateException();

		}

	}

	/**
	 * updates the chess board from the game object.
	 */

	public void update(Observable obs, Object obj) {


		if (obj instanceof String) { //It's a message

			String message = (String)obj; //Handling responses as strings for now.

			if (message.equals("Flip Board")) {

				flipBoard();

			}

		} else if (obj instanceof Game) {

			updateBoard((Game)obj);

		} else {

			throw new IllegalArgumentException("No such object");

		}

	}


}
