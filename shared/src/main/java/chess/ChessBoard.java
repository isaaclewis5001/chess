package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[] squares;
    private final TeamInfo whiteTeamInfo;
    private final TeamInfo blackTeamInfo;
    private int enPassantFile;
    private ChessGame.TeamColor teamToMove;

    public ChessBoard() {
        squares = new ChessPiece[64];
        whiteTeamInfo = new TeamInfo();
        blackTeamInfo = new TeamInfo();
        enPassantFile = 0;
        teamToMove = ChessGame.TeamColor.WHITE;
    }

    public ChessBoard(ChessBoard other) {
        squares = other.squares.clone();
        whiteTeamInfo = new TeamInfo(other.whiteTeamInfo);
        blackTeamInfo = new TeamInfo(other.blackTeamInfo);
        enPassantFile = other.enPassantFile;
        teamToMove = other.teamToMove;
    }

    public ChessGame.TeamColor getTeamToMove() {
        return teamToMove;
    }

    public void setTeamToMove(ChessGame.TeamColor teamToMove) {
        this.teamToMove = teamToMove;
    }

    /**
     * Adds a chess piece to the chessboard.
     *
     * @param position Where to add the piece to.
     * @param piece    The piece to add.
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessPiece previous = squares[position.getIndex()];
        if (previous != null && previous.getPieceType() == ChessPiece.PieceType.KING) {
            getTeamInfo(previous.getTeamColor()).setKingSquare(null);
        }
        squares[position.getIndex()] = piece;
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
            getTeamInfo(piece.getTeamColor()).setKingSquare(position);
        }
    }

    /**
     * Gets a chess piece on the chessboard.
     *
     * @param position The position to get the piece from.
     * @return Either the piece at the position, or null if no piece is at that
     * position.
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getIndex()];
    }

    // Lists out the pieces on the first and eight ranks for resetBoard()
    private static final ChessPiece.PieceType[] PIECE_ORDER = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
    };

    public int getEnPassantFile() {
        return enPassantFile;
    }

    public void setEnPassantFile(int enPassantFile) {
        this.enPassantFile = enPassantFile;
    }

    /**
     * Sets the board to the default starting board.
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        whiteTeamInfo.reset();
        blackTeamInfo.reset();
        enPassantFile = 0;


        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int file = 1; file <= 8; file++) {
            ChessPiece.PieceType pieceType = PIECE_ORDER[file - 1];
            ChessPiece whitePiece = new ChessPiece(ChessGame.TeamColor.WHITE, pieceType);
            ChessPiece blackPiece = new ChessPiece(ChessGame.TeamColor.BLACK, pieceType);
            addPiece(new ChessPosition(1, file), whitePiece);
            addPiece(new ChessPosition(2, file), whitePawn);
            for (int rank = 3; rank < 7; rank++) {
                addPiece(new ChessPosition(rank, file), null);
            }
            addPiece(new ChessPosition(7, file), blackPawn);
            addPiece(new ChessPosition(8, file), blackPiece);
        }
    }

    public void flipTeam() {
        teamToMove = teamToMove.opponent();
    }

    public TeamInfo getTeamInfo(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return whiteTeamInfo;
        }
        return blackTeamInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessBoard other) {
            return Arrays.equals(squares, other.squares) &&
                    whiteTeamInfo.equals(other.whiteTeamInfo) &&
                    blackTeamInfo.equals(other.blackTeamInfo) &&
                    enPassantFile == other.enPassantFile;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(squares), whiteTeamInfo, blackTeamInfo, enPassantFile);
    }
}
