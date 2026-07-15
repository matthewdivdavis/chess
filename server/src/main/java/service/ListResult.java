package service;

import dataaccess.MemoryGameDAO;
import model.GameData;

public class GameResult {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    public GameResult(GameData game){
        gameID = game.getGameID();
        whiteUsername = game.getWhiteUsername();
        blackUsername = game.getBlackUsername();
        gameName = game.getGameName();
    }

    @Override
    public String toString() {
        return "ListResult{" +
                "gameID=" + gameID +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }
}
