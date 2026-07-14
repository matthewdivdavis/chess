package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO {
    private final List<AuthData> auths = new ArrayList<>();

    public void addAuth(AuthData auth){
        auths.add(auth);
    }
    public AuthData getAuth(String authToken){
        for(AuthData auth : auths){
            if(auth.getAuthToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }
    public void remove(String authToken){
        for(AuthData a : auths){
            if(a.getAuthToken().equals(authToken)){
                auths.remove(a);
                return;
            }
        }
    }
}
