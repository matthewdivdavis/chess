package client;

import chess.*;
import com.google.gson.Gson;
import request.HighlightRequest;
import request.LoginRequest;
import request.MoveRequest;
import request.RegisterRequest;
import response.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class ClientMain {

    public static void main(String[] args){
        String serverUrl = "http://localhost:8080";
//        if (args.length == 1) {
//            serverUrl = args[0];
//        }

        try {
            new ChessClient(serverUrl).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}