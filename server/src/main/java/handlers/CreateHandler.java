package handlers;

import com.google.gson.Gson;
import dataaccess.CreateRequest;
import dataaccess.DataAccessException;
import dataaccess.MissingDataException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.GameRequest;
import service.*;

import java.util.Map;

public class CreateHandler implements Handler {
    private final UserService userService;

    public CreateHandler(UserService userService){
        this.userService = userService;
    }


    @Override
    public void handle(Context ctx){
        String authTok = ctx.header("authorization");

        Gson gson = new Gson();
        GameRequest reqname = gson.fromJson(ctx.body(), GameRequest.class);
        CreateRequest request = new CreateRequest(authTok, reqname.gameName());

        try{
            CreateResult result = userService.create(request);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        } catch (MissingDataException e){
            ctx.status(400);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
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

