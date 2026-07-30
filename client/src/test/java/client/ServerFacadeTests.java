package client;

import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;


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
    @DisplayName("Register bad")
    public void registerBad(){
        try{
            ServerFacade.register(new RegisterRequest("matt", null, "urcool@gmail.com"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Register bad")
    public void registerBadUser(){
        try{
            ServerFacade.register(new RegisterRequest(null, "matt", "urcool@gmail.com"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Register bad")
    public void registerBadEmail(){
        try{
            ServerFacade.register(new RegisterRequest("matt", "matt", null));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login normal")
    public void loginNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.login(new LoginRequest("matt", "matt"));
        });
    }

    @Test
    @Order(5)
    @DisplayName("Login bad (password)")
    public void loginBad(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.login(new LoginRequest("matt", null));
        });
    }

    @Test
    @Order(6)
    @DisplayName("Login bad (username)")
    public void loginBadUser(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.login(new LoginRequest(null, "matt"));
        });
    }

    @Test
    @Order(7)
    @DisplayName("Create normal")
    public void createNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.create("matthewGame");
        });
    }

    @Test
    @Order(8)
    @DisplayName("Create bad")
    public void createBad(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.create(null);
        });
    }

    @Test
    @Order(9)
    @DisplayName("Join normal")
    public void joinNorm(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.join(1, "BLACK");
        });
    }
    @Test
    @Order(10)
    @DisplayName("Join normal")
    public void joinNormColor(){
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.join(1, "WHITE");
        });
    }

    @Test
    @Order(12)
    @DisplayName("Join bad (color)")
    public void joinBad(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.join(1, "GREEN");
        });
    }
    @Test
    @Order(13)
    @DisplayName("Join bad (color)")
    public void joinBadBlue(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.join(1, "BLUE");
        });
    }
    @Test
    @Order(14)
    @DisplayName("Join bad (color)")
    public void joinBadBlack(){
        Assertions.assertThrows(Exception.class, () -> {
            ServerFacade.join(1, "black");
        });
    }


    @Test
    @Order(15)
    @DisplayName("List normal")
    public void listNorm(){
        Assertions.assertDoesNotThrow(ServerFacade::clear);
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.register(new RegisterRequest("matt", "matt", "matt"));
        });
        Assertions.assertDoesNotThrow(() -> {
            ServerFacade.list();
        });
    }

    @Test
    @Order(16)
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
