package client;

import chess.*;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChess {

    public static void highlight(GameData gameData, int col, int row, boolean black){
        ChessGame chessGame = gameData.getGame();
        ChessBoard chessBoard = chessGame.getBoard();
        if(chessBoard.getPiece(new ChessPosition(row, col)) == null){
            System.out.printf("%sNo piece in given location.\n", BLUE);
            return;
        }
        List<ChessMove> chessMoveList = (List<ChessMove>) chessGame.validMoves(new ChessPosition(row, col));
        if(black){
            drawBlack(gameData, chessMoveList, new ChessPosition(row, col));
        }else{
            System.out.println(chessBoard.getPiece(new ChessPosition(row, col)));
            drawWhite(gameData, chessMoveList, new ChessPosition(row, col));
        }
    }
    public static void drawBoard(GameData gameData, boolean black){
        if(black){
            drawBlack(gameData, null, null);
        }else{
            drawWhite(gameData, null, null);
        }
    }
    private static void drawBlack(GameData gameData, List<ChessMove> validMoves, ChessPosition startPos){
        ChessBoard chessBoard = gameData.getGame().getBoard();
        String[][] board = new String[8][8];
        for(int r = 8; r >= 1; r--){
            for(int c = 1; c < 9; c++){
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(r, c));
                if(piece == null){
                    board[r - 1][8 - c] = EMPTY;
                }else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    switch (piece.getPieceType()){
                        case KING:
                            board[r - 1][8 - c] = BLACK_KING;
                            break;
                        case QUEEN:
                            board[r - 1][8 - c] = BLACK_QUEEN;
                            break;
                        case ROOK:
                            board[r - 1][8 - c] = BLACK_ROOK;
                            break;
                        case BISHOP:
                            board[r - 1][8 - c] = BLACK_BISHOP;
                            break;
                        case KNIGHT:
                            board[r - 1][8 - c] = BLACK_KNIGHT;
                            break;
                        case PAWN:
                            board[r - 1][8 - c] = BLACK_PAWN;
                            break;
                    }
                }else{
                    switch (piece.getPieceType()) {
                        case KING:
                            board[r - 1][8 - c] = WHITE_KING;
                            break;
                        case QUEEN:
                            board[r - 1][8 - c] = WHITE_QUEEN;
                            break;
                        case ROOK:
                            board[r - 1][8 - c] = WHITE_ROOK;
                            break;
                        case BISHOP:
                            board[r - 1][8 - c] = WHITE_BISHOP;
                            break;
                        case KNIGHT:
                            board[r - 1][8 - c] = WHITE_KNIGHT;
                            break;
                        case PAWN:
                            board[r - 1][8 - c] = WHITE_PAWN;
                            break;
                    }
                }
            }
        }
        char[] lets = {'h', 'g', 'f', 'e', 'd', 'c', 'b' ,'a'};
        int[] range = {7, 6, 5, 4, 3, 2, 1, 0};
        printHighlightBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), board, lets, range, range, validMoves, startPos);
    }

    private static void drawWhite(GameData gameData, List<ChessMove> validMoves, ChessPosition startPos){
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
        printHighlightBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), board, lets, range, range, validMoves, startPos);
    }
    private static void printHighlightBoard(PrintStream out, String[][] board, char[] lets, int[] range, int[] cols,
                                            List<ChessMove> validMoves, ChessPosition startPos){
        String textColor;
        String bgColor;
        String textBold;
        String darkHighlight = SET_BG_COLOR_DARK_GREEN;
        String lightHighlight = SET_BG_COLOR_GREEN;
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
            for(int c : range){
                textColor = BLACK;
                textBold = SET_TEXT_BOLD;
                p = board[r][b];
                if(a % 2 == 0 && c % 2 == 0){
                    if(startPos != null && validMoves.contains(new ChessMove(startPos, new ChessPosition(8 - a, c + 1), null))){
                        bgColor = lightHighlight;
                    }
                    else{
                        bgColor = lightBoard;
                    }
                }else if(a % 2 != 0 && c%2 != 0){
                    if(startPos != null && validMoves.contains(new ChessMove(startPos, new ChessPosition(8 - a, c + 1), null))){
                        bgColor = lightHighlight;
                    }
                    else{
                        bgColor = lightBoard;
                    }
                }else{
                    if(startPos != null && validMoves.contains(new ChessMove(startPos, new ChessPosition(8 - a, b + 1), null))){
                        bgColor = darkHighlight;
                    }
                    else{
                        bgColor = darkBoard;
                    }
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
