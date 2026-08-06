package client;

import com.google.gson.Gson;
import exception.ResponseException;

import jakarta.websocket.*;

import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    static String authorization = null;
    static String username = null;
    int gameId = 0;
    NotificationHandler notificationHandler;
    Session session;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            System.out.println(socketURI);
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if(notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        System.out.println("ERROROREOREROJOJFSJOSODJFO SOSJD FOJS DOFJ ");
                    }
                    else {
                        notificationHandler.notify(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public boolean observe(int gameId, String authToken){
        authorization = authToken;
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authorization, gameId, null, null, false, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            return true;
        } catch (IOException e){
            System.out.println(e.toString());
            return false;
        }
    }

    public void join(int gameId, String authToken, String color) throws Exception {
        authorization = authToken;
        this.gameId = gameId;
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId, null, null, true, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public void leave(String username, int gameId){
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authorization, gameId, null, null, false, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void resign(String username, int gameId){
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authorization, gameId, null, null, false, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void move(int oldCol, int oldRow, int newCol, int newRow) throws Exception{
        System.out.println("oldCol = " + oldCol + "\noldRow = " + oldRow + "\nnewCol = " + newCol + "\nnewRow = " + newRow);
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authorization, gameId, username, null, true, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    @Override
    public void onClose(Session session, CloseReason closeReason){
        System.out.println("Client websocket closed. Reason: " + closeReason);
    }
}
