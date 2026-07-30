package client;
import com.google.gson.Gson;
import exception.DataAccessException;
import request.CreateRequest;
import request.JoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private static int port = 8080;
    private static final String SERVER_URL = "http://localhost:";
    private static String authorization;

    public ServerFacade(int port){
        this.port = port;
    }
    public static void main(String[] args){
    }

    public static HttpResponse<String> register(RegisterRequest req) throws Exception{
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        RegisterResult result = gson.fromJson(response.body(), RegisterResult.class);
        authorization = result.authToken();
        return response;
    }

    public static void clear() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/db"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> login(LoginRequest req) throws Exception{
        if(req.username() == null){
            throw new DataAccessException("null username");
        }
        if(req.password() == null){
            throw new DataAccessException("null password");
        }
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LoginResult result = gson.fromJson(response.body(), LoginResult.class);
        authorization = result.authToken();
        return response;
    }
    public static HttpResponse<String> create(String gameName) throws Exception {
        if(gameName == null){
            throw new DataAccessException("GameName null");
        }
        CreateRequest req = new CreateRequest(authorization, gameName);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/game"))
                .header("Authorization", authorization)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> observe(int gameId) throws IOException, InterruptedException{
        return list();
    }

    public static HttpResponse<String> list() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/game"))
                .header("Authorization", authorization)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> logout() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/session"))
                .header("Authorization", authorization)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        authorization = null;
        return response;
    }

    public static HttpResponse<String> join(int gameId, String color) throws  Exception {
        if(!color.equals("BLACK") && !color.equals("WHITE")){
            throw new DataAccessException("Invalid color");
        }

        JoinRequest req = new JoinRequest(color, gameId);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + port + "/game"))
                .header("Authorization", authorization)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
