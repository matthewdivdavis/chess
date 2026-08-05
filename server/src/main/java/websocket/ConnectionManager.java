package websocket;

import com.google.gson.Gson;
import exception.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(Session session, Integer gameId){
        List<Session> sessionList = connections.get(gameId);
        if(sessionList == null){
            sessionList = new ArrayList<>();
        }
        sessionList.add(session);
        connections.put(gameId, sessionList);
    }

    public boolean contains(Session session, int gameId){
        if(connections.get(gameId) == null){
            return false;
        }
        for(Session s : connections.get(gameId)){
            if(s.equals(session)){
                return true;
            }
        }
        return false;
    }

    public void remove(Session session, int gameId){
        List<Session> sessionList = connections.get(gameId);
        if(sessionList == null){
            return;
        }
        sessionList.remove(session);
        connections.put(gameId, sessionList);
    }

    public void broadcast(Session excludeSession, ServerMessage message, int gameId) throws IOException, DataAccessException{
        System.out.println(new Gson().toJson(message));
        String msg = new Gson().toJson(message);
        List<Session> sessionList = connections.get(gameId);
        if(sessionList == null){
            throw new DataAccessException("Session List Empty");
        }
        for(Session c : sessionList){
            if(c.isOpen()){
                if(!c.equals(excludeSession)){
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
