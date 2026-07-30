package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import request.LoginRequest;
import response.LoginResult;

public interface LoginService {
    LoginResult login(LoginRequest request) throws DataAccessException, ResponseException;
}
