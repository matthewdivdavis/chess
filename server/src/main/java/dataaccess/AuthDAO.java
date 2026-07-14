package dataaccess;

import server.RegisterRequest;
import model.*;

public class AuthDAO {
    public static AuthData createAuth(String username){
        return new AuthData(username);
    }
}
