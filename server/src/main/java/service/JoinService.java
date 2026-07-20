package service;

import dataaccess.DataAccessException;
import server.JoinRequest;

public interface JoinService {
    JoinResult join(String authToken, JoinRequest request) throws DataAccessException;
}
