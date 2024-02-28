package chess;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * A collection of moves on a chess board.
 */
public final class MoveCollection {
    private final BoardState boardState;

    private final ArrayList<ChessMove> moves;

    private ArrayList<ChessMove> opponentMoves;

    public MoveCollection(BoardState boardState) {
        moves = new ArrayList<>();
        this.boardState = boardState;
        opponentMoves = null;
    }

    public ArrayList<ChessMove> getMoves() {
        return moves;
    }


    private boolean canCapture(ChessPiece targetPiece) {
        if (targetPiece == null) {
            return false;
        }
        return boardState.getTeamToMove() != targetPiece.getTeamColor();
    }

    private boolean addNormalMove(ChessPosition position, ChessPosition newPosition) {
        if (!newPosition.isOnBoard()) {
            return false;
        }
        ChessPiece targetPiece = boardState.getPiece(newPosition);
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
        ChessPosition enPassantSquare = new ChessPosition(
                boardState.getTeamToMove().rankToRow(6), boardState.getEnPassantFile()
        );
        ChessPiece targetPiece = boardState.getPiece(newPosition);
        if ((isCapture ? canCapture(targetPiece) : targetPiece == null) || newPosition.equals(enPassantSquare)) {
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

    private void addKingMoves(ChessPosition position, boolean shallow) {
        addNormalMove(position, position.getOffset(1, 1));
        addNormalMove(position, position.getOffset(0, 1));
        addNormalMove(position, position.getOffset(-1, 1));
        addNormalMove(position, position.getOffset(1, 0));
        addNormalMove(position, position.getOffset(-1, 0));
        addNormalMove(position, position.getOffset(1, -1));
        addNormalMove(position, position.getOffset(0, -1));
        addNormalMove(position, position.getOffset(-1, -1));

        ChessGame.TeamColor team = boardState.getTeamToMove();
        TeamInfo info = boardState.getTeamInfo(team);

        // Castling
        int firstRank = team.rankToRow(1);

        ChessPiece rook = new ChessPiece(team, ChessPiece.PieceType.ROOK);
        ChessPosition castlePosition = new ChessPosition(firstRank, 5);
        ChessPosition longRookPos = new ChessPosition(firstRank, 1);
        ChessPosition shortRookPos = new ChessPosition(firstRank, 8);

        if (!shallow) {
            if (position.equals(castlePosition) && !isInCheck()) {
                if (info.canCastleLong() &&
                        boardState.getBoard().isRowEmpty(position.getOffset(0, -3), 3) &&
                        rook.equals(boardState.getPiece(longRookPos)) &&
                        !doMovesCheckSquare(opponentMoves, position.getOffset(0, -1))
                ) {
                    moves.add(new ChessMove(position, position.getOffset(0, -2), null));
                }
                if (info.canCastleShort() &&
                        boardState.getBoard().isRowEmpty(position.getOffset(0, 1), 2) &&
                        rook.equals(boardState.getPiece(shortRookPos)) &&
                        !doMovesCheckSquare(opponentMoves, position.getOffset(0, 1))
                ) {
                    moves.add(new ChessMove(position, position.getOffset(0, 2), null));
                }
            }
        }
    }

    private void addPawnMoves(ChessPosition position) {
        int forwards = boardState.getTeamToMove().direction();
        int row = position.getRow();
        boolean promote = row == boardState.getTeamToMove().rankToRow(7);

        addPawnMove(position, position.getOffset(forwards, 1), promote, true);
        addPawnMove(position, position.getOffset(forwards, -1), promote, true);

        boolean forwardsBlocked = !addPawnMove(position, position.getOffset(forwards, 0), promote, false);
        if (!forwardsBlocked && row == boardState.getTeamToMove().rankToRow(2)) {
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
    void addPieceMoves(ChessPiece.PieceType pieceType, ChessPosition position, boolean shallow) {
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
                addKingMoves(position, shallow);
                break;
            case PAWN:
                addPawnMoves(position);
                break;
        }
    }

    /**
     * Calculates all possible moves on the board, not accounting for king safety.
     */
    public void calculateMoves(boolean shallow) {
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                ChessPosition start = new ChessPosition(row, col);
                ChessPiece piece = boardState.getPiece(start);
                if (piece != null && piece.getTeamColor() == boardState.getTeamToMove()) {
                    addPieceMoves(piece.getPieceType(), start, shallow);
                }
            }
        }
    }

    public void calculateOpponentMoves() {
        if (opponentMoves == null) {
            boardState.flipTeam();
            MoveCollection opponentCollection = new MoveCollection(boardState);
            opponentCollection.calculateMoves(true);
            opponentMoves = opponentCollection.getMoves();
            boardState.flipTeam();
        }
    }

    private static boolean doMovesCheckSquare(ArrayList<ChessMove> moves, ChessPosition pos) {
        if (pos == null) {
            return false;
        }
        for (ChessMove move: moves) {
            if (move.checksSquare(pos)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpponentInCheck() {
        ChessGame.TeamColor opponentColor = boardState.getTeamToMove().opponent();
        ChessPosition kingPos = boardState.getTeamInfo(opponentColor).getKingSquare();
        return doMovesCheckSquare(moves, kingPos);
    }

    public boolean isInCheck() {
        calculateOpponentMoves();

        ChessGame.TeamColor team = boardState.getTeamToMove();
        ChessPosition kingPos = boardState.getTeamInfo(team).getKingSquare();

        return doMovesCheckSquare(opponentMoves, kingPos);
    }

    public HashSet<ChessMove> filteredMoves() {
        HashSet<ChessMove> outMoves = new HashSet<>();
        for (ChessMove move: moves) {
            BoardState futureBoard = new BoardState(boardState);
            move.makeMove(futureBoard);
            futureBoard.flipTeam();

            MoveCollection movesFuture = new MoveCollection(futureBoard);
            movesFuture.calculateMoves(true);
            if (movesFuture.isOpponentInCheck()) {
                // Reject move as it leaves the king in check
                continue;
            }
            outMoves.add(move);
        }
        return outMoves;
    }
}
