package service;

import dataaccess.DataAccessException;
import server.CreateRequest;

public interface CreateService {
    CreateResult create(CreateRequest request) throws DataAccessException;
}
