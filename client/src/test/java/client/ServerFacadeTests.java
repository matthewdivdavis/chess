package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.LoginRequest;
import server.RegisterRequest;
import server.Server;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register a new user")
    public void registerNorm(){
        Assertions.assertDoesNotThrow(ServerFacade::clear);
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.register(new RegisterRequest("matt", "matt", "matt"));
        });
    }

    @Test
    @Order(2)
    @DisplayName("Login normal")
    public void loginNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.login(new LoginRequest("matt", "matt"));
        });
    }
    @Test
    @Order(3)
    @DisplayName("Create normal")
    public void createNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.create("matthewGame");
        });
    }
    @Test
    @Order(4)
    @DisplayName("Join normal")
    public void joinNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.join(1, "BLACK");
        });
    }
    @Test
    @Order(5)
    @DisplayName("List normal")
    public void ListNorm(){
        Assertions.assertDoesNotThrow(ServerFacade::clear);
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.register(new RegisterRequest("matt", "matt", "matt"));
        });
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.list();
        });
    }

    @Test
    @Order(6)
    @DisplayName("Observe normal")
    public void observeNorm(){
        Assertions.assertDoesNotThrow(ServerFacade::clear);
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.register(new RegisterRequest("matt", "matt", "matt"));
        });
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.observe(1);
        });
    }
}
