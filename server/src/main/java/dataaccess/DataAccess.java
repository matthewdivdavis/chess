package dataaccess;

import exception.ResponseException;
import model.*;

public interface DataAccess {
    UserData addUser(UserData userData) throws ResponseException;
    UserData getUser(String username) throws ResponseException;

    GameList listGames() throws ResponseException;

}
