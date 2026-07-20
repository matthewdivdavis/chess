package serviceSQL;

import dataaccess.DataAccessException;
import server.JoinRequest;
import service.JoinResult;

public interface JoinService {
    JoinResult join(String authToken, JoinRequest request) throws DataAccessException;
}
