package servicesql;

import exception.DataAccessException;
import exception.ResponseException;
import request.CreateRequest;
import response.CreateResult;

public interface CreateService {
    CreateResult create(CreateRequest request) throws DataAccessException, ResponseException;
}
