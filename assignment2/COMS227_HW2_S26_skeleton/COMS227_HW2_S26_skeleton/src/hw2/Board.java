package hw2;

import static api.Direction.*;
import static api.Orientation.*;

import java.util.ArrayList;

import api.Boulder;
import api.Cell;
import api.Direction;
import api.Move;

/**
 * Represents a board in the game. A board contains a 2D grid of cells and a
 * list of boulders that slide over the cells.
 *
 * @author Simon S.
 */
public class Board {
	/**
	 * 2D array of cells, the indexes signify (row, column) with (0, 0) representing
	 * the upper-left corner of the board.
	 */
	private Cell[][] grid;

	/**
	 * A list of boulders that are positioned on the board.
	 */
	private ArrayList<Boulder> boulders;

	/**
	 * A list of moves that have been made in order to get to the current position
	 * of boulders on the board.
	 */
	private ArrayList<Move> moveHistory;

	/**
	 * The boulder currently grabbed by the user, or null if none is grabbed.
	 */
	private Boulder grabbedBoulder;

	/**
	 * The total number of successful moves made so far in the game.
	 */
	private int moveCount;

	/**
	 * Whether the game is currently over.
	 */
	private boolean gameOver;

	/**
	 * Constructs a new board from a given 2D array of cells and list of boulders. The
	 * cells of the grid should be updated to indicate which cells have boulders
	 * placed over them (i.e., placeBoulder() method of Cell). The move history should
	 * be initialized as empty.
	 *
	 * @param grid   a 2D array of cells which is expected to be a rectangular shape
	 * @param boulders list of boulders already containing row-column position which
	 *               should be placed on the board
	 */
	public Board(Cell[][] grid, ArrayList<Boulder> boulders) {
		this.grid = grid;
		this.boulders = boulders;
		this.moveHistory = new ArrayList<Move>();
		this.grabbedBoulder = null;
		this.moveCount = 0;
		this.gameOver = false;

		// place boulders onto grid cells occupied
		for (Boulder b : boulders) {
			placeBoulderOnGrid(b);
		}
	}

	/**
	 * DO NOT MODIFY THIS CONSTRUCTOR
	 * <p>
	 * Constructs a new board from a given 2D array of String descriptions.
	 *
	 * @param desc 2D array of descriptions
	 */
	public Board(String[][] desc) {
		this(GridUtil.createGrid(desc), GridUtil.findBoulders(desc));
	}

	/**
	 * Returns the number of rows of the board.
	 *
	 * @return number of rows
	 */
	public int getRowSize() {
		return grid.length;
	}

	/**
	 * Returns the number of columns of the board.
	 *
	 * @return number of columns
	 */
	public int getColSize() {
		return grid[0].length;
	}

	/**
	 * Returns the cell located at a given row and column.
	 *
	 * @param row the given row
	 * @param col the given column
	 * @return the cell at the specified location
	 */
	public Cell getCellAt(int row, int col) {
		return grid[row][col];
	}

	/**
	 * Returns the total number of moves (calls to moveGrabbedBoulder which
	 * resulted in a boulder being moved) made so far in the game.
	 *
	 * @return the number of moves
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Returns a list of all boulders on the board.
	 *
	 * @return a list of all boulders
	 */
	public ArrayList<Boulder> getBoulders() {
		return boulders;
	}

	/**
	 * Returns true if the player has completed the puzzle by positioning a boulder
	 * over an exit, false otherwise.
	 *
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Models the user grabbing (mouse button down) a boulder over the given row and
	 * column. The purpose of grabbing a boulder is for the user to be able to drag
	 * the boulder to a new position, which is performed by calling
	 * moveGrabbedBoulder().
	 * <p>
	 * This method should find which boulder has been grabbed (if any) and record
	 * that boulder as grabbed in some way.
	 *
	 * @param row row to grab the boulder from
	 * @param col column to grab the boulder from
	 */
	public void grabBoulderAt(int row, int col) {
		grabbedBoulder = null;
		for (Boulder b : boulders) {
			if (boulderCoversCell(b, row, col)) {
				grabbedBoulder = b;
				return;
			}
		}
	}

	/**
	 * Models the user releasing (mouse button up) the currently grabbed boulder
	 * (if any). Update the object accordingly to indicate no boulder is
	 * currently being grabbed.
	 */
	public void releaseBoulder() {
		grabbedBoulder = null;
	}

	/**
	 * Returns the currently grabbed boulder. If there is no currently grabbed
	 * boulder the method return null.
	 *
	 * @return the currently grabbed boulder or null if none
	 */
	public Boulder getGrabbedBoulder() {
		return grabbedBoulder;
	}

	/**
	 * Returns true if the cell at the given row and column is available for a
	 * boulder to be placed over it. Boulders can only be placed over ground
	 * and exits. Additionally, a boulder cannot be placed over a cell that is
	 * already occupied by another boulder.
	 *
	 * @param row row location of the cell
	 * @param col column location of the cell
	 * @return true if the cell is available for a boulder, otherwise false
	 */
	public boolean isAvailable(int row, int col) {
		if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
			return false;
		}
		Cell cell = grid[row][col];
		return !cell.isWall() && !cell.hasBoulder();
	}

	/**
	 * Moves the currently grabbed boulder by one cell in the given direction. A
	 * horizontal boulder is only allowed to move right and left and a vertical boulder
	 * is only allowed to move up and down. A boulder can only move over a cell that
	 * is a floor or exit and is not already occupied by another boulder. The method
	 * does nothing under any of the following conditions:
	 * <ul>
	 * <li>The game is over.</li>
	 * <li>No boulder is currently grabbed by the user.</li>
	 * <li>A boulder is currently grabbed by the user, but the boulder is not allowed to
	 * move in the given direction.</li>
	 * </ul>
	 * If none of the above conditions are meet, the method does at least the following:
	 * <ul>
	 * <li>Moves the boulder object by calling its move() method.</li>
	 * <li>Calls placeBoulder() for the grid cell that the boulder is being moved into.</li>
	 * <li>Calls removeBoulder() for the grid cell that the boulder is being moved out of.</li>
	 * <li>Adds the move (as a Move object) to the end of the move history list.</li>
	 * <li>Increments the count of total moves made in the game.</li>
	 * </ul>
	 *
	 * @param dir the direction to move
	 */
	public void moveGrabbedBoulder(Direction dir) {
		if (gameOver || grabbedBoulder == null) {
			return;
		}

		Boulder b = grabbedBoulder;
		int firstRow = b.getFirstRow();
		int firstCol = b.getFirstCol();
		int length = b.getLength();

		if (b.getOrientation() == HORIZONTAL) {
			if (dir == LEFT) {
				if (!isAvailable(firstRow, firstCol - 1)) {
					return;
				}

				grid[firstRow][firstCol - 1].placeBoulder(b);
				grid[firstRow][firstCol + length - 1].removeBoulder();
				b.move(LEFT);
				moveHistory.add(new Move(b, LEFT));
				moveCount++;
			} else if (dir == RIGHT) {
				if (!isAvailable(firstRow, firstCol + length)) {
					return;
				}

				grid[firstRow][firstCol + length].placeBoulder(b);
				grid[firstRow][firstCol].removeBoulder();
				b.move(RIGHT);
				moveHistory.add(new Move(b, RIGHT));
				moveCount++;
			}

		} else {
			if (dir == UP) {
				if (!isAvailable(firstRow - 1, firstCol)) {
					return;
				}

				grid[firstRow - 1][firstCol].placeBoulder(b);
				grid[firstRow + length - 1][firstCol].removeBoulder();
				b.move(UP);
				moveHistory.add(new Move(b, UP));
				moveCount++;
			} else if (dir == DOWN) {
				if (!isAvailable(firstRow + length, firstCol)) {
					return;
				}

				grid[firstRow + length][firstCol].placeBoulder(b);
				grid[firstRow][firstCol].removeBoulder();
				b.move(DOWN);
				moveHistory.add(new Move(b, DOWN));
				moveCount++;
			}
		}

		checkGameOver();
	}

	/**
	 * Resets the state of the game back to the start, which includes the move
	 * count, the move history, and whether the game is over. The method calls the
	 * reset method of each boulder object. It also updates each grid cells by calling
	 * their placeBoulder method to either set a boulder if one is located over the cell
	 * or set null if no boulder is located over the cell.
	 */
	public void reset() {
		for (Boulder b : boulders) {
			b.reset();
		}

		for (int r = 0; r < getRowSize(); r++) {
			for (int c = 0; c < getColSize(); c++) {
				grid[r][c].removeBoulder();
			}
		}
		for (Boulder b : boulders) {
			placeBoulderOnGrid(b);
		}

		moveCount = 0;
		moveHistory = new ArrayList<Move>();
		gameOver = false;
		grabbedBoulder = null;
	}

	/**
	 * Returns a list of all legal moves that can be made by any boulder on the
	 * current board.
	 *
	 * @return a list of legal moves
	 */
	public ArrayList<Move> getAllPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();

		for (Boulder b : boulders) {
			int firstRow = b.getFirstRow();
			int firstCol = b.getFirstCol();
			int length = b.getLength();

			if (b.getOrientation() == HORIZONTAL) {
				if (isAvailable(firstRow, firstCol - 1)) {
					moves.add(new Move(b, LEFT));
				}
				if (isAvailable(firstRow, firstCol + length)) {
					moves.add(new Move(b, RIGHT));
				}
			} else {
				if (isAvailable(firstRow - 1, firstCol)) {
					moves.add(new Move(b, UP));
				}
				if (isAvailable(firstRow + length, firstCol)) {
					moves.add(new Move(b, DOWN));
				}
			}
		}

		return moves;
	}

	/**
	 * Gets the list of all moves performed to get to the current position on the
	 * board.
	 *
	 * @return a list of moves performed to get to the current position
	 */
	public ArrayList<Move> getMoveHistory() {
		return moveHistory;
	}

	/**
	 * This method is only used by the Solver.
	 * <p>
	 * Undo the previous move. The method gets the last move on the moveHistory list
	 * and performs the opposite actions of that move, which are the following:
	 * <ul>
	 * <li>grabs the moved boulder and calls moveGrabbedBoulder passing the opposite
	 * direction</li>
	 * <li>decreases the total move count by two to undo the effect of calling
	 * moveGrabbedBoulder twice</li>
	 * <li>if required, sets is game over to false</li>
	 * <li>removes the move from the moveHistory list</li>
	 * </ul>
	 * If the moveHistory list is empty this method does nothing.
	 */
	public void undoMove() {
		if (moveHistory.isEmpty()) {
			return;
		}

		Move lastMove = moveHistory.get(moveHistory.size() - 1);
		Boulder b = lastMove.getBoulder();
		Direction dir = lastMove.getDirection();

		gameOver = false;
		grabBoulderAt(b.getFirstRow(), b.getFirstCol());
		moveGrabbedBoulder(oppositeDirection(dir));

		moveCount -= 2;
		moveHistory.remove(moveHistory.size() - 1);
		moveHistory.remove(moveHistory.size() - 1);

		gameOver = false;
		releaseBoulder();
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		for (Cell row[] : grid) {
			if (!first) {
				buff.append("\n");
			} else {
				first = false;
			}
			for (Cell cell : row) {
				buff.append(cell.toString());
				buff.append(" ");
			}
		}
		return buff.toString();
	}

	/**
	 * Places a boulder on every grid cell it currently occupies.
	 *
	 * @param b the boulder to place
	 */
	private void placeBoulderOnGrid(Boulder b) {
		int firstRow = b.getFirstRow();
		int firstCol = b.getFirstCol();
		int length = b.getLength();

		if (b.getOrientation() == HORIZONTAL) {
			for (int c = firstCol; c < firstCol + length; c++) {
				grid[firstRow][c].placeBoulder(b);
			}
		} else {
			for (int r = firstRow; r < firstRow + length; r++) {
				grid[r][firstCol].placeBoulder(b);
			}
		}
	}

	/**
	 * Returns true if the given boulder covers the cell at (row, col).
	 *
	 * @param b   the boulder to check
	 * @param row the row to check
	 * @param col the column to check
	 * @return true if the boulder covers that cell
	 */
	private boolean boulderCoversCell(Boulder b, int row, int col) {
		int firstRow = b.getFirstRow();
		int firstCol = b.getFirstCol();
		int length = b.getLength();

		if (b.getOrientation() == HORIZONTAL) {
			return row == firstRow && col >= firstCol && col < firstCol + length;
		} else {
			return col == firstCol && row >= firstRow && row < firstRow + length;
		}
	}

	/**
	 * Checks all boulders to see if any segment sits on an exit cell,
	 * and sets gameOver accordingly.
	 */
	private void checkGameOver() {
		for (Boulder b : boulders) {
			int firstRow = b.getFirstRow();
			int firstCol = b.getFirstCol();
			int length = b.getLength();

			if (b.getOrientation() == HORIZONTAL) {
				for (int c = firstCol; c < firstCol + length; c++) {
					if (grid[firstRow][c].isExit()) {
						gameOver = true;
						return;
					}
				}
			} else {
				for (int r = firstRow; r < firstRow + length; r++) {
					if (grid[r][firstCol].isExit()) {
						gameOver = true;
						return;
					}
				}
			}
		}
	}

	/**
	 * Returns the direction opposite to the one given.
	 *
	 * @param dir the direction to reverse
	 * @return the opposite direction
	 */
	private Direction oppositeDirection(Direction dir) {
		if (dir == LEFT)  return RIGHT;
		if (dir == RIGHT) return LEFT;
		if (dir == UP)    return DOWN;
		return UP;
	}
}


