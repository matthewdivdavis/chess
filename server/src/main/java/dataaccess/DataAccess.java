package dataaccess;

import exception.ResponseException;
import model.*;
import org.eclipse.jetty.server.Authentication;

public interface DataAccess {
    UserData addUser(UserData userData) throws ResponseException;
    UserData getUser(String username) throws ResponseException;

    GameList listGames() throws ResponseException;

}
