package dataaccess;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.*;
import service.*;


import static dataaccess.DatabaseManager.createDatabase;

public class DatabaseUnitTests {
    // Register Tests (add user)
    @Test
    @Order(1)
    @DisplayName("Register (database)")
    public void registerNorm() {
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, password, email);
        SQLUserService userService = new SQLUserService();
        try {
            // call to create a new database (if not already open)
            createDatabase();
            userService.clear();
            // register the user to the database
            RegisterResult result = userService.register(request);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Register duplicate account (database)")
    public void registerDuplicate() {
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, password, email);
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Register with null password (database)")
    public void registerNullPassword() {
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, null, email);
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Register with null username (database)")
    public void registerNullUsername() {
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(null, password, email);
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
    }

    // Login Test
    @Test
    @Order(5)
    @DisplayName("Login Norm")
    public void loginNorm() {
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        LoginRequest request = new LoginRequest(user, password);
        Assertions.assertDoesNotThrow(() -> {
            userService.login(request);
        });
    }

    @Test
    @Order(6)
    @DisplayName("Login with null password")
    public void loginNullPassword() {
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        LoginRequest request = new LoginRequest(user, null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    @Order(7)
    @DisplayName("Login with null username")
    public void loginNullUsername() {
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        LoginRequest request = new LoginRequest(null, password);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(request);
        });
    }

    // Logout Tests (getUser)
    @Test
    @Order(8)
    @DisplayName("Logout norm")
    public void logoutNorm() {
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try {
            LoginResult result = userService.login(loginRequest);
            userService.logout(new LogoutRequest(result.authToken()));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(9)
    @DisplayName("Logout mismatch authorization token")
    public void logoutBadAuth() {
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try {
            LoginResult result = userService.login(loginRequest);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.logout(new LogoutRequest("Heeheehoohoo"));
            });


        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    // Create Game
    @Test
    @Order(10)
    @DisplayName("Create game norm")
    public void createNorm(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try{
            LoginResult result = userService.login(loginRequest);
            userService.create(new CreateRequest(result.authToken(), "newGame"));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(11)
    @DisplayName("Make 10 games")
    public void createTen(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try{
            LoginResult result = userService.login(loginRequest);
            for(int i = 0; i < 9; i++){
                userService.create(new CreateRequest(result.authToken(), "newGame"));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(12)
    @DisplayName("Create game null gamename")
    public void createNullUsername(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try{
            LoginResult result = userService.login(loginRequest);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.create(new CreateRequest(result.authToken(), null));
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(13)
    @DisplayName("Create game bad authtoken")
    public void createBadAuth(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        String user = "username";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(user, password);
        try{
            LoginResult result = userService.login(loginRequest);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.create(new CreateRequest("Hello World!", "gameName"));
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(14)
    @DisplayName("Join Game Norm")
    public void joinNorm(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try{
            LoginResult result = userService.login(loginRequest);
            int gameId = userService.create(new CreateRequest(result.authToken(), "newGame")).gameID();
            JoinRequest request = new JoinRequest("BLACK", gameId);
            userService.join(result.authToken(), request);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(15)
    @DisplayName("Join Game Bad Color")
    public void joinBadColor(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try{
            LoginResult result = userService.login(loginRequest);
            int gameId = userService.create(new CreateRequest(result.authToken(), "newGame")).gameID();
            JoinRequest request = new JoinRequest("GREEN", gameId);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.join(result.authToken(), request);
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(16)
    @DisplayName("Join Game Bad AuthToken")
    public void joinBadAuth(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try{
            LoginResult result = userService.login(loginRequest);
            int gameId = userService.create(new CreateRequest(result.authToken(), "newGame")).gameID();
            JoinRequest request = new JoinRequest("BLACK", gameId);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.join("Heeheehoohoo", request);
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(17)
    @DisplayName("Join Game Bad GameID")
    public void joinBadGameId(){
        SQLUserService userService = new SQLUserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        // log someone in
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try{
            LoginResult result = userService.login(loginRequest);
            int gameId = userService.create(new CreateRequest(result.authToken(), "newGame")).gameID();
            JoinRequest request = new JoinRequest("BLACK", 100000);
            Assertions.assertThrows(DataAccessException.class, () -> {
                userService.join(result.authToken(), request);
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(18)
    @DisplayName("List Game")
    public void listGames(){

    }
}