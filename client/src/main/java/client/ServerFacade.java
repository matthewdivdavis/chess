package client;
import com.google.gson.Gson;
import io.javalin.Javalin;
import server.LoginRequest;
import server.RegisterRequest;
import service.LoginResult;

import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ServerFacade {
    private static int port = 8080;
    private static final String serverUrl = "http://localhost:";

    public ServerFacade(int port){
//        this.port = port;
    }
    public static void main(String[] args){
    }

    public static HttpResponse<String> register(RegisterRequest req) throws Exception{
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + port + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

    public static void clear() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + port + "/db"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return;
    }

    public static HttpResponse<String> login(LoginRequest req) throws Exception{

        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + port + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;

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
