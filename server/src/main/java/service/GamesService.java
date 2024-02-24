package service;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import dataAccess.games.GamesDAO;
import model.GameData;
import model.GameDesc;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;

import java.util.Collection;
import java.util.UUID;

public class GamesService {

    private final GamesDAO gamesDAO;

    public Collection<GameDesc> listGames() {
        return gamesDAO.listGames();
    }

    public CreateGameResponse createGame(CreateGameRequest request) {
        // Since we can't use the entire 128-bit uuid, we lose the "universal uniqueness" guarantee.
        // Therefore, we repeat until we find a unique id, or until a fixed number of iterations have passed,
        // when we can probably assume something is quite wrong.
        for (int i = 0; i < 16; i++) {
            // XOR-folding the uuid may produce more "random" outputs than truncating,
            UUID uuid = UUID.randomUUID();
            long foldedOnce = uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits();
            int gameId = (int)(foldedOnce >> 32 ^ foldedOnce);

            // Game ids must be non-negative
            if (gameId < 0) {
                gameId = ~gameId;
            }

            String gameName = request.gameName();
            GameData gameData = new GameData(gameId, "", "", gameName, null);
            try {
                gamesDAO.createGame(gameData);
            } catch (DuplicateKeyException ex) {
                continue;
            }

            return new CreateGameResponse(gameId);
        }
        throw new RuntimeException("Failed to create game with unique ID.");
    }

    public static class BadGameIdException extends Exception {
        public BadGameIdException(Exception cause) {
            super("Bad game ID", cause);
        }
    }

    public static class TeamTakenException extends Exception {
        public TeamTakenException(Exception cause) {
            super("Team already taken", cause);
        }
    }

    public void joinGame(JoinGameRequest request, String username)
            throws BadGameIdException, TeamTakenException {
        try {
            if (request.playerColor() != null) {
                gamesDAO.updateGameParticipants(request.gameID(), request.playerColor(), username);
            }
        } catch (MissingKeyException ex) {
            throw new BadGameIdException(ex);
        } catch (BadUpdateException ex) {
            throw new TeamTakenException(ex);
        }
    }

    public GamesService(GamesDAO gamesDao) {
        this.gamesDAO = gamesDao;
    }
}
