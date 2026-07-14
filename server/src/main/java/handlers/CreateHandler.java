package handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CreateHandler implements Handler {
    @Override
    public void handle(Context ctx){
        System.out.println(ctx.body());
    }
}

