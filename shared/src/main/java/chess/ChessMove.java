package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public final class ChessMove {
    private final ChessPosition startPos;
    private final ChessPosition endPos;
    private final ChessPiece.PieceType promotion;

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

    public boolean makeMove(BoardState board) {
        ChessPiece piece = board.getPiece(startPos);

        // Handle castling
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            // Treat the rook move as a regular move.
            if (startPos.getColumn() == 5 && endPos.getColumn() == 3) {
                ChessPosition rookStart = startPos.getOffset(0, -4);
                ChessPosition rookEnd = startPos.getOffset(0, -1);
                new ChessMove(rookStart, rookEnd, null).makeMove(board);
            }
            if (startPos.getColumn() == 5 && endPos.getColumn() == 7) {
                ChessPosition rookStart = startPos.getOffset(0, 3);
                ChessPosition rookEnd = startPos.getOffset(0, 1);
                new ChessMove(rookStart, rookEnd, null).makeMove(board);
            }
        }

        boolean isPawn = piece.getPieceType() == ChessPiece.PieceType.PAWN;
        board.addPiece(startPos, null);
        if (promotion != null) {
            piece = new ChessPiece(piece.getTeamColor(), promotion);
        }
        ChessPiece endPiece = board.getPiece(endPos);
        board.addPiece(endPos, piece);
        return endPiece != null || isPawn;
    }

    public boolean checksSquare(ChessPosition position) {
        return endPos.equals(position);
    }

}
