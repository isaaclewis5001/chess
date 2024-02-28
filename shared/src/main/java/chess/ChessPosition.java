package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessPosition {
    private final int row;
    private final int column;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.column = col;
    }

    public ChessPosition(ChessPosition toCopy) {
        this(toCopy.row, toCopy.column);
    }

    public static ChessPosition fromIndex(int index) {
        return new ChessPosition((index & 0b00000111) + 1, (index >> 3) + 1);
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
     * @return The translated position.
     * @param rowOff How far to translate forward.
     * @param colOff How for to translate rightwards.
     */
    public ChessPosition getOffset(int rowOff, int colOff) {
        return new ChessPosition(row + rowOff, column + colOff);
    }

    public ChessPosition copy() {
        return new ChessPosition(this);
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


    public String toString() {
        char rowChar = (char)('1' - 1 + this.row);
        char colChar = (char)('a' - 1 + this.column);

        return String.format("%c%c", colChar, rowChar);
    }


    /**
     * @return An index in the range [0...63] unique to this square on the chessboard.
     */
    public int getIndex() {
        return row + column * 8 - 9;
    }
}
