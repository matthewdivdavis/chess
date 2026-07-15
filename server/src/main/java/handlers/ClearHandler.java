package handlers;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;

public class ClearHandler implements Handler {
    private final UserService userService;

    public ClearHandler(UserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        userService.clear();
        ctx.json("");
    }
}
