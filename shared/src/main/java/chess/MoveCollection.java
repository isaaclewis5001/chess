package chess;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A collection of moves on a chess board.
 */
public final class MoveCollection {
    private final ChessBoard board;

    private final ArrayList<ChessMove> moves;

    public MoveCollection(ChessBoard board) {
        moves = new ArrayList<>();
        this.board = board;
    }

    public Collection<ChessMove> getMoves() {
        return moves;
    }


    private boolean canCapture(ChessPiece targetPiece) {
        if (targetPiece == null) {
            return false;
        }
        return board.getTeamToMove() != targetPiece.getTeamColor();
    }

    private boolean addNormalMove(ChessPosition position, ChessPosition newPosition) {
        if (!newPosition.isOnBoard()) {
            return false;
        }
        ChessPiece targetPiece = board.getPiece(newPosition);
        if (targetPiece == null || canCapture(targetPiece)) {
            moves.add(new ChessMove(position, newPosition, null));
            return targetPiece == null;
        }
        return false;
    }

    private boolean addPawnMove(ChessPosition position, ChessPosition newPosition, boolean promote, boolean isCapture) {
        if (!newPosition.isOnBoard()) {
            return false;
        }
        ChessPiece targetPiece = board.getPiece(newPosition);
        if (isCapture ? canCapture(targetPiece) : targetPiece == null) {
            if (promote) {
                moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
            }
            else {
                moves.add(new ChessMove(position, newPosition, null));
            }
            return targetPiece == null;
        }
        return false;
    }

    private void scanMoveLine(ChessPosition position, int up, int right) {
        ChessPosition newPos = position;
        do {
            newPos = newPos.getOffset(up, right);
        } while(addNormalMove(position, newPos));
    }

    private void addRookMoves(ChessPosition position) {
        scanMoveLine(position, 1, 0);
        scanMoveLine(position, -1, 0);
        scanMoveLine(position, 0, 1);
        scanMoveLine(position, 0, -1);
    }


    private void addBishopMoves(ChessPosition position) {
        scanMoveLine(position, 1, 1);
        scanMoveLine(position, -1, 1);
        scanMoveLine(position, 1, -1);
        scanMoveLine(position, -1, -1);
    }

    private void addQueenMoves(ChessPosition position) {
        addRookMoves(position);
        addBishopMoves(position);
    }

    private void addKnightMoves(ChessPosition position) {
        addNormalMove(position, position.getOffset(1,2));
        addNormalMove(position, position.getOffset(-1,2));
        addNormalMove(position, position.getOffset(1,-2));
        addNormalMove(position, position.getOffset(-1,-2));
        addNormalMove(position, position.getOffset(2,1));
        addNormalMove(position, position.getOffset(2,-1));
        addNormalMove(position, position.getOffset(-2,1));
        addNormalMove(position, position.getOffset(-2,-1));
    }

    private void addKingMoves(ChessPosition position) {
        addNormalMove(position, position.getOffset(1, 1));
        addNormalMove(position, position.getOffset(0, 1));
        addNormalMove(position, position.getOffset(-1, 1));
        addNormalMove(position, position.getOffset(1, 0));
        addNormalMove(position, position.getOffset(-1, 0));
        addNormalMove(position, position.getOffset(1, -1));
        addNormalMove(position, position.getOffset(0, -1));
        addNormalMove(position, position.getOffset(-1, -1));
    }

    private void addPawnMoves(ChessPosition position) {
        int forwards = 1;
        int rank = position.getRow();
        if (board.getTeamToMove() == ChessGame.TeamColor.BLACK) {
            forwards = -1;
            rank = 9 - rank;
        }
        boolean promote = rank == 7;

        addPawnMove(position, position.getOffset(forwards, 1), promote, true);
        addPawnMove(position, position.getOffset(forwards, -1), promote, true);

        boolean forwardsBlocked = !addPawnMove(position, position.getOffset(forwards, 0), promote, false);
        if (!forwardsBlocked && rank == 2) {
            addPawnMove(position, position.getOffset(forwards * 2, 0), false, false);
        }
    }

    /**
     * Computes the moves that a given piece would have at a specific location.
     * This calculation is performed "as if" the piece was there; whether the piece is actually
     * there is not checked.
     * @param pieceType The type of piece to compute the moves for
     * @param position The starting square of the piece
      */
    void addPieceMoves(ChessPiece.PieceType pieceType, ChessPosition position) {
        switch (pieceType) {
            case KNIGHT:
                addKnightMoves(position);
                break;
            case ROOK:
                addRookMoves(position);
                break;
            case BISHOP:
                addBishopMoves(position);
                break;
            case QUEEN:
                addQueenMoves(position);
                break;
            case KING:
                addKingMoves(position);
                break;
            case PAWN:
                addPawnMoves(position);
                break;
        }
    }

    /**
     * Calculates all possible moves on the board, not accounting for king safety.
     */
    public void calculateMoves() {
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                ChessPosition start = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(start);
                if (piece != null && piece.getTeamColor() == board.getTeamToMove()) {
                    addPieceMoves(piece.getPieceType(), start);
                }
            }
        }
    }

    public boolean isOpponentInCheck() {
        ChessGame.TeamColor opponentColor = board.getTeamToMove().opponent();

        ChessPosition kingPos = board.getTeamInfo(opponentColor).getKingSquare();

        if (kingPos == null) {
            return false;
        }

        for (ChessMove move: moves) {
            if (move.checksSquare(kingPos)) {
                return true;
            }
        }
        return false;
    }
}
