package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[] squares;

    public ChessBoard() {
        squares = new ChessPiece[64];
    }

    public ChessBoard(ChessBoard other) {
        squares = other.squares.clone();
    }


    /**
     * Adds a chess piece to the chessboard.
     *
     * @param position Where to add the piece to.
     * @param piece    The piece to add.
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getIndex()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard.
     *
     * @param position The position to get the piece from.
     * @return Either the piece at the position, or null if no piece is at that
     * position.
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getIndex()];
    }

    public ChessPiece getPiece(int index) {
        return squares[index];
    }

    // Lists out the pieces on the first and eight ranks for resetBoard()
    private static final ChessPiece.PieceType[] PIECE_ORDER = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
    };



    /**
     * Sets the board to the default starting board.
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int file = 1; file <= 8; file++) {
            ChessPiece.PieceType pieceType = PIECE_ORDER[file - 1];
            ChessPiece whitePiece = new ChessPiece(ChessGame.TeamColor.WHITE, pieceType);
            ChessPiece blackPiece = new ChessPiece(ChessGame.TeamColor.BLACK, pieceType);
            addPiece(new ChessPosition(1, file), whitePiece);
            addPiece(new ChessPosition(2, file), whitePawn);
            for (int rank = 3; rank < 7; rank++) {
                addPiece(new ChessPosition(rank, file), null);
            }
            addPiece(new ChessPosition(7, file), blackPawn);
            addPiece(new ChessPosition(8, file), blackPiece);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessBoard other) {
            return Arrays.equals(squares, other.squares);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }

    public boolean isRowEmpty(ChessPosition start, int lengthRight) {
        for (int i = 0; i < lengthRight; i++) {
            if (getPiece(start.getOffset(0, i)) != null) {
                return false;
            }
        }
        return true;
    }

    public ChessPosition getKingSquare(ChessGame.TeamColor teamColor) {
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for (int i = 0; i < 64; i++) {
            if (king.equals(squares[i])) {
                return ChessPosition.fromIndex(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 8; row >= 1; row--) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = getPiece(new ChessPosition(row, col));
                builder.append(String.format("%-15s", piece));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
