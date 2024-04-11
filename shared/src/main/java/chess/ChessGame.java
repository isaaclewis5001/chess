package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private final HashMap<BoardState, Integer> pastBoards;
    private BoardState currentBoard;
    private int movesSinceCaptureOrPush;
    private boolean isCheck;
    private boolean isGameEnd;

    private boolean isCheckmate;
    private boolean hasMoves;
    private HashSet<ChessMove> moves;

    public ChessGame() {
        this(new ChessBoard());
    }

    public ChessGame(ChessBoard board) {
        pastBoards = new HashMap<>();
        moves = new HashSet<>();
        setBoard(board);
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

        public int rankToRow(int rank) {
            if (this == WHITE) {
                return rank;
            }
            return 9 - rank;
        }

        public int direction() {
            if (this == WHITE) {
                return 1;
            }
            return -1;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece.
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = currentBoard.getPiece(startPosition);
        return piece.filteredMoves(currentBoard, startPosition);
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

        BoardState boardCopy = new BoardState(currentBoard);
        int timesPlayed = pastBoards.getOrDefault(boardCopy, 0) + 1;
        pastBoards.put(boardCopy, timesPlayed);

        calculateMoves();
        if (timesPlayed >= 3) {
            isGameEnd = true;
            isCheckmate = false;
        }
        else if (movesSinceCaptureOrPush >= 40) {
            isGameEnd = true;
        }
    }

    private<R> R computeMaybeFlipped(TeamColor teamColor, Function<ChessGame, R> func) {
        boolean wrongColor = teamColor != currentBoard.getTeamToMove();
        if (wrongColor) {
            setTeamTurn(teamColor);
        }
        R out = func.apply(this);
        if (wrongColor) {
            setTeamTurn(teamColor.opponent());
        }
        return out;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return computeMaybeFlipped(teamColor, (ChessGame maybeFlipped) ->
            maybeFlipped.isCheck
        );
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return computeMaybeFlipped(teamColor, (ChessGame maybeFlipped) ->
            maybeFlipped.isCheckmate
        );
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
        currentBoard = new BoardState(board);
        pastBoards.put(currentBoard, 1);
        movesSinceCaptureOrPush = 0;
        calculateMoves();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard.getBoard();
    }

    public BoardState getBoardState() {
        return currentBoard;
    }

    private void calculateMoves() {
        if (isGameEnd) {
            return;
        }
        MoveCollection ownMoves = new MoveCollection(currentBoard);
        ownMoves.calculateMoves(false);
        moves = ownMoves.filteredMoves();


        isCheck = ownMoves.isInCheck();
        hasMoves = !moves.isEmpty();
        isCheckmate = !hasMoves && isCheck;
    }
}
