package server;

import handlers.*;
import io.javalin.*;
import io.javalin.http.Handler;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserService userService = new UserService();


        // Register your endpoints and exception handlers here.
        javalin.post("/user", new RegisterHandler(userService));
        javalin.post("/session", new LoginHandler(userService));
//        javalin.delete("/session", new LogoutHandler(userService));
//        javalin.get("/game", new ListHandler(userService));
//        javalin.post("/game", new CreateHandler(userService));
//        javalin.put("/game", new JoinHandler(userService));
//        javalin.delete("/db", new ClearHandler(userService));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
