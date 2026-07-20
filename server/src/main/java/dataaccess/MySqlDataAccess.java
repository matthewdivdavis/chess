package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;
import service.SQLUserService;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MySqlDataAccess implements DataAccess{

    public MySqlDataAccess() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public UserData addUser(UserData userData) throws ResponseException {
        var statement = "INSERT INTO `user` (username, password, email) VALUES(?, ?, ?)";
        try{
            executeUserUpdate(statement, userData);
        } catch (ResponseException e){
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return userData;
    }

    public AuthData addAuth(AuthData authData) throws ResponseException{
        var statement = "INSERT INTO authorization (username, authToken) VALUES(?, ?)";
        try{
            executeAuthUpdate(statement, authData);
        } catch (ResponseException e){
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        } catch (DataAccessException e){
            throw new RuntimeException(e);
        }
        return authData;
    }

    public UserData getUser(String username) throws ResponseException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, email, password FROM user WHERE username=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readUser(rs);
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public boolean checkPassword(String username, String password) throws ResponseException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, email, password FROM user WHERE username=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return BCrypt.checkpw(password, readUser(rs).password());
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return false;
    }

    public AuthData getAuth(String authToken) throws ResponseException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM authorization WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void setBlack(int gameId, String authToken){
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "UPDATE gamedata SET blackUsername=? WHERE gameId=?";
            try(PreparedStatement p = conn.prepareStatement(statement)){
                p.setString(1, getAuth(authToken).getUsername());
                p.setInt(2, gameId);
                p.executeUpdate();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setWhite(int gameId, String authToken){
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "UPDATE gamedata SET whiteUsername=? WHERE gameId=?";
            try(PreparedStatement p = conn.prepareStatement(statement)){
                p.setString(1, getAuth(authToken).getUsername());
                p.setInt(2, gameId);
                p.executeUpdate();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void remove(String authToken) throws ResponseException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM authorization WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int createGame(String gameName) throws ResponseException{
        var statement = "INSERT INTO gamedata (gameName) VALUES(?)";
        try{
            return executeGameUpdate(statement, gameName);
        } catch (ResponseException e){
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public GameData getGame(int gameID) throws ResponseException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameId, blackUsername, whiteUsername, gameName, gameJson FROM gamedata WHERE gameId=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private int executeGameUpdate(String statement, String gameName) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement p = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                p.setString(1, gameName);
                p.executeUpdate();
                try (ResultSet rs = p.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new SQLException("No key generated");
                    }
                }
            }
        }catch (SQLException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
    }

    private GameData readGame(ResultSet rs) throws SQLException{
        GameData gameData = new GameData(rs.getInt("gameId"));
        if(rs.getString("blackUsername") != null){
            gameData.setBlackUsername(rs.getString("blackUsername"));
        }
        if(rs.getString("whiteUsername") != null){
            gameData.setWhiteUsername(rs.getString("whiteUsername"));
        }
        if(rs.getString("gameName") != null){
            gameData.setGameName(rs.getString("gameName"));
        }
        return gameData;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException{
        AuthData authData = new AuthData(rs.getString("username"));
        authData.setAuthToken(rs.getString("authToken"));
        return authData;
    }

    public GameList listGames() throws ResponseException {
        try(Connection conn = DatabaseManager.getConnection()){
            GameList games = new GameList();
            var statement = "SELECT * FROM gamedata";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        games.addGame(readGame(rs));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `user` (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL UNIQUE,
            PRIMARY KEY (username))""",
            """
            CREATE TABLE IF NOT EXISTS authorization (
            `username` varchar(256) NOT NULL,
            `authToken` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            INDEX(username))""",
            """
            CREATE TABLE IF NOT EXISTS gamedata (
            `gameId` int NOT NULL AUTO_INCREMENT,
            `blackUsername` varchar(256) DEFAULT NULL,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `gameJson` TEXT DEFAULT NULL,
            PRIMARY KEY (`gameId`),
            INDEX(blackUsername),
            INDEX(whiteUsername),
            INDEX(gameName))"""
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try(Connection conn = DatabaseManager.getConnection()){
            for(String statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex){
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private void executeUserUpdate(String statement, UserData userData) throws ResponseException, DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, userData.username());
                ps.setString(2, userData.password());
                ps.setString(3, userData.email());
                ps.executeUpdate();
                return;
            }
        } catch (SQLException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
    private void executeAuthUpdate(String statement, AuthData authData) throws ResponseException, DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authData.getUsername());
                ps.setString(2, authData.getAuthToken());
                ps.executeUpdate();
                return;
            }
        } catch (SQLException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    /**
     * Clears all data from the database by truncating every table.
     */
    public void clearDatabase() throws DataAccessException {
        var truncateStatements = new String[]{
                "TRUNCATE TABLE `user`",
                "TRUNCATE TABLE authorization",
                "TRUNCATE TABLE gamedata"
        };
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : truncateStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear database", ex);
        }
    }
}
