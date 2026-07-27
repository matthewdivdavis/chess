package client;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        preLogin(out);
        postLogin(out);
    }

    private static void postLogin(PrintStream out){
        Scanner myScan = new Scanner(System.in);
        while(true){
            out.printf(SET_TEXT_COLOR_LIGHT_GREY);
            out.print("[LOGGED IN] >>> ");
            String input = myScan.next().trim();
            if(input.equals("help")){
                helpLoggedIn(out);
            }
        }
    }
    private static void preLogin(PrintStream out){
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
                return;
            }

        }
    }
    private static void helpLoggedIn(PrintStream out){

    }
    private static void helpLoggedOut(PrintStream out){
        out.printf("%s\tregister <USERNAME> <PASSWORD> <EMAIL> %s- to create an account\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tlogin <USERNAME> <PASSWORD> %s- to play chess\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\tquit %s- playing chess\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf("%s\thelp %s- with possible commands\n", SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA);
        out.printf(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static boolean login(PrintStream out){
        out.printf("%sInput your username and password (%s%susername password%s%s): ",SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();
        System.out.println("Logged in as " + username);
        return true;
    }
    private static boolean register(PrintStream out){
        out.printf("%sInput your username, password andm email (%s%susername password email%s%s): ",SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA, SET_TEXT_ITALIC, RESET_TEXT_ITALIC, SET_TEXT_COLOR_BLUE);
        Scanner myScan = new Scanner(System.in);
        String username = myScan.next();
        String password = myScan.next();
        String email = myScan.next();
        System.out.println("Logged in as " + username);
        return true;
    }

}
