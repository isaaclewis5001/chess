package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessPiece {

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
        BoardState boardState = new BoardState(board);
        boardState.setTeamToMove(pieceColor);
        MoveCollection moveCollection = new MoveCollection(boardState);
        moveCollection.addPieceMoves(type, myPosition, true);
        return new HashSet<>(moveCollection.getMoves());
    }

    public Collection<ChessMove> filteredMoves(BoardState board, ChessPosition myPosition) {
        board = new BoardState(board);
        board.setTeamToMove(pieceColor);
        MoveCollection moveCollection = new MoveCollection(board);
        moveCollection.addPieceMoves(type, myPosition, false);
        return moveCollection.filteredMoves();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece other) {
            return (other.pieceColor == pieceColor) && (other.type == type);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s", pieceColor.toString(), type.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
