package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameList {
    private final List<GameData> games = new ArrayList<>();

    public void addGame(GameData game){
        games.add(game);
    }

    public GameData getGame(int gameID){
        for(GameData g : games){
            if(g.getGameID() == gameID){
                return g;
            }
        }
        return null;
    }
    public int size(){
        return games.size();
    }
    public GameData at(int i){
        return games.get(i);
    }
    public void clear(){
        games.clear();
    }
}
