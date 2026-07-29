package client;

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
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @Test
//    public void sampleTest() {
//        Assertions.assertTrue(true);
//    }

//    @Test
//    @Order(1)
//    @DisplayName("Register a new user")
//    public void registerNorm(){
//        String username = "username";
//        String password = "password";
//        String email = "urcool@gmail.com";
//    }




}
