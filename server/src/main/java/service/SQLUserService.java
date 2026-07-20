package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.*;
import serviceSQL.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class SQLUserService implements ClearService,
        CreateService, LoginService, RegisterService,
        JoinService, ListService, LogoutService {
    MySqlDataAccess sqlDataAccess;

    public SQLUserService(){
        try{
            sqlDataAccess = new MySqlDataAccess();
        }
        catch (ResponseException | DataAccessException e){
            throw new RuntimeException(e);
        }

    }
    @Override
    public RegisterResult register(RegisterRequest request) throws DataAccessException{
        // check to make sure user and pass are given
        if(request.username() == null || request.password() == null){
            throw new MissingDataException("username or password empty");
        }
        String hashPass = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        try{
            // Check to see if username is taken
            if(sqlDataAccess.getUser(request.username()) != null){
                throw new DataAccessException("username already taken");
            }
            // add user data
            sqlDataAccess.addUser(new UserData(request.username(), hashPass, request.email()));
            AuthData auth = new AuthData(request.username());
            sqlDataAccess.addAuth(auth);
            return new RegisterResult(request.username(), auth.getAuthToken());
        } catch (ResponseException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public LoginResult login(LoginRequest request) throws DataAccessException{
        // check username was given
        if(request.username() == null || request.password() == null){
            throw new MissingDataException("username or password empty");
        }
        try{
            // check username exists
            if(sqlDataAccess.getUser(request.username()) == null){
                throw new DataAccessException("username could not be found");
            }
            // check password is right
            if (!sqlDataAccess.checkPassword(request.username(), request.password())) {
                throw new DataAccessException("password did not match");
            }
            AuthData auth = new AuthData(request.username());
            sqlDataAccess.addAuth(auth);
            return new LoginResult(request.username(), auth.getAuthToken());
        }catch (ResponseException e) {
            throw new DataAccessException(e.toString());
        }

    }
    @Override
    public LogoutResult logout(LogoutRequest request) throws DataAccessException{
        try{
            if(sqlDataAccess.getAuth(request.authToken()) == null){
                throw new DataAccessException("unauthorized");
            }
            if(!sqlDataAccess.getAuth(request.authToken()).getAuthToken().equals(request.authToken())){
                throw new DataAccessException("token mismatch");
            }
            sqlDataAccess.remove(request.authToken());
            return new LogoutResult(request.authToken());
        } catch (ResponseException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public CreateResult create(CreateRequest request) throws DataAccessException{
        if(request.gameName() == null){
            throw new MissingDataException("no game name");
        }
        try{
            if(sqlDataAccess.getAuth(request.authToken()) == null){
                throw new DataAccessException("unauthorized");
            }
            int gameID = sqlDataAccess.createGame(request.gameName());
            return new CreateResult(gameID);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ArrayList<GameResult> list(ListRequest request) throws DataAccessException{
        try{
            if(sqlDataAccess.getAuth(request.authToken()) == null){
                throw new DataAccessException("unauthorized");
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        try{
            GameList list = sqlDataAccess.listGames();
            ArrayList<GameResult> result = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                result.add(new GameResult(list.at(i)));
            }
            return result;
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public JoinResult join(String authToken, JoinRequest request) throws DataAccessException, MissingDataException{
        if(request.playerColor() == null){
            throw new MissingDataException("request.color == null");
        }
        if(!request.playerColor().equals("BLACK") && !request.playerColor().equals("WHITE")){
            throw new MissingDataException("color does not exist");
        }
        try{
            // bad auth
            if(sqlDataAccess.getAuth(authToken) == null){
                throw new DataAccessException("unauthorized");
            }
            // game ID doesn't exist
            if(sqlDataAccess.getGame(request.gameID()) == null){
                throw new MissingDataException("gameID not found");
            }
            if(request.playerColor().equals("BLACK")){
                if(sqlDataAccess.getGame(request.gameID()).getBlackUsername() == null){
                    sqlDataAccess.setBlack(request.gameID(), authToken);
                    sqlDataAccess.getGame(request.gameID()).setBlackUsername(sqlDataAccess.getAuth(authToken).getUsername());
                }
                else{
                    throw new NameTakenException("color 'BLACK' taken");
                }
            }else{
                if(sqlDataAccess.getGame(request.gameID()).getWhiteUsername() == null){
                    sqlDataAccess.setWhite(request.gameID(), authToken);

                }
                else {
                    throw new DataAccessException("color 'WHITE' taken");
                }
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return new JoinResult(request.playerColor(), request.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        try{
            sqlDataAccess.clearDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}
