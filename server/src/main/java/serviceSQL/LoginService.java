package serviceSQL;

import dataaccess.DataAccessException;
import server.LoginRequest;
import service.LoginResult;

public interface LoginService {
    LoginResult login(LoginRequest request) throws DataAccessException;
}
