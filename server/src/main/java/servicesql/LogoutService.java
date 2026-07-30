package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import request.LogoutRequest;
import response.LogoutResult;

public interface LogoutService {
    LogoutResult logout(LogoutRequest request) throws DataAccessException, ResponseException;
}
