package chess;

import java.util.ArrayList;
import java.util.Collection;


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


    private void scanMoveLine(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition, int right, int up) {
        throw new RuntimeException("Not implemented");
    }

    private void addRookMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }


    private void addBishopMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }


    private void addKnightMoves(Collection<ChessMove> addTo, ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
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
        Collection<ChessMove> moves = new ArrayList<>();
        switch (type) {
            case KNIGHT:
                addKnightMoves(moves, board, myPosition);
            case ROOK:
                addRookMoves(moves, board, myPosition);
            case BISHOP:
                addBishopMoves(moves, board, myPosition);
            case QUEEN:
                addRookMoves(moves, board, myPosition);
                addBishopMoves(moves, board, myPosition);
            case KING:
                addKingMoves(moves, board, myPosition);
            case PAWN:
                addPawnMoves(moves, board, myPosition);
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
