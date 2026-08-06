package client;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;

import jakarta.websocket.*;

import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import static ui.EscapeSequences.*;


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
                    notificationHandler.notify(notification);
//                    if(notification.getServerMessageType() == ServerMessage.ServerMessa
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

    public void move(int oldCol, int oldRow, int newCol, int newRow, ChessBoard chessBoard) throws Exception{
        try{
            ChessPosition oldPos = new ChessPosition(oldRow, oldCol);
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece.PieceType promotion = null;
            if(newPos.getRow() == 8
                    && chessBoard.getPiece(oldPos).getPieceType() == ChessPiece.PieceType.PAWN
                    && chessBoard.getPiece(oldPos).getTeamColor() == ChessGame.TeamColor.WHITE){
                promotion = promotePiece();
            } else if(newPos.getRow() == 1
                    && chessBoard.getPiece(oldPos).getPieceType() == ChessPiece.PieceType.PAWN
                    && chessBoard.getPiece(oldPos).getTeamColor() == ChessGame.TeamColor.BLACK){
                promotion = promotePiece();
            }
            ChessMove move = new ChessMove(oldPos, newPos, promotion);
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authorization, gameId, username, new Gson().toJson(move), true, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private ChessPiece.PieceType  promotePiece(){
        System.out.printf("%sThis move will cause your pawn to be promoted.\nPlease pick a promotion piece type \n(%s%sQUEEN, BISHOP, KNIGHT, ROOK%s%s): ",
                BLUE, SET_TEXT_ITALIC, MAGENTA, RESET_TEXT_ITALIC, BLUE);
        Scanner myScan = new Scanner(System.in);
        String type = myScan.next();
        while(true){
            switch (type) {
                case "QUEEN", "Queen", "queen" -> {
                    return ChessPiece.PieceType.QUEEN;
                }
                case "BISHOP", "Bishop", "bishop" -> {
                    return ChessPiece.PieceType.BISHOP;
                }
                case "KNIGHT", "Knight", "knight" -> {
                    return ChessPiece.PieceType.KNIGHT;
                }
                case "ROOK", "Rook", "rook" -> {
                    return ChessPiece.PieceType.ROOK;
                }
                default -> {
                    System.out.printf("%sNo match found.\nPlease pick a promotion piece type \n(%s%sQUEEN, BISHOP, KNIGHT, ROOK%s%s): ",
                            BLUE, SET_TEXT_ITALIC, MAGENTA, RESET_TEXT_ITALIC, BLUE);
                    type = myScan.next();
                }
            }
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
