package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import request.RegisterRequest;
import response.RegisterResult;

public interface RegisterService {
    RegisterResult register(RegisterRequest request) throws DataAccessException, ResponseException;
}
