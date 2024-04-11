package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardDrawer {
    private static ChessPiece getPiece(ChessBoard board, ChessGame.TeamColor perspective, int row, int col) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            return board.getPiece(new ChessPosition(row + 1, col + 1));
        }
        return board.getPiece(new ChessPosition(8 - row, 8 - col));
    }

    private static String getColLabels(ChessGame.TeamColor perspective) {
        String prefix = EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY;
        if (perspective == ChessGame.TeamColor.WHITE) {
            return prefix + "    1  2  3  4  5  6  7  8    \n";
        }
        return prefix + "    8  7  6  5  4  3  2  1    \n";
    }

    private static final String[] rowLabels = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private static String getRowLabel(ChessGame.TeamColor perspective, int row) {
        if (perspective == ChessGame.TeamColor.BLACK) {
            row = 7 - row;
        }
        return EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY + rowLabels[row];
    }
    public static String pieceString(ChessPiece piece) {
        String prefix;
        if (piece == null) {
            return "   ";
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            prefix = EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        } else {
            prefix = EscapeSequences.SET_TEXT_COLOR_BLACK;
        }

        String suffix;
        switch(piece.getPieceType()) {
            case KING -> suffix = " K ";
            case QUEEN -> suffix = " Q ";
            case BISHOP -> suffix = " B ";
            case KNIGHT -> suffix = " N ";
            case ROOK -> suffix = " R ";
            default -> suffix = " i ";
        }
        return prefix + suffix;
    }
    public static void draw(StringBuilder output, ChessBoard board, ChessGame.TeamColor fromPerspective, ChessPosition highlightPos) {
        if (highlightPos != null) {
            throw new RuntimeException("Move highlighting not implemented");
        }
        boolean lightSquareRowStart = true;
        String colLabels = getColLabels(fromPerspective);

        ChessPiece[] rowPieces = new ChessPiece[8];
        output.append("/n");
        output.append(colLabels);
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                rowPieces[col] = getPiece(board, fromPerspective, row, col);
            }
            boolean lightSquare = lightSquareRowStart;
            String rowLabel = getRowLabel(fromPerspective, row);
            output.append(rowLabel);
            for (int col = 0; col < 8; col++) {
                if (lightSquare) {
                    output.append(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                else {
                    output.append(EscapeSequences.SET_BG_COLOR_BLUE);
                }
                output.append(pieceString(rowPieces[col]));
                lightSquare = !lightSquare;
            }
            output.append(rowLabel);
            output.append(EscapeSequences.RESET + "\n");

            lightSquareRowStart = !lightSquareRowStart;
        }
        output.append(colLabels);
    }
}
