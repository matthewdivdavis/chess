package service;

import dataaccess.DataAccessException;
import server.ListRequest;

import java.util.ArrayList;

public interface ListService {
    ArrayList<GameResult> list(ListRequest request) throws DataAccessException;
}
