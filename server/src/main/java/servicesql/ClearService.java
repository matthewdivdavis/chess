package servicesql;

import exception.DataAccessException;
import exception.ResponseException;

public interface ClearService {
    void clear() throws DataAccessException, ResponseException;
}
