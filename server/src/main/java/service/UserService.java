package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import server.*;

import javax.xml.crypto.Data;

public class UserService{
    MemoryUserDAO userMem;
    MemoryAuthDAO authMem;
    public UserService(){
        userMem = new MemoryUserDAO();
        authMem = new MemoryAuthDAO();
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException{
        // Check to see if username is taken
        if(userMem.getUser(request.username()) != null){
            throw new DataAccessException("username already taken");
        }
        // add user data
        userMem.addUser(UserDAO.createUser(request.username(), request.password(), request.email()));
        // create AuthData
        AuthData auth = AuthDAO.createAuth(request.username());
        // add authData
        authMem.addAuth(auth);
        return new RegisterResult(request.username(), auth.getAuthToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException{
        // check username exists
        if(userMem.getUser(request.username()) == null){
            throw new DataAccessException("username could not be found");
        }
        // check password is right
        if (userMem.getPassword(request.username(), request.password()) == null) {
            throw new DataAccessException("password did not match");
        }
        AuthData auth = AuthDAO.createAuth(request.username());
        authMem.addAuth(auth);
        return new LoginResult(request.username(), auth.getAuthToken());
    }
}
