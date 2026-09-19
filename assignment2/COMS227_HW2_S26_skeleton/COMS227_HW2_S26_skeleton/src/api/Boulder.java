package api;

import static api.Direction.*;
import static api.Orientation.HORIZONTAL;
import static api.Orientation.VERTICAL;

/**
 * Represents a boulder in the game.
 */
public class Boulder {
	/**
	 * Top most row of the boulder.
	 */
	private int firstRow;
	
	/**
	 * Left most column of the boulder.
	 */
	private int firstCol;
	
	/**
	 * Length of the boulder.
	 */
	private int length;
	
	/**
	 * Orientation of the boulder.
	 */
	private Orientation orientation;
	
	/**
	 * The row that was provided to the constructor.
	 */
	private int originalRow;
	
	/**
	 * The column that was provided to the constructor.
	 */
	private int originalCol;

	/**
	 * Constructs a new Boulder with a specific location relative to the board. The
	 * upper/left most corner of the boulder is given as firstRow and firstCol. All
	 * boulders are only one cell wide. The length of the boulder is specified in cells.
	 * The boulder can either be horizontal or vertical on the board as specified by
	 * orientation.
	 * 
	 * @param firstRow    the first row that contains the boulder
	 * @param firstCol    the first column that contains the boulder
	 * @param length      boulder length in cells
	 * @param orientation either HORIZONTAL or VERTICAL
	 */
	public Boulder(int firstRow, int firstCol, int length, Orientation orientation) {
		this.firstRow = firstRow;
		this.firstCol = firstCol;
		originalRow = firstRow;
		originalCol = firstCol;
		this.length = length;
		this.orientation = orientation;
	}

	/**
	 * Resets the position of the boulder to the original firstRow and firstCol values
	 * that were passed to the constructor during initialization of the the boulder.
	 */
	public void reset() {
		firstRow = originalRow;
		firstCol = originalCol;
	}

	/**
	 * Move the boulders position by one cell in the direction specified. The boulders
	 * first column and row should be updated. The method will only move VERTICAL
	 * boulders UP or DOWN and HORIZONTAL boulders RIGHT or LEFT. Invalid movements are
	 * ignored.
	 * 
	 * @param dir direction to move (UP, DOWN, RIGHT, or LEFT)
	 */
	public void move(Direction dir) {
		if (dir == RIGHT && orientation == HORIZONTAL) {
			firstCol++;
		} else if (dir == LEFT && orientation == HORIZONTAL) {
			firstCol--;
		} else if (dir == UP && orientation == VERTICAL) {
			firstRow--;
		} else if (dir == DOWN && orientation == VERTICAL) {
			firstRow++;
		}
	}

	/**
	 * Gets the first row of the boulder on the board.
	 * 
	 * @return first row
	 */
	public int getFirstRow() {
		return firstRow;
	}

	/**
	 * Sets the first row of the boulder on the board.
	 * 
	 * @param firstRow first row
	 */
	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	/**
	 * Gets the first column of the boulder on the board.
	 * 
	 * @return first column
	 */
	public int getFirstCol() {
		return firstCol;
	}

	/**
	 * Sets the first column of the boulder on the board.
	 * 
	 * @param firstCol first column
	 */
	public void setFirstCol(int firstCol) {
		this.firstCol = firstCol;
	}

	/**
	 * Gets the length of the boulder.
	 * 
	 * @return length measured in cells
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the orientation of the boulder.
	 * 
	 * @return either VERTICAL or HORIZONTAL
	 */
	public Orientation getOrientation() {
		return orientation;
	} 
	
	@Override
	public String toString() {
		return "(row=" + getFirstRow() + ", col=" + getFirstCol() + ", len=" + getLength()
				+ ", ori=" + getOrientation() + ")";
	}
}
