package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import request.JoinRequest;
import response.JoinResult;

public interface JoinService {
    JoinResult join(String authToken, JoinRequest request) throws DataAccessException, ResponseException;
}
