package client;

import chess.*;
import com.google.gson.Gson;
import request.LoginRequest;
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

    public static String playerColor;

    public ClientMain(){
    }
    public static void main(String[] args) throws Exception {
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
    private static void helpGamePlay(PrintStream out){
        out.printf("%s\tmove <COLUMN ROW> %s- to make a chess move\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\thighlight %s- highlight all legal moves\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tleave %s- leave game\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tresign %s- admit defeat and give the win to your opponent (rip)\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tredraw %s- redraw the chessboard\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static boolean login(PrintStream out) throws Exception {
        out.printf("%sInput your username and password (%s%susername password%s%s): ",
                SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_ITALIC,
                RESET_TEXT_ITALIC,
                SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();

        LoginRequest request = new LoginRequest(username, password);

        Gson gson = new Gson();
        HttpResponse<String> response = ServerFacade.login(request);
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
    private static boolean register(PrintStream out) throws Exception {
        out.printf("%sInput your username, password and email (%s%susername password email%s%s): ",
                SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_ITALIC,
                RESET_TEXT_ITALIC,
                SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();
        String email = myScan.next();

        // for sending the data
        Gson gson = new Gson();
        RegisterRequest request = new RegisterRequest(username, password, email);
        HttpResponse<String> response = ServerFacade.register(request);

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
        out.printf("%sInput a game name: ", SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String gameName = myScan.next();

        // for sending the data
        Gson gson = new Gson();
        HttpResponse<String> response = ServerFacade.create(gameName);

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
        HttpResponse<String> response = ServerFacade.list();

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
                out.printf("%sNo games. Please create a game and try again.\n", SET_TEXT_COLOR_MAGENTA);
            }
            for(var game : result.games()){
                out.printf("%s%s\n", SET_TEXT_COLOR_BLUE, game.toString());
            }
        }
        else{
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
        return null;
    }

    public static boolean observe(PrintStream out) throws IOException, InterruptedException {
        out.printf("%sInput gameID: ", SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        int gameId = myScan.nextInt();
        GameResult game = getGame(out, gameId);
        if(game == null){
            out.printf("%sCould not find game %d. Please Try again.\n", SET_TEXT_COLOR_MAGENTA, gameId);
            return false;
        }
        out.printf("%s%s\n", SET_TEXT_COLOR_BLUE, game.toString());
        printGameWhite(out);
        return true;
    }

    private static void join(PrintStream out) throws Exception {
        out.printf("%sInput gameID, and color (%s%sgameID color%s%s): ",
                SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_ITALIC,
                SET_TEXT_COLOR_BLUE,
                RESET_TEXT_ITALIC);
        Scanner myScan = new Scanner(System.in);
        int gameId;
        while(true){
            try {
                gameId = myScan.nextInt();
                if(gameId < 1){
                    out.printf("%sInvalid ID input. Please try again: ", SET_TEXT_COLOR_BLUE);
                }
                break;
            } catch (Exception e) {
                out.printf("%sInvalid ID input. Please try again: ", SET_TEXT_COLOR_BLUE);
                myScan.next();
            }
        }
        String color = myScan.next();
        while(true){
            if(!color.equals("BLACK") && !color.equals("WHITE")){
                out.printf("%sInvalid color input. Please choose color again (%s%sBLACK or WHITE%s%s): ",
                        SET_TEXT_COLOR_BLUE,
                        SET_TEXT_COLOR_MAGENTA,
                        SET_TEXT_ITALIC,
                        RESET_TEXT_ITALIC,
                        SET_TEXT_COLOR_BLUE);
                color = myScan.next();
            }
            else{
                break;
            }
        }
        ServerFacade.join(gameId, color);
        HttpResponse<String> response = ServerFacade.list();
        if(response.statusCode() == 200){
            GameResult game = null;
            Gson gson = new Gson();
            ListGamesResult result = gson.fromJson(response.body(), ListGamesResult.class);
            for(GameResult g : result.games()){
                if(g.getGameID() == gameId){
                    game = g;
                }
            }
            if(game == null){
                out.printf("%sCould not find game %d. Please Try again.\n", SET_TEXT_COLOR_MAGENTA, gameId);
                return;
            }
            out.printf("%s%s\n", SET_TEXT_COLOR_BLUE, game.toString());
            if(color.equals("BLACK")){
                playerColor = "BLACK";
                printGameBlack(out);
            }
            else{
                playerColor = "WHITE";
                printGameWhite(out);
            }
            gamePlay(out);
        }
        else{
            Gson gson = new Gson();
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            System.out.println(result.message());
        }
    }

    private static void gamePlay(PrintStream out){
        out.printf("%sType 'help' to list commands.\n", SET_TEXT_COLOR_LIGHT_GREY);
        while(true){
            out.printf("%s[GAME PLAY] >>> ", SET_TEXT_COLOR_LIGHT_GREY);
            Scanner myScan = new Scanner(System.in);
            String userIn = myScan.next();
            if(userIn.equals("help")){
                helpGamePlay(out);
            }
            else if(userIn.equals("resign")){
                out.printf("%sAre you sure you wish to resign? (%s%sy or n%s%s): %s",
                        SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC,
                        SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY);
                if(myScan.next().equals("y")){
                    return;
                }
            }
            else if(userIn.equals("leave")){
                return;
            }
            else if(userIn.equals("move")){
                makeMove(out);
            }
            else if(userIn.equals("highlight")){
                highlightMoves(out);
            }
            else if(userIn.equals("redraw")){
                if(playerColor.equals("BLACK")){
                    printGameBlack(out);
                }else{
                    printGameWhite(out);
                }
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
                        SET_TEXT_COLOR_BLUE);
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
                        SET_TEXT_COLOR_BLUE);
                fill = myScan.next().charAt(0);
            }
        }
        return result;
    }

    private static void highlightMoves(PrintStream out) {
        out.printf("%sInput a piece by position (%s%sCOLUMN ROW%s%s) to see it's possible moves: ",
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE,
                RESET_TEXT_ITALIC);
        List<Character> result = validateMoveHighlight(out);
        char col = result.get(0);
        int row = result.get(1) - '0';
        out.println("col = " + col+ "\nrow = " + row);
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
                        SET_TEXT_COLOR_BLUE);
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
                        SET_TEXT_COLOR_BLUE);
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
                        SET_TEXT_COLOR_BLUE);
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
                        SET_TEXT_COLOR_BLUE);
                newRow = myScan.next().charAt(0);
            }
        }
        return result;
    }

    private static void makeMove(PrintStream out){
        out.printf("%sInput piece's current position and the new desired position (%s%sCOLUMN ROW COLUMN ROW%s%s) : ",
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE,
                RESET_TEXT_ITALIC);
        List<Character> result = validateMoveMakeMove(out);
        char oldCol = result.get(0);
        int oldRow = result.get(1) - '0';
        char newCol = result.get(2);
        int newRow = result.get(3) - '0';
        out.println("oldCol = " + oldCol + "\noldRow = " + oldRow + "\nnewCol = " + newCol + "\nnewRow = " + newRow);
    }

    private static boolean logout(PrintStream out) throws Exception {
        HttpResponse<String> response = ServerFacade.logout();
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
        ServerFacade.clear();
    }

    private static char toLower(char let){
        if(let < 97){
            return (char) (let + 32);
        }
        return let;
    }

    private static void printBoard(PrintStream out, String[][] board, char[] lets, int[] range, int[] cols){
        String textColor;
        String bgColor;
        String textBold;
        String LIGHT_BOARD = SET_BG_COLOR_WHITE;
        String DARK_BOARD = SET_BG_COLOR_BROWN;
        String BORDER_COLOR = SET_BG_COLOR_LIGHT_GREY;
        String p;
        out.printf("%s   %s%s", BORDER_COLOR, SET_TEXT_COLOR_BLACK, SET_TEXT_BOLD);
        for(char l : lets){
            out.printf("  %s  ", l);
        }
        int r = 0;
        int b = 0;
        out.printf("   %s\n", RESET_BG_COLOR);
        for(int a : range){
            out.printf("%s %s%s%d ", BORDER_COLOR, SET_TEXT_COLOR_BLACK, SET_TEXT_BOLD, 8 - a);
            for(int c : cols){
                textColor = SET_TEXT_COLOR_BLACK;
                textBold = SET_TEXT_BOLD;
                p = board[r][b];
                if(a % 2 == 0 && c % 2 == 0){
                    bgColor = LIGHT_BOARD;
                }
                else if(a % 2 != 0 && c%2 != 0){
                    bgColor = LIGHT_BOARD;
                }
                else{
                    bgColor = DARK_BOARD;
                }
                out.printf("%s %s%s%s ", bgColor, textColor, textBold, p);
                out.printf("%s%s", RESET_TEXT_COLOR, RESET_BG_COLOR);
                b++;
            }
            b = 0;
            out.printf("%s %s%s%d ", BORDER_COLOR, SET_TEXT_COLOR_BLACK, SET_TEXT_BOLD,8 - a);
            out.printf("%s%s\n", RESET_TEXT_COLOR, RESET_BG_COLOR);
            r++;
        }
        out.printf("%s   %s%s", BORDER_COLOR, SET_TEXT_COLOR_BLACK, SET_TEXT_BOLD);
        for(char l : lets){
            out.printf("  %s  ", l);
        }
        out.printf("   %s%s%s\n", RESET_TEXT_COLOR, RESET_TEXT_BOLD_FAINT, RESET_BG_COLOR);
    }

    private static void printGameBlack(PrintStream out){
        String[][] board = {
                {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_KING, WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK},
                {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,},
                {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_KING, BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},};
        char[] lets = {'h', 'g', 'f', 'e', 'd', 'c', 'b' ,'a'};
        int[] range = {7, 6, 5, 4, 3, 2, 1, 0};
        int[] cols = {7, 6, 5, 4, 3, 2, 1, 0};
        printBoard(out, board, lets, range, cols);
    }

    private static void printGameWhite(PrintStream out){
        String[][] board = {
                {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},
                {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
                {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,},
                {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK}};
        char[] lets = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] range = {0, 1, 2, 3, 4, 5, 6, 7};
        int[] cols = {0, 1, 2, 3, 4, 5, 6, 7};
        printBoard(out, board, lets, range, cols);
    }
}
