package client;

import chess.*;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class DrawChess {
    public static void drawBoard(GameData gameData, boolean black){
        if(black){
            drawBlack(gameData);
        }else{
            drawWhite(gameData);
        }
    }
    private static void drawBlack(GameData gameData){
        ChessBoard chessBoard = gameData.getGame().getBoard();
        String[][] board = new String[8][8];
        for(int r = 8; r >= 1; r--){
            for(int c = 1; c < 9; c++){
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(r, c));
                if(piece == null){
                    board[r - 1][c - 1] = EMPTY;
                }else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    switch (piece.getPieceType()){
                        case KING:
                            board[r - 1][c - 1] = BLACK_KING;
                            break;
                        case QUEEN:
                            board[r - 1][c - 1] = BLACK_QUEEN;
                            break;
                        case ROOK:
                            board[r - 1][c - 1] = BLACK_ROOK;
                            break;
                        case BISHOP:
                            board[r - 1][c - 1] = BLACK_BISHOP;
                            break;
                        case KNIGHT:
                            board[r - 1][c - 1] = BLACK_KNIGHT;
                            break;
                        case PAWN:
                            board[r - 1][c - 1] = BLACK_PAWN;
                            break;
                    }
                }else{
                    switch (piece.getPieceType()) {
                        case KING:
                            board[r - 1][c - 1] = WHITE_KING;
                            break;
                        case QUEEN:
                            board[r - 1][c - 1] = WHITE_QUEEN;
                            break;
                        case ROOK:
                            board[r - 1][c - 1] = WHITE_ROOK;
                            break;
                        case BISHOP:
                            board[r - 1][c - 1] = WHITE_BISHOP;
                            break;
                        case KNIGHT:
                            board[r - 1][c - 1] = WHITE_KNIGHT;
                            break;
                        case PAWN:
                            board[r - 1][c - 1] = WHITE_PAWN;
                            break;
                    }
                }
            }
        }
        char[] lets = {'h', 'g', 'f', 'e', 'd', 'c', 'b' ,'a'};
        int[] range = {7, 6, 5, 4, 3, 2, 1, 0};
        printBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), board, lets, range, range);
    }

    private static void drawWhite(GameData gameData){
        ChessBoard chessBoard = gameData.getGame().getBoard();
        String[][] board = new String[8][8];
        for(int r = 8; r >= 1; r--){
            for(int c = 1; c < 9; c++){
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(r, c));
                if(piece == null){
                    board[8 - r][c - 1] = EMPTY;
                }else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    switch (piece.getPieceType()){
                        case KING:
                            board[8 - r][c - 1] = BLACK_KING;
                            break;
                        case QUEEN:
                            board[8 - r][c - 1] = BLACK_QUEEN;
                            break;
                        case ROOK:
                            board[8 - r][c - 1] = BLACK_ROOK;
                            break;
                        case BISHOP:
                            board[8 - r][c - 1] = BLACK_BISHOP;
                            break;
                        case KNIGHT:
                            board[8 - r][c - 1] = BLACK_KNIGHT;
                            break;
                        case PAWN:
                            board[8 - r][c - 1] = BLACK_PAWN;
                            break;
                    }
                }else{
                    switch (piece.getPieceType()) {
                        case KING:
                            board[8 - r][c - 1] = WHITE_KING;
                            break;
                        case QUEEN:
                            board[8 - r][c - 1] = WHITE_QUEEN;
                            break;
                        case ROOK:
                            board[8 - r][c - 1] = WHITE_ROOK;
                            break;
                        case BISHOP:
                            board[8 - r][c - 1] = WHITE_BISHOP;
                            break;
                        case KNIGHT:
                            board[8 - r][c - 1] = WHITE_KNIGHT;
                            break;
                        case PAWN:
                            board[8 - r][c - 1] = WHITE_PAWN;
                            break;
                    }
                }
            }
        }
        char[] lets = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] range = {0, 1, 2, 3, 4, 5, 6, 7};
        printBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), board, lets, range, range);
    }
    private static void printBoard(PrintStream out, String[][] board, char[] lets, int[] range, int[] cols){
        String textColor;
        String bgColor;
        String textBold;
        String lightBoard = SET_BG_COLOR_WHITE;
        String darkBoard = SET_BG_COLOR_BROWN;
        String boarderColor = SET_BG_COLOR_LIGHT_GREY;
        String p;
        out.printf("\n%s   %s%s", boarderColor, BLACK, SET_TEXT_BOLD);
        for(char l : lets){
            out.printf("  %s  ", l);
        }
        int r = 0;
        int b = 0;
        out.printf("   %s\n", RESET_BG_COLOR);
        for(int a : range){
            out.printf("%s %s%s%d ", boarderColor, BLACK, SET_TEXT_BOLD, 8 - a);
            for(int c : cols){
                textColor = BLACK;
                textBold = SET_TEXT_BOLD;
                p = board[r][b];
                if(a % 2 == 0 && c % 2 == 0){
                    bgColor = lightBoard;
                }else if(a % 2 != 0 && c%2 != 0){
                    bgColor = lightBoard;
                }else{
                    bgColor = darkBoard;
                }
                out.printf("%s %s%s%s %s%s", bgColor, textColor, textBold, p, RESET_TEXT_COLOR, RESET_BG_COLOR);
                b++;
            }
            b = 0;
            out.printf("%s %s%s%d %s%s\n", boarderColor, BLACK, SET_TEXT_BOLD,8 - a,RESET_TEXT_COLOR, RESET_BG_COLOR);
            r++;
        }
        out.printf("%s   %s%s", boarderColor, BLACK, SET_TEXT_BOLD);
        for(char l : lets){
            out.printf("  %s  ", l);
        }
        out.printf("   %s%s%s\n", RESET_TEXT_COLOR, RESET_TEXT_BOLD_FAINT, RESET_BG_COLOR);
        out.printf("%sType 'help' to list commands. \n[GAME PLAY] >>> ", LIGHT_GREY);
    }
}
