package service;

import dataaccess.DataAccessException;
import server.RegisterRequest;

public interface RegisterService {
    RegisterResult register(RegisterRequest request) throws DataAccessException;
}
