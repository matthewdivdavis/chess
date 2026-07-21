package servicesql;

import dataaccess.DataAccessException;
import exception.ResponseException;

public interface ClearService {
    void clear() throws DataAccessException, ResponseException;
}
