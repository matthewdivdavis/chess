package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

    public void add(Session session){
        connections.put(session, session);
    }

    public void remove(Session session){
        connections.remove(session);
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException{
        System.out.println(new Gson().toJson(message));
        String msg = new Gson().toJson(message);
        for(Session c : connections.values()){
            if(c.isOpen() && c.equals(session)){
                c.getRemote().sendString(msg);
            }
        }
    }

    public void broadcast(Session excludeSession, ServerMessage message) throws IOException{
        System.out.println(new Gson().toJson(message));
        String msg = new Gson().toJson(message);
        for(Session c : connections.values()){
            if(c.isOpen()){
                if(!c.equals(excludeSession)){
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
