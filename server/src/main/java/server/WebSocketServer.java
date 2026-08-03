package server;

import io.javalin.Javalin;

public class WebSocketServer {
    public static void main(String[] args){
        Javalin javalinServer = Javalin.create();
        createHandlers(javalinServer);
        javalinServer.start(8080);
    }

    private static void createHandlers(Javalin javalinServer){

    }
}
