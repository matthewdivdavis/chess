package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import serviceSQL.ClearService;

import java.util.Map;

public class ClearHandler implements Handler {
    private final ClearService userService;

    public ClearHandler(ClearService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx) {
        try{
            userService.clear();
            ctx.json("");
        } catch (DataAccessException e) {
            Gson gson = new Gson();
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            ctx.contentType("application/json");
        }
    }
}
