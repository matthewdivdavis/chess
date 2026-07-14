package dataaccess;

import model.*;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO {
    private final List<UserData> users = new ArrayList<>();

    public void addUser(UserData user){
        users.add(user);
    }

    public UserData getUser(String username){
        for(UserData user : users){
            if(username.equals(user.username())) return user;
        }
        return null;
    }
    public UserData getPassword(String username, String password){
        for(UserData user : users){
            if(username.equals(user.username())
                    && password.equals(user.password())){
                return user;
            }
        }
        return null;
    }
}
