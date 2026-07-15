package dataaccess;

import model.GameData;

public class GameDAO {
    public GameData createGame(int ID){
        return new GameData(ID);
    }
}
