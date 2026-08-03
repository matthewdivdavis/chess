package websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx){
        System.out.println("Websocket connected!");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleClose(WsCloseContext ctx){
        System.out.println("Websocket Closed");
    }

@Override
    public void handleMessage(WsMessageContext ctx) throws Exception {
        System.out.println("\n\n\n" + ctx.message() + "\n\n\n");
        UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                userGameCommand.getGameID())));
    }
}
