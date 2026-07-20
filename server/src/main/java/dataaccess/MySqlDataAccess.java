package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.sql.*;

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
            var statement = "SELECT username, email, password, json FROM user WHERE username=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public boolean checkPassword(String username, String password) throws ResponseException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, email, password FROM user WHERE username=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        if(readUser(rs).password() == password){
                            return true;
                        }
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
            var statement = "SELECT username, authToken FROM authorization WHERE username=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
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

    public void remove(String authToken) throws ResponseException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "DELTE FROM authorization WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        UserData userData = new Gson().fromJson(json, UserData.class);
        return userData;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException{
        var json = rs.getString("json");
        AuthData authData = new Gson().fromJson(json, AuthData.class);
        return authData;
    }

    public GameList listGames() throws ResponseException {

        return null;
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
