package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private final HashMap<ChessBoard, Integer> pastBoards;
    private ChessBoard currentBoard;
    private int movesSinceCaptureOrPush;
    private boolean isCheck;
    private boolean isGameEnd;

    private boolean isCheckmate;
    private boolean hasMoves;
    private final HashSet<ChessMove> moves;

    public ChessGame() {
        pastBoards = new HashMap<>();
        moves = new HashSet<>();
        setBoard(new ChessBoard());
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentBoard.getTeamToMove();
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentBoard.setTeamToMove(team);
        calculateMoves();
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;

        public TeamColor opponent() {
            if (this == WHITE) {
                return BLACK;
            }
            return WHITE;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!moves.contains(move)) {
            throw new InvalidMoveException();
        }
        if (move.makeMove(currentBoard)) {
            movesSinceCaptureOrPush = 0;
        }
        else {
            movesSinceCaptureOrPush += 1;
        }
        currentBoard.flipTeam();

        int timesPlayed = pastBoards.getOrDefault(currentBoard, 0) + 1;
        pastBoards.put(currentBoard, timesPlayed);

        calculateMoves();

        if (timesPlayed >= 3) {
            isGameEnd = true;
            isCheckmate = false;
        }
        else if (movesSinceCaptureOrPush >= 40) {
            isGameEnd = true;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean wrongColor = teamColor != currentBoard.getTeamToMove();
        if (wrongColor) {
            setTeamTurn(teamColor);
        }
        boolean out = isCheck;
        if (wrongColor) {
            setTeamTurn(teamColor.opponent());
        }
        return out;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean wrongColor = teamColor != currentBoard.getTeamToMove();
        if (wrongColor) {
            setTeamTurn(teamColor);
        }
        boolean out = isCheckmate;
        if (wrongColor) {
            setTeamTurn(teamColor.opponent());
        }
        return out;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean wrongColor = teamColor != currentBoard.getTeamToMove();
        if (wrongColor) {
            setTeamTurn(teamColor);
        }
        boolean out = (isGameEnd || !hasMoves) && !isCheckmate;
        if (wrongColor) {
            setTeamTurn(teamColor.opponent());
        }
        return out;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        pastBoards.clear();
        pastBoards.put(board, 1);
        currentBoard = board;
        movesSinceCaptureOrPush = 0;
        calculateMoves();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }


    private void calculateMoves() {
        moves.clear();
        if (isGameEnd) {
            return;
        }
        MoveCollection movesNow = new MoveCollection(currentBoard);
        movesNow.calculateMoves();
        Collection<ChessMove> candidateMoves = movesNow.getMoves();

        isCheckmate = false;
        isCheck = false;
        hasMoves = false;

        for (ChessMove move: candidateMoves) {
            ChessBoard futureBoard = new ChessBoard(currentBoard);
            move.makeMove(futureBoard);
            futureBoard.flipTeam();

            MoveCollection movesFuture = new MoveCollection(futureBoard);
            movesFuture.calculateMoves();
            if (movesFuture.isOpponentInCheck()) {
                // Reject move as it leaves the king in check
                continue;
            }
            hasMoves = true;
            moves.add(move);
        }

        currentBoard.flipTeam();
        MoveCollection opponentMoves = new MoveCollection(currentBoard);
        opponentMoves.calculateMoves();
        if (opponentMoves.isOpponentInCheck()) {
            isCheck = true;
            isCheckmate = !hasMoves;
        }
        currentBoard.flipTeam();
    }
}
