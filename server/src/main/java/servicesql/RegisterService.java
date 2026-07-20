package servicesql;

import dataaccess.DataAccessException;
import server.RegisterRequest;
import service.RegisterResult;

public interface RegisterService {
    RegisterResult register(RegisterRequest request) throws DataAccessException;
}
