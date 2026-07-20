package serviceSQL;

import dataaccess.DataAccessException;
import server.CreateRequest;
import service.CreateResult;

public interface CreateService {
    CreateResult create(CreateRequest request) throws DataAccessException;
}
