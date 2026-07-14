package model;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private String blackUsername;
    private String whiteUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int gameID) {
        // I might want to randomly generate the gameID. I'm not sure.
        this.gameID = gameID;
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

}
