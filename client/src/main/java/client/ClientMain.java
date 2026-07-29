package client;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import server.LoginRequest;
import server.RegisterRequest;
import server.Server;
import service.LoginResult;
import service.RegisterResult;
import service.SQLUserService;

import java.io.PrintStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static dataaccess.DatabaseManager.createDatabase;
import static ui.EscapeSequences.*;

public class ClientMain {
    public static Server server;
    public static void main(String[] args) throws Exception {
        server = new Server();
        var port = server.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if(preLogin(out) == 1){
            server.stop();
            return;
        }
        if(postLogin(out) == 1){
            server.stop();
            return;
        }
    }

    private static int postLogin(PrintStream out){
        Scanner myScan = new Scanner(System.in);
        while(true){
            out.printf(SET_TEXT_COLOR_LIGHT_GREY);
            out.print("[LOGGED IN] >>> ");
            String input = myScan.next().trim();
            if(input.equals("help")){
                helpLoggedIn(out);
            }
            else if(input.equals("create")){
                create(out);
            }
            else if(input.equals("list")){
                list(out);
            }
            else if(input.equals("observe")){
                observe(out);
            }
            else if(input.equals("logout")){
                logout(out);
                break;
            }
            else if(input.equals("quit")){
                return 1;
            }
        }
        return 0;
    }
    private static int preLogin(PrintStream out) throws Exception {
        Scanner myScan = new Scanner(System.in);
        out.println("Welcome to 240 Chess. Type 'help' to get started. ");
        boolean loggedIn = false;
        while(!loggedIn){
            out.printf(SET_TEXT_COLOR_LIGHT_GREY);
            out.print("[LOGGED OUT] >>> ");
            String input = myScan.next().trim();
            if(input.equals("help")){
                helpLoggedOut(out);
            }
            else if(input.equals("login")) {
                loggedIn = login(out);
            } else if(input.equals("register")){
                loggedIn = register(out);
            } else if(input.equals("quit")){
                return 1;
            }
            else if(input.equals("clear")){
                clear();
            }
        }
        return 0;
    }
    private static void helpLoggedIn(PrintStream out){
        out.printf("%s\tcreate <NAME> %s- a game\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tlist %s- games\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tjoin <ID> [WHITE or BLACK] %s- a game\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tobserve %s- a game\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tlogout %s- when you are done\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tquit %s- playing chess\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\thelp %s- with possible commands\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf(SET_TEXT_COLOR_LIGHT_GREY);

    }
    private static void helpLoggedOut(PrintStream out){
        out.printf("%s\tregister <USERNAME> <PASSWORD> <EMAIL> %s- to create an account\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tlogin <USERNAME> <PASSWORD> %s- to play chess\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tquit %s- playing chess\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\thelp %s- with possible commands\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static boolean login(PrintStream out) throws Exception {
        out.printf("%sInput your username and password (%s%susername password%s%s): ",SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();

        LoginRequest request = new LoginRequest(username, password);

        Gson gson = new Gson();
        HttpResponse<String> response = ServerFacade.login(request);
        if(response.statusCode() == 200){
            LoginResult result = gson.fromJson(response.body(), LoginResult.class);
            System.out.println(result.authToken());
        }
        System.out.println("Logged in as " + username);
        return true;
    }
    private static boolean register(PrintStream out) throws Exception {
        out.printf("%sInput your username, password and email (%s%susername password email%s%s): ",SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();
        String email = myScan.next();
        // for sending the data
        Gson gson = new Gson();
        RegisterRequest request = new RegisterRequest(username, password, email);
        HttpResponse<String> response = ServerFacade.register(request);

        if(response.statusCode() > 300 || response.statusCode() < 200){
            return false;
        }
        System.out.println("Logged in as " + username);
        return true;
    }

    private static boolean create(PrintStream out){
        out.printf("%sInput a game name: ", SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String gameName = myScan.next();

        ServerFacade.create(gameName);
        out.println("Created game '" + gameName + "'");
        return true;
    }

    private static boolean list(PrintStream out){
        ServerFacade.list();
        out.println("All the games: ");
        return true;
    }

    private static boolean observe(PrintStream out){
        ServerFacade.observe();
        printGame(out);
        out.println("Observing this game: hurdurdur");
        return true;
    }

    private static boolean logout(PrintStream out){
        out.println("Logging you out...");
        ServerFacade.logout();
        return true;
    }

    private static void clear() throws Exception{
        ServerFacade.clear();
        return;
    }

    private static void printGame(PrintStream out){
        char[] lets = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        out.printf("%s%s   ", SET_BG_COLOR_WHITE, SET_TEXT_COLOR_BLACK);
        for(char c : lets){
            out.printf(" %s%s ",SET_TEXT_BOLD, c);
        }
        out.printf("   %s\n", RESET_BG_COLOR);
        int d = 1;
        for(int j = 0; j < 4; j++){
            out.printf("%s %d ", SET_BG_COLOR_WHITE, d);
            for(int i = 0; i < 4; i++){
                out.printf("%s   ", SET_BG_COLOR_LIGHT_GREY);
                out.printf("%s   ", SET_BG_COLOR_DARK_GREY);
            }
            out.printf("%s %d %s\n",SET_BG_COLOR_WHITE, d++, RESET_BG_COLOR);
            out.printf("%s %d ", SET_BG_COLOR_WHITE, d);
            for(int i = 0; i < 4; i++){
                out.printf("%s   ", SET_BG_COLOR_DARK_GREY);
                out.printf("%s   ", SET_BG_COLOR_LIGHT_GREY);
            }
            out.printf("%s %d %s\n",SET_BG_COLOR_WHITE, d++, RESET_BG_COLOR);
        }
        out.printf("%s%s   ", SET_BG_COLOR_WHITE, SET_TEXT_COLOR_BLACK);
        for(char c : lets){
            out.printf(" %s%s ",SET_TEXT_BOLD, c);
        }
        out.printf("   %s%s%s\n", RESET_TEXT_COLOR, RESET_TEXT_BOLD_FAINT, RESET_BG_COLOR);
    }

}
