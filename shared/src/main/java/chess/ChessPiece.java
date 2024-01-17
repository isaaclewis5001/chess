package chess;

import java.util.Collection;
import java.util.HashSet;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    /**
     * @return Whether this piece can capture the given piece, or false if it is null.
     * @param other The chess piece to be captured, or null for an empty square.
     * **/
    public boolean canCapture(ChessPiece other) {
        if (other == null) {
            return false;
        }
        return other.pieceColor != pieceColor;
    }


    private boolean addMove(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition, ChessPosition newPosition) {
        if (!newPosition.isOnBoard()) {
            return false;
        }
        ChessPiece piece = board.getPiece(newPosition);
        if (piece == null || canCapture(piece)) {
            addTo.add(new ChessMove(myPosition, newPosition, null));
            return piece == null;
        }
        return false;
    }

    private void scanMoveLine(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition, int up, int right) {
        ChessPosition newPos = myPosition.copy();
        do {
            newPos.offset(right, up);
        } while(addMove(addTo, board, myPosition, newPos));
    }

    private void addRookMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        scanMoveLine(addTo, board, myPosition, 1, 0);
        scanMoveLine(addTo, board, myPosition, -1, 0);
        scanMoveLine(addTo, board, myPosition, 0, 1);
        scanMoveLine(addTo, board, myPosition, 0, -1);
    }


    private void addBishopMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        scanMoveLine(addTo, board, myPosition, 1, 1);
        scanMoveLine(addTo, board, myPosition, -1, 1);
        scanMoveLine(addTo, board, myPosition, 1, -1);
        scanMoveLine(addTo, board, myPosition, -1, -1);
    }


    private void addKnightMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        addMove(addTo, board, myPosition, myPosition.getOffset(1,2));
        addMove(addTo, board, myPosition, myPosition.getOffset(-1,2));
        addMove(addTo, board, myPosition, myPosition.getOffset(1,-2));
        addMove(addTo, board, myPosition, myPosition.getOffset(-1,-2));
        addMove(addTo, board, myPosition, myPosition.getOffset(2,1));
        addMove(addTo, board, myPosition, myPosition.getOffset(2,-1));
        addMove(addTo, board, myPosition, myPosition.getOffset(-2,1));
        addMove(addTo, board, myPosition, myPosition.getOffset(-2,-1));
    }

    private void addKingMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    private void addPawnMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        switch (type) {
            case KNIGHT:
                addKnightMoves(moves, board, myPosition);
                break;
            case ROOK:
                addRookMoves(moves, board, myPosition);
                break;
            case BISHOP:
                addBishopMoves(moves, board, myPosition);
                break;
            case QUEEN:
                addRookMoves(moves, board, myPosition);
                addBishopMoves(moves, board, myPosition);
                break;
            case KING:
                addKingMoves(moves, board, myPosition);
                break;
            case PAWN:
                addPawnMoves(moves, board, myPosition);
                break;
        }
        return moves;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece other) {
            return (other.pieceColor == pieceColor) && (other.type == type);
        }
        return false;
    }
}
