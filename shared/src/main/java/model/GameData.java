package model;

import chess.ChessGame;
import com.google.gson.Gson;

public class GameData {
    private final int gameId;
    private String blackUsername;
    private String whiteUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int id) {
        this.gameId = id;
        game = new ChessGame();
    }

    public void setLoser(String username){
        if (blackUsername != null && blackUsername.equals(username)) {
            game.setWinner(ChessGame.TeamColor.WHITE);
        }
        else{
            game.setWinner(ChessGame.TeamColor.BLACK);
        }
    }

    public void setBlackUsername(String username){
        blackUsername = username;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGame(String json){
        this.game = new Gson().fromJson(json, ChessGame.class);
    }

    public int getGameID(){
        return gameId;
    }
    public String getBlackUsername(){
        return blackUsername;
    }
    public String getWhiteUsername(){
        return whiteUsername;
    }
    public String getGameName(){
        return gameName;
    }
    public ChessGame getGame(){
        return game;
    }

    @Override
    public String toString() {
        return "gameID:" + gameId +
                ", blackUsername:'" + blackUsername + '\'' +
                ", whiteUsername:'" + whiteUsername + '\'' +
                ", gameName:'" + gameName + '\'';
    }
}
