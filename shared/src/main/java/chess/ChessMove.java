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
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPos = startPosition.copy();
        this.endPos = endPosition.copy();
        this.promotionPiece = promotionPiece;
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
        return promotionPiece;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove other) {
            return (
                    startPos.equals(other.startPos) &&
                    endPos.equals(other.endPos) &&
                    promotionPiece == other.promotionPiece
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promotionPiece);
    }

    @Override
    public String toString() {
        if (promotionPiece == null) {
            return String.format("Move %s to %s", startPos, endPos);
        }
        else {
            return String.format("Move %s to %s (promote into %s)", startPos, endPos, promotionPiece);
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
            board.getTeamInfo(piece.getTeamColor()).setKingSquare(endPos);
        }

        boolean isPawn = piece.getPieceType() == ChessPiece.PieceType.PAWN;
        ChessPiece endPiece = board.getPiece(endPos);

        // Is this move en passant?
        if (endPiece == null && isPawn && startPos.getColumn() != endPos.getColumn()) {
            ChessPosition enPassantSquare = new ChessPosition(startPos.getRow(), endPos.getColumn());
            board.addPiece(enPassantSquare, null);
        }

        // Should en passant be allowed next move?
        int rowDist = endPos.getRow() - startPos.getRow();
        if (isPawn && (rowDist > 1 || rowDist < -1)) {
            board.setEnPassantFile(endPos.getColumn());
        }
        else {
            board.setEnPassantFile(0);
        }


        board.addPiece(startPos, null);
        if (promotionPiece != null) {
            piece = new ChessPiece(piece.getTeamColor(), promotionPiece);
        }
        board.addPiece(endPos, piece);
        return endPiece != null || isPawn;
    }

    public boolean checksSquare(ChessPosition position) {
        return endPos.equals(position);
    }

}
