package model.request;

import chess.ChessGame;
import model.validation.Validate;
import model.validation.ValidationException;

public record JoinGameRequest(int gameID, ChessGame.TeamColor playerColor) {}
