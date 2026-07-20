package server;

import handlers.*;
import io.javalin.*;
import io.javalin.http.Handler;
import service.SQLUserService;
import service.UserService;
import sqlhandlers.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        SQLUserService userService = new SQLUserService();


        // Register your endpoints and exception handlers here.
        javalin.post("/user", new SQLRegisterHandler(userService));
        javalin.post("/session", new SQLLoginHandler(userService));
        javalin.delete("/session", new SQLLogoutHandler(userService));

        javalin.post("/game", new SQLCreateHandler(userService));

        javalin.get("/game", new SQLListHandler(userService));
        javalin.put("/game", new SQLJoinHandler(userService));
        javalin.delete("/db", new SQLClearHandler(userService));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
}
