package dataaccess;

import server.RegisterRequest;
import model.*;

public class UserDAO {
    private UserData user;

    public UserDAO (RegisterRequest register){
        user.setUsername(register.username());
        user.setPassword(register.password());
        user.setEmail(register.email());
    }

    public UserData getUser(){
        return user;
    }
}
