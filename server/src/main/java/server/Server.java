package server;

import handlers.*;
import io.javalin.*;

import service.SQLUserService;

import websocket.WebSocketHandler;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class Server {

    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler;

    public Server() {

        webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });

        SQLUserService userService = new SQLUserService();


        // Register your endpoints and exception handlers here.
        javalin.post("/user", new RegisterHandler(userService));
        javalin.post("/session", new LoginHandler(userService));
        javalin.delete("/session", new LogoutHandler(userService));
        javalin.post("/game", new CreateHandler(userService));
        javalin.get("/getGame/{gameId}", new GetGameHandler(userService));
        javalin.get("/game", new ListHandler(userService));
        javalin.put("/game", new JoinHandler(userService));
        javalin.delete("/db", new ClearHandler(userService));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
}
