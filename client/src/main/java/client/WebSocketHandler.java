package client;

import io.javalin.websocket.WsConnectContext;

public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx){
        System.out.println("Websocket connected!");
        ctx.enableAutomaticPings();
    }
}
