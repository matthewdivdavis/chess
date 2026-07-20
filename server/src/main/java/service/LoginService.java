package service;

import dataaccess.DataAccessException;
import server.LoginRequest;

public interface LoginService {
    LoginResult login(LoginRequest request) throws DataAccessException;
}
