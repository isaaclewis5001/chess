package service;

import dataAccess.BadUpdateException;
import dataAccess.DatabaseException;
import dataAccess.MissingKeyException;
import dataAccess.games.GamesDAO;
import model.data.GameDesc;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResponse;

import java.util.List;

public class GamesService {

    private final GamesDAO gamesDAO;

    public List<GameDesc> listGames() throws DatabaseException {
        return gamesDAO.listGames();
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DatabaseException {

        int gameId = gamesDAO.createGame(request.gameName());

        return new CreateGameResponse(gameId);
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
            throws BadGameIdException, TeamTakenException, DatabaseException {
        try {
            if (request.playerColor() != null) {
                gamesDAO.updateGameParticipants(request.gameID(), request.playerColor(), username);
            }
            else if (!gamesDAO.gameExists(request.gameID())) {
                throw new BadGameIdException(null);
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
