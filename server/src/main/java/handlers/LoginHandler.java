package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.*;
import service.*;

import java.util.Map;

public class LoginHandler implements Handler{
    UserService userService;
    public LoginHandler(UserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(ctx.body(), LoginRequest.class);
        try{
            LoginResult result = userService.login(loginRequest);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        } catch (DataAccessException e) {
            ctx.status(403);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
            ctx.contentType("application/json");
        }
    }
}
