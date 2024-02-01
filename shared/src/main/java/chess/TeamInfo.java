package chess;

import java.util.Objects;

public final class TeamInfo {
    private ChessPosition kingSquare;
    private boolean castleShort;
    private boolean castleLong;

    public TeamInfo(ChessPosition kingSquare, boolean castleShort, boolean castleLong) {
        this.kingSquare = kingSquare;
        this.castleShort = castleShort;
        this.castleLong = castleLong;
    }

    public TeamInfo(ChessGame.TeamColor color) {
        this(null, true, true);
        reset(color);
    }

    public TeamInfo(TeamInfo other) {
        this(other.kingSquare, other.castleShort, other.castleLong);
    }

    public void reset(ChessGame.TeamColor color) {
        this.castleShort = true;
        this.castleLong = true;

        int backRank = (color == ChessGame.TeamColor.WHITE) ? 1 : 8;
        kingSquare = new ChessPosition(backRank, 5);
    }

    public ChessPosition getKingSquare() {
        return kingSquare;
    }

    public boolean canCastleShort() {
        return castleShort;
    }

    public boolean canCastleLong() {
        return castleLong;
    }

    public void setKingSquare(ChessPosition kingSquare) {
        this.kingSquare = kingSquare;
    }

    public void revokeShortCastle(boolean castleShort) {
        this.castleShort = castleShort;
    }

    public void revokeLongCastle(boolean castleLong) {
        this.castleLong = castleLong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TeamInfo teamInfo) {
            return castleShort == teamInfo.castleShort &&
                    castleLong == teamInfo.castleLong &&
                    kingSquare.equals(teamInfo.kingSquare);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingSquare, castleShort, castleLong);
    }
}
