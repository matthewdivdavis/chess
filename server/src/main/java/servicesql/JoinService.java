package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import server.JoinRequest;
import service.JoinResult;

public interface JoinService {
    JoinResult join(String authToken, JoinRequest request) throws DataAccessException, ResponseException;
}
