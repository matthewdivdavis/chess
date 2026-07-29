package client;
import io.javalin.Javalin;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ServerFacade {
    private int port;

    public ServerFacade(int port){
        this.port = port;
    }
    public static void main(String[] args){
    }

    public static void register(String username, String password, String email){

    }

    public static void login(String username, String password){

    }
    public static void create(String gameName){

    }

    public static void list(){

    }

    public static void observe(){

    }
    public static void logout(){

    }
}
