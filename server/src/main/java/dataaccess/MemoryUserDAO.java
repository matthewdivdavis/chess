package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO {
    private final List<UserData> users = new ArrayList<>();

    public void addUser(UserData user){
        users.add(user);
    }
    public UserData getUser(String username){
        for(UserData user : users){
            if(username.equals(user.getUsername())) return user;
        }
        return null;
    }
    public boolean containsUser(String username){
        for(UserData user : users){
            if(username.equals(user.getUsername())) return true;
        }
        return false;
    }
}
