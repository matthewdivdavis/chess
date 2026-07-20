package DataAccess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.MissingDataException;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.*;
import service.*;


import static dataaccess.DatabaseManager.createDatabase;

public class DatabaseTests {
    // Register Tests (add user)
    @Test
    @Order(1)
    @DisplayName("Register (database)")
    public void registerNorm(){
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, password, email);
        UserService userService = new UserService();
        try {
            // call to create a new database (if not already open)
            createDatabase();
            userService.clear();
            // register the user to the database
            RegisterResult result = userService.register(request);
//            System.out.println(result.toString());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(2)
    @DisplayName("Register duplicate account (database)")
    public void registerDuplicate(){
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, password, email);
        UserService userService = new UserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () ->{
            userService.register(request);
        });
    }
    @Test
    @Order(3)
    @DisplayName("Register with null password (database)")
    public void registerNullPassword(){
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(user, null, email);
        UserService userService = new UserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () ->{
            userService.register(request);
        });
    }
    @Test
    @Order(4)
    @DisplayName("Register with null username (database)")
    public void registerNullUsername(){
        String user = "username";
        String password = "password";
        String email = "urcool@gmail.com";
        RegisterRequest request = new RegisterRequest(null, password, email);
        UserService userService = new UserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertThrows(DataAccessException.class, () ->{
            userService.register(request);
        });
    }
    // Logout Tests (getUser)
    @Test
    @Order(4)
    @DisplayName("Login (database)")
    public void loginNorm(){
        String user = "username";
        String password = "password";
        LoginRequest request = new LoginRequest(user, password);
        UserService userService = new UserService();
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
        Assertions.assertDoesNotThrow(userService.login(request));
    }
}
