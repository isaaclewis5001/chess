package chess;

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
            for (int rank = 1; rank <= 8; rank++) {
                for (int file = 1; file <= 8; file++) {
                    ChessPosition pos = new ChessPosition(rank, file);
                    ChessPiece thisPiece = getPiece(pos);
                    ChessPiece otherPiece = other.getPiece(pos);
                    if (thisPiece == null) {
                        if (otherPiece != null) {
                            return false;
                        }
                    }
                    else if (!thisPiece.equals(otherPiece)) {
                        System.out.println(pos.getIndex());
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
