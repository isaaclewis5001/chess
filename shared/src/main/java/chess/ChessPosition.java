package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int column;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * @return Which row this position is in, with 1
     * coding for the bottom row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return Which column this position is in, with 1 coding for the left column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return Whether the position represents a position that is on the chessboard.
     **/
    public boolean isOnBoard() {
        return (
                row >= 1 &&
                column >= 1 &&
                row <= 8 &&
                column <= 8
        );
    }

    /**
     * Applies a translation to this position.
     * @param row_off How far to translate forward.
     * @param col_off How for to translate rightwards.
     */
    public void offset(int row_off, int col_off) {
        row += row_off;
        column += col_off;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPosition) {
            return (
                    ((ChessPosition)obj).row == row &&
                    ((ChessPosition)obj).column == column
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return row << 16 | column;
    }

    /**
     * @return An index in the range [0...63] unique to this square on the chessboard.
     */
    public int getIndex() {
        return row + column * 8 - 9;
    }
}
