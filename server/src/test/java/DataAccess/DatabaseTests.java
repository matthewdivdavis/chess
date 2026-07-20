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
//            System.out.println(result.toString());

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
            Assertions.assertDoesNotThrow(userService.logout(new LogoutRequest(result.authToken())));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

//    }
//    @Test
//    @Order(4)
//    @DisplayName("Login (database)")
//    public void loginNorm(){
//        String user = "username";
//        String password = "password";
//        LoginRequest request = new LoginRequest(user, password);
//        SQLUserService userService = new SQLUserService();
//        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
//        Assertions.assertDoesNotThrow(userService.login(request));
//    }

}