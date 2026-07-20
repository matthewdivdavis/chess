package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MissingDataException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.*;
import service.*;
import servicesql.RegisterService;

import java.util.Map;

public class RegisterHandler implements Handler {
    private final RegisterService userService;
    public RegisterHandler(RegisterService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);
        try{
            RegisterResult result = userService.register(registerRequest);
            ctx.result(gson.toJson(result));
            ctx.contentType("application/json");
        } catch (MissingDataException e){
            ctx.status(400);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
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
