package chess;

public final class BoardState {
    private final ChessBoard board;
    private final TeamInfo whiteTeamInfo;
    private final TeamInfo blackTeamInfo;
    private int enPassantFile;
    private ChessGame.TeamColor teamToMove;

    public BoardState(ChessBoard board) {
        this.board = board;
        whiteTeamInfo = new TeamInfo(board.getKingSquare(ChessGame.TeamColor.WHITE), true, true);
        blackTeamInfo = new TeamInfo(board.getKingSquare(ChessGame.TeamColor.BLACK), true, true);
        enPassantFile = 0;
        teamToMove = ChessGame.TeamColor.WHITE;
    }

    public BoardState(BoardState other) {
        board = new ChessBoard(other.board);
        whiteTeamInfo = new TeamInfo(other.whiteTeamInfo);
        blackTeamInfo = new TeamInfo(other.blackTeamInfo);
        enPassantFile = other.enPassantFile;
        teamToMove = other.teamToMove;
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessPiece previous = board.getPiece(position);
        if (previous != null && previous.getPieceType() == ChessPiece.PieceType.KING) {
            getTeamInfo(previous.getTeamColor()).setKingSquare(null);
        }
        board.addPiece(position, piece);
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
            getTeamInfo(piece.getTeamColor()).setKingSquare(position);
        }

        ChessGame.TeamColor backTeam = null;

        if (position.getRow() == 1) {
            backTeam = ChessGame.TeamColor.WHITE;
        }
        else if (position.getRow() == 8) {
            backTeam = ChessGame.TeamColor.BLACK;
        }

        if (backTeam != null) {
            TeamInfo info = getTeamInfo(backTeam);
            ChessPiece rook = new ChessPiece(backTeam, ChessPiece.PieceType.ROOK);
            ChessPiece king = new ChessPiece(backTeam, ChessPiece.PieceType.KING);
            if (position.getColumn() == 1 && !rook.equals(piece)) {
                info.revokeLongCastle();
            }
            else if (position.getColumn() == 8 && !rook.equals(piece)) {
                info.revokeShortCastle();
            }
            else if (position.getColumn() == 5 && !king.equals(piece)) {
                info.revokeShortCastle();
                info.revokeLongCastle();
            }
        }
    }

    public ChessPiece getPiece(ChessPosition position) {
        return board.getPiece(position);
    }

    public int getEnPassantFile() {
        return enPassantFile;
    }

    public void setEnPassantFile(int enPassantFile) {
        this.enPassantFile = enPassantFile;
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


    public ChessGame.TeamColor getTeamToMove() {
        return teamToMove;
    }

    public void setTeamToMove(ChessGame.TeamColor teamToMove) {
        this.teamToMove = teamToMove;
    }

    public ChessBoard getBoard() {
        return board;
    }
}
