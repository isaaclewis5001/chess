package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessMove {
    ChessPosition startPos;
    ChessPosition endPos;
    ChessPiece.PieceType promotion;


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPos = startPosition.copy();
        this.endPos = endPosition.copy();
        this.promotion = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location.
     */
    public ChessPosition getStartPosition() {
        return startPos;
    }

    /**
     * @return ChessPosition of ending location.
     */
    public ChessPosition getEndPosition() {
        return endPos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move.
     *
     * @return Type of piece to promote a pawn to, or null if no promotion.
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove other) {
            return (
                    startPos.equals(other.startPos) &&
                    endPos.equals(other.endPos) &&
                    promotion == other.promotion
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promotion);
    }

    @Override
    public String toString() {
        if (promotion == null) {
            return String.format("Move %s to %s", startPos, endPos);
        }
        else {
            return String.format("Move %s to %s (promote into %s)", startPos, endPos, promotion);
        }
    }
}
