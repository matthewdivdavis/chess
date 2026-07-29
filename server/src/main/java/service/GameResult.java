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

    public int getGameID(){
        return gameID;
    }

    @Override
    public String toString() {
        return gameName + ":\n" +
                "\tgameID = " + gameID +
                "\n\twhiteUsername='" + whiteUsername + '\'' +
                "\n\tblackUsername='" + blackUsername + '\'';
    }
}
