package handlers;

import com.google.gson.Gson;
import exception.DataAccessException;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import request.JoinRequest;
import request.ListRequest;
import response.GameResult;
import service.SQLUserService;
import servicesql.GetGameService;

import java.util.Map;

public class GetGameHandler implements Handler {
    private final GetGameService userService;

    public GetGameHandler(SQLUserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        int gameId = Integer.parseInt(ctx.pathParam("gameId"));
        String authTok = ctx.header("authorization");
        ListRequest listRequest = new ListRequest(authTok);

        try{
            GameResult result = userService.getGame(listRequest, gameId);
            ctx.result(new Gson().toJson(result));ctx.contentType("application/json");
        } catch (DataAccessException e){
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
            ctx.contentType("application/json");
        } catch(ResponseException e){
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
            ctx.contentType("application/json");
        }
    }
}
