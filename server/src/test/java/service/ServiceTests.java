package service;
import dataaccess.DataAccessException;
import dataaccess.MissingDataException;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.LoginRequest;
import server.RegisterRequest;
import server.Server;

public class ServiceTests {
    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server server;
    private String existingAuth;

    // Make tests for:
    // REGISTER
    @Test
    @Order(1)
    @DisplayName("Register test (normal)")
    public void registerTestNorm(){
        // regular test
        String newUser = "username";
        RegisterRequest request = new RegisterRequest(newUser, "password", "urcool@gmail.com");
        UserService userService = new UserService();
        try {
            RegisterResult result = userService.register(request);
            Assertions.assertEquals(newUser, result.username(),
                    "Response did not have the same username as was registered");
            Assertions.assertNotNull(result.authToken(), "Response did not have an authToken");
        }
        catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(2)
    @DisplayName("Register test (bad)")
    public void registerTestBad(){
        // duplicate username
        String username = "username";
        RegisterRequest request = new RegisterRequest(username, "password", "urcool@gmail.com");
        UserService userService = new UserService();

        Assertions.assertDoesNotThrow(() -> userService.register(request));

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
    }
    @Test
    @Order(3)
    @DisplayName("Register test (bad)")
    public void registerTestNoPassword(){
        // Password is null
        String username = "username1";
        RegisterRequest request = new RegisterRequest(username, null, "urcool@gmail.com");
        UserService userService = new UserService();

        Assertions.assertThrows(MissingDataException.class, () -> {
            userService.register(request);
        });
    }
    // LOGIN
    @Test
    @Order(4)
    @DisplayName("Login normal")
    public void loginTest(){
        String newUser = "username";
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest(newUser, "password", "urcool@gmail.com");
        LoginRequest request = new LoginRequest(newUser, "password");

        try {
            RegisterResult registerResult = userService.register(registerRequest);
            LoginResult result = userService.login(request);
            Assertions.assertEquals(newUser, result.username(),
                    "Response did not have the same username as was registered");
            Assertions.assertNotNull(result.authToken(), "Response did not have an authToken");
        }
        catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(5)
    @DisplayName("Username not found")
    public void loginNotFound(){
        String username = "username";
        LoginRequest request = new LoginRequest(username, "password");
        UserService userService = new UserService();
        Assertions.assertThrows(DataAccessException.class, ()->{
            userService.login(request);
        });
    }
    @Test
    @Order(6)
    @DisplayName("Username null")
    public void loginUsernameNull(){
        String username = "username";
        LoginRequest request = new LoginRequest(null, "password");
        UserService userService = new UserService();
        Assertions.assertThrows(DataAccessException.class, ()->{
            userService.login(request);
        });
    }
    @Test
    @Order(7)
    @DisplayName("Password null")
    public void loginPasswordNull(){
        String username = "username";
        LoginRequest request = new LoginRequest(username, null);
        UserService userService = new UserService();
        Assertions.assertThrows(DataAccessException.class, ()->{
            userService.login(request);
        });
    }
    @Test
    @Order(8)
    @DisplayName("Incorrect Password")
    public void loginPasswordWrong(){
        String newUser = "username";
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest(newUser, "password", "urcool@gmail.com");
        LoginRequest request = new LoginRequest(newUser, "password1");
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.assertThrows(DataAccessException.class, () ->{
                userService.login(request);
            });
        }
        catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }
    // LOGOUT
    // CREATE
    // LIST
    // JOIN
    // CLEAR
}
