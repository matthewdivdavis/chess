package model;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private String blackUsername;
    private String whiteUsername;
    private String gameName;
    private final ChessGame game;

    public GameData(int ID) {
        this.gameID = ID;
        game = new ChessGame();
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

    public int getGameID(){
        return gameID;
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
        return "gameID:" + gameID +
                ", blackUsername:'" + blackUsername + '\'' +
                ", whiteUsername:'" + whiteUsername + '\'' +
                ", gameName:'" + gameName + '\'';
    }
}
