package handlers;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.*;

public class ClearHandler implements Handler {
    private final ClearService userService;

    public ClearHandler(ClearService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx) {
        userService.clear();
        ctx.json("");
    }
}
