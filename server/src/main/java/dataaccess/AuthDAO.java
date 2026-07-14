package dataaccess;

import server.RegisterRequest;
import model.*;

public class AuthDAO {
    private final AuthData auth;
    public AuthDAO (){
        auth = new AuthData();
    }
    public void createAuth(String username){
        auth.setUsername(username);
    }
    public AuthData getAuth(){
        return auth;
    }
}
