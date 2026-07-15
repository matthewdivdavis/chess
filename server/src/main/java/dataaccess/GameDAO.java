package dataaccess;

import model.GameData;

public class GameDAO {
    public static GameData createGame(int ID){
        return new GameData(ID);
    }
}
