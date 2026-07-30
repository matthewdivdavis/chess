package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import request.ListRequest;
import response.GameResult;

import java.util.ArrayList;

public interface ListService {
    ArrayList<GameResult> list(ListRequest request) throws DataAccessException, ResponseException;
}
