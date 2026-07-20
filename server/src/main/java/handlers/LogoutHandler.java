package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.LogoutRequest;
import service.LogoutResult;
import service.LogoutService;
import service.UserService;

import java.util.Map;

public class LogoutHandler implements Handler{
    private final LogoutService userService;

    public LogoutHandler(LogoutService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        String authTok = ctx.header("authorization");
        Gson gson = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(authTok);
        try{
            LogoutResult result = userService.logout(logoutRequest);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        }catch (DataAccessException e){
            ctx.status(401);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
            ctx.contentType("application/json");
        }

    }
}

