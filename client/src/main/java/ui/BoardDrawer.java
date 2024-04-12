package ui;

import chess.*;

import java.util.Arrays;

public class BoardDrawer {
    private static int getIndex(ChessGame.TeamColor perspective, int row, int col) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            return new ChessPosition(row + 1, col + 1).getIndex();
        }
        return new ChessPosition(8 - row , 8 - col).getIndex();
    }

    private static String getColLabels(ChessGame.TeamColor perspective) {
        String prefix = EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.SET_BG_COLOR_DARK_GREY;
        if (perspective == ChessGame.TeamColor.WHITE) {
            return prefix + "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET + "\n";
        }
        return prefix + "    h  g  f  e  d  c  b  a    "+ EscapeSequences.RESET + "\n";
    }

    private static final String[] rowLabels = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
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
    public static void draw(StringBuilder output, BoardState game, ChessGame.TeamColor fromPerspective, ChessPosition highlightPos, ChessGame.TeamColor highlightTeam) {
        boolean[] highlights = new boolean[64];
        Arrays.fill(highlights, false);
        if (highlightPos != null) {
            if (game.getTeamToMove() != highlightTeam) {
                game = new BoardState(game);
                game.flipTeam();
            }
            MoveCollection moves = new MoveCollection(game);
            moves.calculateMoves(false);
            for (ChessMove move: moves.filteredMoves()) {
                if (move.getStartPosition().equals(highlightPos)) {
                    highlights[move.getEndPosition().getIndex()] = true;
                }
            }
        }
        boolean lightSquareRowStart = true;
        String colLabels = getColLabels(fromPerspective);

        output.append("\n");
        output.append(colLabels);
        for (int row = 7; row >= 0; row--) {
            boolean lightSquare = lightSquareRowStart;
            String rowLabel = getRowLabel(fromPerspective, row);
            output.append(rowLabel);
            for (int col = 0; col < 8; col++) {
                int squareIndex = getIndex(fromPerspective, row, col);
                if (highlights[squareIndex]) {
                    output.append(EscapeSequences.SET_BG_COLOR_GREEN);
                }
                else if (lightSquare) {
                    output.append(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                else {
                    output.append(EscapeSequences.SET_BG_COLOR_BLUE);
                }

                output.append(pieceString(game.getBoard().getPiece(squareIndex)));
                lightSquare = !lightSquare;
            }
            output.append(rowLabel);
            output.append(EscapeSequences.RESET + "\n");

            lightSquareRowStart = !lightSquareRowStart;
        }
        output.append(colLabels);
    }
}
