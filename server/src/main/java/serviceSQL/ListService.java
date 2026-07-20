package serviceSQL;

import dataaccess.DataAccessException;
import server.ListRequest;
import service.GameResult;

import java.util.ArrayList;

public interface ListService {
    ArrayList<GameResult> list(ListRequest request) throws DataAccessException;
}
