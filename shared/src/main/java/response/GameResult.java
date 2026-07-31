package response;

//import dataaccess.MemoryGameDAO;
import chess.ChessGame;
import model.GameData;

public class GameResult {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
//    ChessGame game;
    public GameResult(GameData game){
        gameID = game.getGameID();
        whiteUsername = game.getWhiteUsername();
        blackUsername = game.getBlackUsername();
        gameName = game.getGameName();
//        this.game = game.getGame();
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
