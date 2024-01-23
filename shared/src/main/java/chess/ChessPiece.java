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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveCollection moveCollection = new MoveCollection(board, pieceColor);
        moveCollection.addPieceMoves(type, myPosition);

        // Required because the piece promotion tests tend to freak out if you give them an array list..
        return new HashSet<>(moveCollection.getMoves());
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece other) {
            return (other.pieceColor == pieceColor) && (other.type == type);
        }
        return false;
    }
}
