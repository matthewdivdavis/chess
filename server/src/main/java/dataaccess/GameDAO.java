package dataaccess;

import model.GameData;

public class GameDAO {
    public static GameData createGame(int iD){
        return new GameData(iD);
    }
}
