package service;

import dataaccess.*;
import server.*;

public class UserService {
    static MemoryUserDAO userMemory = new MemoryUserDAO();
    static MemoryAuthDAO authMemory = new MemoryAuthDAO();
    public static RegisterResult register(RegisterRequest request) throws DataAccessException{
        if(userMemory.getUser(request.username()) != null){
            throw new DataAccessException("Username Already Exists");
        }
        UserDAO user = new UserDAO(request);
        userMemory.addUser(user.getUser());
        AuthDAO auth = new AuthDAO();
        auth.createAuth(request.username());
        authMemory.addAuth(auth.getAuth());
        return new RegisterResult(request.username(), auth.getAuth().getAuthToken());
    }
}
