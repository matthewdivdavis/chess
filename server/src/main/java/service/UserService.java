package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import server.*;

public class UserService{
    MemoryUserDAO userMem;
    MemoryAuthDAO authMem;

    public UserService(){
        userMem = new MemoryUserDAO();
        authMem = new MemoryAuthDAO();
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException{
        if(userMem.getUser(request.username()) != null){
            throw new DataAccessException("username already taken");
        }
        AuthData auth = AuthDAO.createAuth(request.username());
        userMem.addUser(UserDAO.createUser(request.username(), request.password(), request.email()));
        authMem.addAuth(auth);
        return new RegisterResult(request.username(), auth.getAuthToken());
    }
}
