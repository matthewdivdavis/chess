package handlers;

import com.google.gson.Gson;
import dataaccess.CreateRequest;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.ListRequest;
import service.*;

import java.util.ArrayList;
import java.util.Map;

public class ListHandler implements Handler {
    private final UserService userService;

    public ListHandler(UserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        String authTok = ctx.header("authorization");
        ListRequest request = new ListRequest(authTok);
        System.out.println("Request: "+request);
        Gson gson = new Gson();
        try{
            ArrayList<GameResult> games = userService.list(request);
            ListGamesResult result = new ListGamesResult(games);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        } catch (DataAccessException e){
            ctx.status(401);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
            ctx.contentType("application/json");
        }
    }
}
