package serverpractice;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WsEchoClient extends Endpoint {
    public Session session;

    public static void main(String[] args) throws Exception{
        WsEchoClient client = new WsEchoClient();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to ehco: ");
        while(true){
            String input = scanner.nextLine();
//            System.out.println(input);
            client.send(input);
        }
    }

    public WsEchoClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");

        WebSocketContainer container =
                ContainerProvider.getWebSocketContainer();

        container.connectToServer(this, uri);
    }


    public void send(String message) throws IOException {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        } else {
            System.out.println("WebSocket is not connected.");
        }
    }
    // This method must be overridden, but we don't have to do anything with it
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println(message);
                System.out.println("\nEnter another message you want to echo: ");
            }
        });

        System.out.println("Connected!");
    }
}
