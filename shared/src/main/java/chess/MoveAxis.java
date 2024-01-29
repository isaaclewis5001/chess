package chess;

public enum MoveAxis {
    UP(1, 0),
    RIGHT(0,1),
    UP_RIGHT(1,1),
    DOWN_RIGHT(-1, 1);

    public final int rowOff;
    public final int colOff;

    MoveAxis(int rowOff, int colOff) {
        this.rowOff = rowOff;
        this.colOff = colOff;
    }
}
