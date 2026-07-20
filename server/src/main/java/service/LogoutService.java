package service;

import dataaccess.DataAccessException;
import server.LogoutRequest;

public interface LogoutService {
    LogoutResult logout(LogoutRequest request) throws DataAccessException;
}
