package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import server.LoginRequest;
import service.LoginResult;

public interface LoginService {
    LoginResult login(LoginRequest request) throws DataAccessException, ResponseException;
}
