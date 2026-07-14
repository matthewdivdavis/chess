package server;

import handlers.*;
import io.javalin.*;
import io.javalin.http.Handler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", new RegisterHandler());
        javalin.post("/session", new LoginHandler());
        javalin.delete("/session", new LogoutHandler());
        javalin.get("/game", new ListHandler());
        javalin.post("/game", new CreateHandler());
        javalin.put("/game", new JoinHandler());
        javalin.delete("/db", new ClearHandler());

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
