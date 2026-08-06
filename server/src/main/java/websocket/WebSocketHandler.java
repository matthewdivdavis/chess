package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.MySqlDataAccess;
import exception.DataAccessException;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;


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
                    if(!userGameCommand.getPlay()){
                        System.out.println(ctx.message());
                        enter(userGameCommand, ctx.session, "joined as an observer to");
                    }
                    else if(checkLogin(userGameCommand)){
                        System.out.println(ctx.message());
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                                userGameCommand.getGameID(), null, null)));
                        enter(userGameCommand, ctx.session, "joined");
                    }
                    else{
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                                null, null, "message: You are not authorized")));
                    }
                }
                case MAKE_MOVE -> {
                    System.out.println("ctx.message(): " + ctx.message());
                    if(validMove(userGameCommand, ctx)){
                        System.out.println("\nMaking Move\n");
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                                userGameCommand.getGameID(), null, null)));
                        makeMove(getUsername(userGameCommand), ctx.session, userGameCommand.getGameID());
                    }
                }
                case LEAVE -> {
                    leave(userGameCommand, ctx.session);
                }
                case RESIGN -> {
                    System.out.println("\n\n\n" + ctx.message() + "\n\n\n");
                    resign(userGameCommand, ctx.session, userGameCommand.getGameID(), ctx);

                }
            }
        } catch (IOException | DataAccessException e){
//            e.printStackTrace();
            System.out.println("Error in server");
        }

    }

    private void leave(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException{
        var message = String.format("message: %s has left the game", getUsername(userGameCommand));
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        try{
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            AuthData authData = mySqlDataAccess.getAuth(userGameCommand.getAuthToken());
            GameData gameData = mySqlDataAccess.getGame(userGameCommand.getGameID());
            System.out.println(authData.getUsername());
            if(gameData.getBlackUsername() != null
                    && gameData.getBlackUsername().equals(authData.getUsername())){
                gameData.setBlackUsername(null);
                mySqlDataAccess.updateBlack(userGameCommand.getGameID(), null);
            } else if(gameData.getWhiteUsername() != null
                    && gameData.getWhiteUsername().equals(authData.getUsername())){
                gameData.setWhiteUsername(null);
                mySqlDataAccess.updateWhite(userGameCommand.getGameID(), null);
            }
            mySqlDataAccess.updateGame(userGameCommand.getGameID(), gameData.getGame());
        } catch(DataAccessException | ResponseException e ){
            System.out.println(e.toString());
        }
        connections.broadcast(session, notification, userGameCommand.getGameID());
        connections.remove(session, userGameCommand.getGameID());
    }

    private String getUsername(UserGameCommand userGameCommand){
        try{
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
            return mySqlDataAccess.getAuth(userGameCommand.getAuthToken()).getUsername();
        } catch (DataAccessException | ResponseException e){
            System.out.println(e.toString());
        }
        return null;
    }

    private void enter(UserGameCommand userGameCommand, Session session, String join) throws IOException, DataAccessException{
        connections.add(session, userGameCommand.getGameID());
        var message = String.format("message: %s has %s the game", getUsername(userGameCommand), join);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification, userGameCommand.getGameID());
    }

    private boolean isObserver(GameData gameData, UserGameCommand userGameCommand){
        if((gameData.getBlackUsername() != null
                && gameData.getBlackUsername().equals(getUsername(userGameCommand) )
                || (gameData.getWhiteUsername() != null
                    && gameData.getWhiteUsername().equals(getUsername(userGameCommand))))){
            return false;
        }
        return true;
    }

    private boolean resign(UserGameCommand userGameCommand, Session session, int gameId, WsMessageContext ctx)
            throws IOException, DataAccessException{
        if(connections.contains(session, userGameCommand.getGameID())){
            var message = String.format("message: %s has resigned", getUsername(userGameCommand));
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
            try{
                MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
                GameData gameData = mySqlDataAccess.getGame(gameId);
                if(isObserver(gameData, userGameCommand)){
                    ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                            null, null, "errorMessage: Observer cannot resign, choose leave game!")));
                    return false;
                }
                if(gameData.getGame().getWinner() != null){
                    ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                            null, null, "errorMessage: Other team already resigned. You won!")));
                    return false;
                }
                gameData.setLoser(getUsername(userGameCommand));
                mySqlDataAccess.updateGame(gameId, gameData.getGame());
            } catch (DataAccessException | ResponseException e) {
                System.out.println(e.toString());
            }
            connections.broadcast(session, notification, userGameCommand.getGameID());
            connections.remove(session, userGameCommand.getGameID());
            return true;
        }
        return false;
    }

    private void checkMateMessage(String color, Session session, int gameId) throws IOException, DataAccessException{
        var message = String.format("message: %s is in checkmate. ", color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification, gameId);
    }

    private void isInCheckMessage(String color, Session session, int gameId) throws IOException, DataAccessException {
        var message = String.format("message: %s is in check. ", color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification, gameId);
    }

    private void makeMove(String username, Session session, int gameId) throws IOException, DataAccessException{
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, 0, null, null);
        connections.broadcast(session, notification, gameId);
        var message = String.format("message: %s has made a move", username);
        notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null);
        connections.broadcast(session, notification, gameId);
    }

    private boolean checkTurn(ChessMove move, int gameId, String authToken){
        try{
            MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();

            AuthData authData = mySqlDataAccess.getAuth(authToken);
            GameData gameData = mySqlDataAccess.getGame(gameId);
            if(gameData.getGame().getWinner() != null){
                return false;
            }
            ChessGame chessGame = gameData.getGame();
            if(chessGame.getBoard().getPiece(move.getStartPosition()) == null){
                return false;
            }
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
                        checkMateMessage(chessGame.getTeamTurn().toString(), ctx.session, gameId);
                        String msg = "message: " + chessGame.getTeamTurn().toString() + " is in checkmate";
                        ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, msg, null)));
                    }
                    else if(chessGame.isInCheck(chessGame.getTeamTurn())){
                        isInCheckMessage(chessGame.getTeamTurn().toString(), ctx.session, gameId);
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
        if(!checkAuth(userGameCommand.getAuthToken())){
            ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    null, null, "message: You are not authorized. Leave game and rejoin. ")));
            return false;
        }
        if(!checkTurn(userGameCommand.getMove(), userGameCommand.getGameID(), userGameCommand.getAuthToken())){
            ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    null, null, "message: Not your turn or piece in given position doesn't exist. Please try a different move.")));
            return false;
        }
        if(!checkMove(userGameCommand.getMove(), userGameCommand.getGameID(), ctx)){
            ctx.send(new Gson().toJson(new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    null, null, "message: Not your turn or piece in given position doesn't exist. Please try a different move.")));
            return false;
        }
        return true;
    }

    private boolean checkLogin(UserGameCommand userGameCommand){
        if(userGameCommand.getPlay()){
            try {
                MySqlDataAccess mySqlDataAccess = new MySqlDataAccess();
                if(userGameCommand.getColor() != null
                        && userGameCommand.getColor().equals("WHITE")){
                    mySqlDataAccess.updateWhite(userGameCommand.getGameID(), mySqlDataAccess.getAuth(userGameCommand.getAuthToken()).getUsername());
                } else{
                    mySqlDataAccess.updateBlack(userGameCommand.getGameID(), mySqlDataAccess.getAuth(userGameCommand.getAuthToken()).getUsername());
                }
            } catch (ResponseException | DataAccessException e){
                System.out.println(e.toString());
            }
        }
        return checkAuth(userGameCommand.getAuthToken())
                && checkGameId(userGameCommand.getGameID());
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
