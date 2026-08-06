package response;

//import dataaccess.MemoryGameDAO;
import chess.ChessGame;
import model.GameData;

public class GameResult {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    GameData gameData;
    public GameResult(GameData game){
        gameID = game.getGameID();
        whiteUsername = game.getWhiteUsername();
        blackUsername = game.getBlackUsername();
        gameName = game.getGameName();
        gameData = game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public GameData getGameData(){
        return gameData;
    }

    public String getBlackUsername(){
        return blackUsername;
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
