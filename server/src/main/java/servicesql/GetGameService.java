package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import model.GameData;
import request.ListRequest;
import response.GameResult;

public interface GetGameService {
    GameResult getGame(ListRequest request, int gameId) throws DataAccessException, ResponseException;
}
