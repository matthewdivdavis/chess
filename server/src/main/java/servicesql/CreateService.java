package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;
import server.CreateRequest;
import service.CreateResult;

public interface CreateService {
    CreateResult create(CreateRequest request) throws DataAccessException, ResponseException;
}
