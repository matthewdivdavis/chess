package client;

import com.google.gson.Gson;
import exception.ResponseException;

import jakarta.websocket.*;
import request.*;
import server.WsClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade {
    private static int port;
    private static final String SERVER_URL = "http://localhost:";
    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
//                    Notification notification = new Gson().fromJson(message, Notification.class);
//                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public static void connectGame(){
    }

    public static void move(MoveRequest req) throws Exception{
        System.out.println(req);
        System.out.println("INSIDE THE MOVE FUNCTION");
        // Connect to websocket
        WsClient client = new WsClient();
        client.connect();

        // get game board
        // validate move
//
//        ChessGame chess = new ChessGame();
//        ChessBoard board = new ChessBoard();
//        chess.setBoard(board);
//        ChessPosition start = new ChessPosition(req.startRow(), req.startCol());
//        ChessPosition end = new ChessPosition(req.endRow(), req.endCol());
//        ChessMove move = new ChessMove(start, end, board.getPiece(start).getPieceType());
//        chess.validMoves(start);
//
//
//        Gson gson = new Gson();
//        String json = gson.toJson(req);

    }
}
