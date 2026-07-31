package serverpractice;

import io.javalin.Javalin;

public class SimpleWsEchoServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/echo/{msg}", ctx ->
                        ctx.result("Http Response: " + ctx.pathParam("msg")))
                .ws("/ws", ws -> {
                    ws.onConnect(ctx -> {
                        ctx.enableAutomaticPings();
                        System.out.println("WebSocket connected");
                    });

                    ws.onMessage(ctx -> {
                        System.out.println("Received: " + ctx.message());
                        ctx.send("WebSocket response: " + ctx.message());
                    });

                    ws.onClose(ctx ->
                            System.out.println("WebSocket closed"));
                })
                .start(8080);
    }
}
