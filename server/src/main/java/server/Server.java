package server;

import com.google.gson.Gson;
import handlers.*;
import io.javalin.*;
import io.javalin.http.Handler;
import service.SQLUserService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class Server {

    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .ws("/ws", ws -> {
                    ws.onConnect(WebSocketHandler);
                    ws.onMessage(ctx ->{
                        System.out.println("\n\n\n" + ctx.message() + "\n\n\n");
                        UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                                userGameCommand.getGameID())));
                    });
                    ws.onClose(ctx -> System.out.println("Websocket Closed"));
                });

        SQLUserService userService = new SQLUserService();


        // Register your endpoints and exception handlers here.
        javalin.post("/user", new RegisterHandler(userService));
        javalin.post("/session", new LoginHandler(userService));
        javalin.delete("/session", new LogoutHandler(userService));
        javalin.post("/game", new CreateHandler(userService));
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
