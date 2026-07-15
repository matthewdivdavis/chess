package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MissingDataException;
import dataaccess.NameTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.server.Authentication;
import server.GameRequest;
import server.JoinRequest;
import service.CreateResult;
import service.JoinResult;
import service.UserService;

import java.util.Map;

public class JoinHandler implements Handler {
    private final UserService userService;

    public JoinHandler(UserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        String authTok = ctx.header("authorization");
        Gson gson = new Gson();
        JoinRequest request = gson.fromJson(ctx.body(), JoinRequest.class);
        try{
            JoinResult result = userService.join(authTok, request);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        } catch (NameTakenException e){
            ctx.status(403);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
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

