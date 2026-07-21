package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import server.ListRequest;
import service.GameResult;

import java.util.ArrayList;

public interface ListService {
    ArrayList<GameResult> list(ListRequest request) throws DataAccessException, ResponseException;
}
