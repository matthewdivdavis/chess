package service;
import dataaccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
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

    // LOGIN
    // LOGOUT
    // CREATE
    // LIST
    // JOIN
    // CLEAR
}
