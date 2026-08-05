package client;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;

public class WsClient extends Endpoint {
    public Session session;
    public int port = 8080;

    public WsClient() throws Exception{
        URI uri = new URI("ws://localhost:" + port + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message){
                System.out.println("WS MESSAGE: "+message);
            }
        });
    }

    public void connect() throws IOException {
        session.getBasicRemote().sendText("CONNECT");
    }


    public void onOpen(Session session, EndpointConfig endpointConfig){
    }
}
