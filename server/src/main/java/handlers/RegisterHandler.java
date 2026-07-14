package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.*;
import service.*;

public class RegisterHandler implements Handler {
    @Override
    public void handle(Context ctx){
        System.out.println(ctx.body());
        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);

        try {
            RegisterResult result = UserService.register(registerRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
