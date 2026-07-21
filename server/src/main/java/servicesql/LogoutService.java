package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import server.LogoutRequest;
import service.LogoutResult;

public interface LogoutService {
    LogoutResult logout(LogoutRequest request) throws DataAccessException, ResponseException;
}
