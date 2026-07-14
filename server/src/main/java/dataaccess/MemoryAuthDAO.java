package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO {
    private final List<AuthData> auths = new ArrayList<>();

    public void addAuth(AuthData auth){
        auths.add(auth);
    }
    public AuthData getAuth(String username){
        for(AuthData auth : auths){
            if(auth.getUsername().equals(username)){
                return auth;
            }
        }
        return null;
    }
}
