package sqlhandlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.SQLUserService;
import service.UserService;

public class SQLClearHandler implements Handler {
    private final SQLUserService userService;

    public SQLClearHandler(SQLUserService userService){
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx){
        userService.clear();
        ctx.json("");
    }
}
