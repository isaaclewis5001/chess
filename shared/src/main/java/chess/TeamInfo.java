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
    public TeamInfo(TeamInfo other) {
        this(other.kingSquare, other.castleShort, other.castleLong);
    }

    public void reset() {
        this.castleShort = true;
        this.castleLong = true;
        this.kingSquare = null;
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

    public void revokeShortCastle() {
        this.castleShort = false;
    }

    public void revokeLongCastle() {
        this.castleLong = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TeamInfo teamInfo) {
            boolean kingSquaresEqual;
            if (kingSquare == null) {
                kingSquaresEqual = teamInfo.kingSquare == null;
            }
            else {
                kingSquaresEqual = kingSquare.equals(teamInfo.kingSquare);
            }

            return castleShort == teamInfo.castleShort &&
                    castleLong == teamInfo.castleLong &&
                    kingSquaresEqual;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingSquare, castleShort, castleLong);
    }
}
