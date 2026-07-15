package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import server.*;

import java.util.ArrayList;

public class UserService{
    MemoryUserDAO userMem;
    MemoryAuthDAO authMem;
    MemoryGameDAO gameMem;
    public UserService(){
        userMem = new MemoryUserDAO();
        authMem = new MemoryAuthDAO();
        gameMem = new MemoryGameDAO();
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException{
        // check to make sure user and pass are given
        if(request.username() == null || request.password() == null){
            throw new MissingDataException("username or password empty");
        }
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
        // check username was given
        if(request.username() == null || request.password() == null){
            throw new MissingDataException("username or password empty");
        }
        // check username exists
        else if(userMem.getUser(request.username()) == null){
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

    public LogoutResult logout(LogoutRequest request) throws DataAccessException{
        if(authMem.getAuth(request.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }
        authMem.remove(request.authToken());
        return null;
    }

    public CreateResult create(CreateRequest request) throws DataAccessException{
        if(request.gameName() == null){
            throw new MissingDataException("no game name");
        }
        if(authMem.getAuth(request.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }
        GameData game = new GameData(gameMem.createID());
        game.setGameName(request.gameName());
        gameMem.addGame(game);
        System.out.println(gameMem.size());
        return new CreateResult(game.getGameID());
    }

    public ArrayList<GameResult> list(ListRequest request) throws DataAccessException{
        if(authMem.getAuth(request.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }
        ArrayList<GameResult> result = new ArrayList<>();
        for(int i = 0; i < gameMem.size(); i++){
            result.add(new GameResult(gameMem.at(i)));
            System.out.println(new GameResult(gameMem.at(i)));
        }
        return result;
    }

    public JoinResult join(String authToken, JoinRequest request) throws DataAccessException{
        if(authMem.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
        if(request.playerColor() == null){
            throw new MissingDataException("request.color == null");
        }
        if(gameMem.getGame(request.gameID()) == null){
            System.out.println("GameID = " + request.gameID());
            throw new MissingDataException("gameID not found");
        }
        if(request.playerColor().equals("BLACK")){
            if(gameMem.getGame(request.gameID()).getBlackUsername() == null){
                gameMem.getGame(request.gameID()).setBlackUsername(authMem.getAuth(authToken).getUsername());
            }
            else{
                throw new DataAccessException("color taken");
            }
        }else{
            if(gameMem.getGame(request.gameID()).getWhiteUsername() == null){
                gameMem.getGame(request.gameID()).setWhiteUsername(authMem.getAuth(authToken).getUsername());
            }
            else {
                throw new DataAccessException("color taken");
            }
        }
        return new JoinResult(request.playerColor(), request.gameID());
    }

    public void clear(){
        userMem.clear();
        authMem.clear();
        gameMem.clear();
    }
}
