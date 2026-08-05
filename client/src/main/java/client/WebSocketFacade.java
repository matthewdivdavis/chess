package client;

import com.google.gson.Gson;
import exception.ResponseException;

import jakarta.websocket.*;

import request.*;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    static String authorization = null;
    static String username = null;
    static int gameId = 0;
    NotificationHandler notificationHandler;
    Session session;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);


            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void connect(int gameId, String authToken, String username) throws Exception {
        authorization = authToken;
        this.username = username;
        this.gameId = gameId;
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId, username, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public void move(int oldCol, int oldRow, int newCol, int newRow) throws Exception{
        System.out.println("oldCol = " + oldCol + "\noldRow = " + oldRow + "\nnewCol = " + newCol + "\nnewRow = " + newRow);
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authorization, gameId, username, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
