package client;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.DataAccessException;
import exception.ResponseException;
import model.GameData;
import request.HighlightRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {
    public static String userName = null;
    public static int gameId;
    private static ServerFacade server;
    private static WebSocketFacade ws;
    public static String playerColor;
    public static boolean resign = false;
    public static boolean observer = false;

    public ChessClient(String serverUrl) throws ResponseException{
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, this);
    }

    public void notify(ServerMessage message) {
        if(message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            System.out.println("\n" + RED + message.message);
            System.out.printf("%s[GAME PLAY] >>> ", LIGHT_GREY);
        }
        else if(message.errorMessage != null){
            System.out.println("\n" + RED + message.errorMessage);
        }
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            if(message.message != null){
                System.out.println("\n" + RED + message.message);
                System.out.printf("%s[GAME PLAY] >>> ", LIGHT_GREY);
            }
            else{
                try{
                    DrawChess.drawBoard(server.getGame(gameId), playerColor.equals("BLACK"));

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }
    public static void run() throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        boolean quit = false;
        while(!quit){
            if(preLogin(out) == 1){
                quit = true;
            }
            else if(postLogin(out) == 1){
                quit = true;
            }
        }
    }

    private static int postLogin(PrintStream out) throws Exception {
        Scanner myScan = new Scanner(System.in);
        while(true){
            out.printf(LIGHT_GREY);
            out.print("[LOGGED IN] >>> ");
            String input = myScan.next().trim();
            if(input.equals("help")){
                helpLoggedIn(out);
            }
            else if(input.equals("create")){
                create(out);
            }
            else if(input.equals("list")){
                list(out, 0);
            }
            else if(input.equals("observe")){
                observe(out);
            }
            else if(input.equals("join")){
                join(out);
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
            out.printf(LIGHT_GREY);
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
        out.printf("%s\tcreate <NAME> %s- a game\n%s\tlist %s- games\n%s\tjoin <ID> [WHITE or BLACK] %s- a game\n%s\tobserve %s- a game\n%s\t" +
            "logout %s- when you are done\n%s\tquit %s- playing chess\n%s\tquit %s- playing chess\n%s\thelp %s- with possible commands\n%s",
            BLUE,MAGENTA,BLUE,MAGENTA,BLUE,MAGENTA,BLUE,MAGENTA,BLUE,MAGENTA,BLUE, MAGENTA,BLUE, MAGENTA,BLUE, MAGENTA, LIGHT_GREY);
    }
    private static void helpLoggedOut(PrintStream out){
        out.printf("%s\tregister <USERNAME> <PASSWORD> <EMAIL> %s- to create an account\n%s\tlogin <USERNAME> <PASSWORD> %s- to play chess\n%s\t" +
                "quit %s- playing chess\n%s\thelp %s- with possible commands\n%s", BLUE, MAGENTA,BLUE,
                MAGENTA, BLUE, MAGENTA,BLUE,MAGENTA,LIGHT_GREY);
    }
    private static void helpGamePlay(PrintStream out){
        out.printf("%s\tmove <COLUMN ROW> %s- to make a chess move\n", BLUE, MAGENTA);
        out.printf("%s\thighlight %s- highlight all legal moves\n", BLUE, MAGENTA);
        out.printf("%s\tleave %s- leave game\n", BLUE, MAGENTA);
        out.printf("%s\tresign %s- admit defeat and give the win to your opponent (rip)\n", BLUE, MAGENTA);
        out.printf("%s\tredraw %s- redraw the chessboard\n", BLUE, MAGENTA);
        out.printf(LIGHT_GREY);
    }
    private static boolean login(PrintStream out) throws Exception {
        out.printf("%sInput your username and password (%s%susername password%s%s): ", BLUE, MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();

        LoginRequest request = new LoginRequest(username, password);

        Gson gson = new Gson();
        HttpResponse<String> response = server.login(request);
        if(response.statusCode() == 200){
            LoginResult result = gson.fromJson(response.body(), LoginResult.class);
            System.out.println("Logged in as " + username);
            userName = username;
            return true;
        }
        else{
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return false;
    }
    private static boolean register(PrintStream out) throws Exception {
        out.printf("%sInput your username, password and email (%s%susername password email%s%s): ",
                BLUE, MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();
        String email = myScan.next();
        // for sending the data
        Gson gson = new Gson();
        RegisterRequest request = new RegisterRequest(username, password, email);
        HttpResponse<String> response = server.register(request);
        if(response.statusCode() == 200){
            LoginResult result = gson.fromJson(response.body(), LoginResult.class);
            System.out.println("Logged in as " + username);
            return true;
        }
        else{
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return false;
    }
    private static boolean create(PrintStream out) throws Exception {
        out.printf("%sInput a game name: ", BLUE);
        Scanner myScan = new Scanner(System.in);
        String gameName = myScan.next();
        // for sending the data
        Gson gson = new Gson();
        HttpResponse<String> response = server.create(gameName);
        if(response.statusCode() == 200){
            CreateResult result = gson.fromJson(response.body(), CreateResult.class);
            System.out.println("Game created as '" + gameName + "' with ID: " + result.gameID());
            return true;
        }
        else{
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return false;
    }
    private static GameResult list(PrintStream out, int gameId) throws IOException, InterruptedException {
        // for sending the data
        Gson gson = new Gson();
        HttpResponse<String> response = server.list();
        if(response.statusCode() == 200){
            ListGamesResult result = gson.fromJson(response.body(), ListGamesResult.class);
            if(gameId > 0){
                for(GameResult game : result.games()){
                    if(game.getGameID() == gameId){
                        return game;
                    }
                }
                return null;
            }
            if(result.games().size() < 1){
                out.printf("%sNo games. Please create a game and try again.\n", MAGENTA);
            }
            for(var game : result.games()){
                out.printf("%s%s\n", BLUE, game.toString());
            }
        }
        else{
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return null;
    }
    public static boolean observe(PrintStream out) throws IOException, InterruptedException {
        out.printf("%sInput gameID: ", BLUE);
        Scanner myScan = new Scanner(System.in);
        int gameId = myScan.nextInt();
        GameResult game = getGame(out, gameId);
        if(game == null){
            out.printf("%sCould not find game %d. Please Try again.\n", MAGENTA, gameId);
            return false;
        }
        out.printf("%s%s\n", BLUE, game.toString());
        if(!ws.observe(gameId, server.getAuthorization())){
            out.printf("%sConnection error. Please try again\n", MAGENTA);
            return false;
        }
        DrawChess.drawBoard(server.getGame(gameId), false);
        try {
            observer = true;
            gamePlay(out);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return true;
    }
    private static void join(PrintStream out) throws Exception {
        out.printf("%sInput gameID, and color (%s%sgameID color%s%s): ",
                BLUE, MAGENTA, SET_TEXT_ITALIC, BLUE, RESET_TEXT_ITALIC);
        Scanner myScan = new Scanner(System.in);
        int gameId;
        while(true){
            try {
                gameId = myScan.nextInt();
                if(gameId < 1) out.printf("%sInvalid ID input. Please try again: ", BLUE);
                break;
            } catch (Exception e) {
                out.printf("%sInvalid ID input. Please try again: ", BLUE);
                myScan.next();
            }
        }
        String color = myScan.next();
        while(true){
            if(!color.equals("BLACK") && !color.equals("WHITE")){
                out.printf("%sInvalid color input. Please choose color again (%s%sBLACK or WHITE%s%s): ",
                        BLUE, MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, BLUE);
                color = myScan.next();
            }
            else{
                break;
            }
        }
        try {
            GameResult game = server.join(gameId, color);
            ChessClient.gameId = gameId;
            if((color.equals("BLACK") && game.getBlackUsername() != null) || (color.equals("WHITE") && game.getWhiteUsername() != null)){
                out.printf("%sTeam '%s' is taken. Please try a different color, or different game.\n", BLUE, color);
                return;
            }
            if(color.equals("BLACK")){
                playerColor = "BLACK";
            }
            else{
                playerColor = "WHITE";
            }
            ws.join(gameId, server.getAuthorization(), playerColor);
        } catch (DataAccessException | IOException | InterruptedException | ResponseException e){
            out.println(e.toString());
        }
        gamePlay(out);
    }
    private static void gamePlay(PrintStream out) throws Exception{
        out.printf("%sType 'help' to list commands.\n", LIGHT_GREY);
        while(true){
            out.printf("%s[GAME PLAY] >>> ", LIGHT_GREY);
            Scanner myScan = new Scanner(System.in);
            String userIn = myScan.next();
            if(userIn.equals("help")){
                helpGamePlay(out);
            }
            else if(userIn.equals("resign")){
                if(observer){
                    out.printf("%sObserver cannot resign. If you'd like to leave type 'leave'.", MAGENTA);

                }
                else{
                    out.printf("%sAre you sure you wish to resign? (%s%sy or n%s%s): %s",
                            BLUE, MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC,
                            BLUE, LIGHT_GREY);
                    if(myScan.next().equals("y")){
                        ws.resign(userName, gameId);
                        out.printf("%sResigning...\n", RED);
                        resign = true;
                    }
                }
            }
            else if(userIn.equals("leave")){
                ws.leave(userName, gameId);
                return;
            }
            else if(userIn.equals("move")){
                if(resign){
                    out.printf("%sYou cannot make new moves after resigning.", BLUE);
                }
                else if(observer){
                    out.printf("%sObserver cannot make moves.", MAGENTA);
                }
                else{
                    makeMove(out);
                }
            }
            else if(userIn.equals("highlight")){
                highlightMoves(out);
            }
            else if(userIn.equals("redraw")){
                DrawChess.drawBoard(server.getGame(gameId), playerColor.equals("BLACK"));
            }
        }
    }
    private static List<Character> validateMoveHighlight(PrintStream out){
        List<Character> result = new ArrayList<>();
        Scanner myScan = new Scanner(System.in);
        char col;
        char fill;
        col = myScan.next().charAt(0);
        fill = myScan.next().charAt(0);
        while(true){
            if((col > 64 && col < 73)
                    || (col > 96 && col < 105)){
                col = toLower(col);
                result.add(col);
                break;
            } else {
                out.printf("%sInvalid input column. Input a single letter between a-h: ",
                        BLUE);
                col = myScan.next().charAt(0);
            }
        }
        while (true) {
            if(fill > 48 && fill < 57) {
                result.add(fill);
                break;
            }
            else{
                out.printf("%sInvalid row input. Input a single number between 1 and 8: ",
                        BLUE);
                fill = myScan.next().charAt(0);
            }
        }
        return result;
    }
    private static void highlightMoves(PrintStream out) throws Exception{
        out.printf("%sInput a piece by position (%s%sCOLUMN ROW%s%s) to see it's possible moves: ",
                BLUE, MAGENTA,
                SET_TEXT_ITALIC, BLUE,
                RESET_TEXT_ITALIC);
        List<Character> result = validateMoveHighlight(out);
        int col = result.get(0) - 'a' + 1;
        int row = result.get(1) - '0';
        DrawChess.highlight(server.getGame(gameId),col, row, playerColor.equals("BLACK"));
    }
    private static List<Character> validateMoveMakeMove(PrintStream out){
        List<Character> result = new ArrayList<>();
        Scanner myScan = new Scanner(System.in);
        char oldCol;
        char oldRow;
        char newCol;
        char newRow;
        oldCol = myScan.next().charAt(0);
        oldRow = myScan.next().charAt(0);
        newCol = myScan.next().charAt(0);
        newRow = myScan.next().charAt(0);
        while(true){
            if((oldCol > 64 && oldCol < 73)
                    || (oldCol > 96 && oldCol < 105)){
                oldCol = toLower(oldCol);
                result.add(oldCol);
                break;
            } else {
                out.printf("%sInvalid first input column. Input a single letter between a-h: ",
                        BLUE);
                oldCol = myScan.next().charAt(0);
            }
        }
        while (true) {
            if(oldRow > 48 && oldRow < 57) {
                result.add(oldRow);
                break;
            }
            else{
                out.printf("%sInvalid first row input. Input a single number between 1 and 8: ",
                        BLUE);
                oldRow = myScan.next().charAt(0);
            }
        }
        while(true){
            if((newCol > 64 && newCol < 73)
                    || (newCol > 96 && newCol < 105)){
                newCol = toLower(newCol);
                result.add(newCol);
                break;
            } else {
                out.printf("%sInvalid second input column. Input a single letter between a-h: ",
                        BLUE);
                newCol = myScan.next().charAt(0);
            }
        }
        while (true) {
            if(newRow > 48 && newRow < 57) {
                result.add(newRow);
                break;
            }
            else{
                out.printf("%sInvalid second row input. Input a single number between 1 and 8: ",
                        BLUE);
                newRow = myScan.next().charAt(0);
            }
        }
        return result;
    }
    private static void makeMove(PrintStream out) throws Exception{
        out.printf("%sInput piece's current position and the new desired position \n(%s%sCOLUMN ROW COLUMN ROW%s%s) : ",
                BLUE, MAGENTA,SET_TEXT_ITALIC, BLUE, RESET_TEXT_ITALIC);
        List<Character> result = validateMoveMakeMove(out);
        int oldCol = result.get(0) - 'a' + 1;
        int oldRow = result.get(1) - '0';
        int newCol = result.get(2) - 'a' + 1;
        int newRow = result.get(3) - '0';
        ws.move(oldCol, oldRow, newCol, newRow, server.getGame(gameId).getGame().getBoard());
    }
    private static boolean logout(PrintStream out) throws Exception {
        HttpResponse<String> response = server.logout();
        if(response.statusCode() == 200){
            return true;
        }
        else{
            Gson gson = new Gson();
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return false;
    }
    private static GameResult getGame(PrintStream out, int gameId) throws IOException, InterruptedException {
        if(gameId < 1){
            return null;
        }
        return list(out, gameId);
    }
    private static void clear() throws Exception{
        server.clear();
    }
    private static char toLower(char let){
        if(let < 97){
            return (char) (let + 32);
        }
        return let;
    }
}
