package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.MySqlDataAccess;
import exception.DataAccessException;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import server.Server;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.http.HttpClient;

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
    public void handleMessage(WsMessageContext ctx) {
        try{
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> {
                    System.out.println("\nConnecting\n");
                    if(checkLogin(userGameCommand)){
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                                userGameCommand.getGameID(), null, null)));
                        enter(userGameCommand.getUsername(), ctx.session);
                    }
                    else{
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                                null, null, "message: You are not authorized")));
                        error(userGameCommand.getUsername(), ctx.session);
                    }
                }
                case MAKE_MOVE -> {
                    System.out.println("ctx.message(): " + ctx.message());
                    if(validMove(userGameCommand, ctx)){
                        System.out.println("\nMaking Move\n");
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                                userGameCommand.getGameID(), null, null)));
                        makeMove(userGameCommand.getUsername(), ctx.session);
                    }
                    else{
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                                null, null, "message: Make move error")));
                    }
                }
                case LEAVE -> System.out.println("\n\n\n" + ctx.message() + "\n\n\n");
                case RESIGN -> {
                    System.out.println("\n\n\n" + ctx.message() + "\n\n\n");
                    ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            null, "Resigning...", null)));
                    resign(userGameCommand.getUsername(), ctx.session, userGameCommand.getGameID());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void resign(String username, Session session, int gameId) throws IOException{
        if(connections.contains(session)){
            var message = String.format("message: %s has resigned", username);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
            connections.broadcast(session, notification);
            connections.remove(session);
        }
    }

    private void enter(String username, Session session) throws IOException{
        connections.add(session);
        var message = String.format("message: %s has joined the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification);
    }

    private void checkMateMessage(String color, Session session) throws IOException {
        var message = String.format("message: %s is in checkmate. ", color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification);
    }

    private void isInCheckMessage(String color, Session session) throws IOException {
        var message = String.format("message: %s is in check. ", color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification);
    }

    private void makeMove(String username, Session session) throws IOException{
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, 0, null, null);
        connections.broadcast(session, notification);
        var message = String.format("message: %s has made a move", username);
        notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification);
    }

    private void error(String username, Session session) throws IOException{
        var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "errorMessage: You are not authorized");
        connections.sendMessage(session, notification);
    }

    private boolean checkTurn(ChessMove move, int gameId, String authToken){
        try{
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();

            AuthData authData = mySqlDataAccess.getAuth(authToken);
            GameData gameData = mySqlDataAccess.getGame(gameId);
            ChessGame chessGame = gameData.getGame();
            if(chessGame.getTeamTurn() == chessGame.getBoard().getPiece(move.getStartPosition()).getTeamColor()){
                if(chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && gameData.getBlackUsername().equals(authData.getUsername())){
                    return true;
                }
                else if (gameData.getWhiteUsername().equals(authData.getUsername())){
                    return true;
                }
            }
        } catch (DataAccessException | ResponseException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private boolean checkMove(ChessMove move, int gameId, WsMessageContext ctx){
        try{
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            ChessGame chessGame = mySqlDataAccess.getGame(gameId).getGame();
            chessGame.setBoard(mySqlDataAccess.getGame(gameId).getGame().getBoard());
            if(chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) || chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
                System.out.println("CheckMove Error");
                return false;
            }
            for(ChessMove m : chessGame.validMoves(move.getStartPosition())){
                if(move.equals(m)){
                    chessGame.makeMove(move);
                    mySqlDataAccess.updateGame(gameId, chessGame);
                    if(chessGame.isInCheckmate(chessGame.getTeamTurn())){
                        checkMateMessage(chessGame.getTeamTurn().toString(), ctx.session);
                        String msg = "message: " + chessGame.getTeamTurn().toString() + " is in checkmate";
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, msg, null)));
                    }
                    if(chessGame.isInCheck(chessGame.getTeamTurn())){
                        isInCheckMessage(chessGame.getTeamTurn().toString(), ctx.session);
                        String msg = "message: " + chessGame.getTeamTurn().toString() + " is in check";
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, msg, null)));
                    }
                    return true;
                }
            }
        }catch (DataAccessException | ResponseException | InvalidMoveException | IOException e) {
            System.out.println(e.toString());
        }
        System.out.println("CheckMove Error After If");
        return false;
    }

    private boolean validMove(UserGameCommand userGameCommand, WsMessageContext ctx){
        return checkAuth(userGameCommand.getAuthToken())
                && checkTurn(userGameCommand.getMove(), userGameCommand.getGameID(), userGameCommand.getAuthToken())
                && checkMove(userGameCommand.getMove(), userGameCommand.getGameID(), ctx);
    }

    private boolean checkLogin(UserGameCommand userGameCommand){
        return checkAuth(userGameCommand.getAuthToken())
                && checkGameId(userGameCommand.getGameID());
    }

    private boolean checkUsername(String username){
        try {
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            if(mySqlDataAccess.getUser(username) != null){
                return true;
            }
        } catch (DataAccessException | ResponseException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private boolean checkGameId(int gameId){
        try {
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            if(mySqlDataAccess.getGame(gameId) != null){
                return true;
            }
        } catch (DataAccessException | ResponseException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private boolean checkAuth(String auth){
        try {
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            if(mySqlDataAccess.getAuth(auth) != null){
                return true;
            }
        } catch (DataAccessException | ResponseException e) {
            System.out.println(e.toString());
        }
        System.out.println("Bad Auth");
        return false;
    }
}
