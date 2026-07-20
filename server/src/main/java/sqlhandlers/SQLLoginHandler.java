package sqlhandlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MissingDataException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.LoginRequest;
import service.LoginResult;
import service.SQLUserService;
import service.UserService;

import java.util.Map;

public class SQLLoginHandler implements Handler{
    private final SQLUserService userService;
    public SQLLoginHandler(SQLUserService userService){
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
        } catch (MissingDataException e){
            ctx.status(400);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
            ctx.contentType("application/json");
        } catch (DataAccessException e) {
            ctx.status(401);
            ctx.result(gson.toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
            ctx.contentType("application/json");
        }
    }
}
