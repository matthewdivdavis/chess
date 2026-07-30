package client;
import com.google.gson.Gson;
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
    private static int port;
    private static final String ServerUrl = "http://localhost:";
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
                .uri(URI.create(ServerUrl + port + "/user"))
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
                .uri(URI.create(ServerUrl + port + "/db"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> login(LoginRequest req) throws Exception{

        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerUrl + port + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LoginResult result = gson.fromJson(response.body(), LoginResult.class);
        authorization = result.authToken();
        return response;
    }
    public static HttpResponse<String> create(String gameName) throws IOException, InterruptedException {
        CreateRequest req = new CreateRequest(authorization, gameName);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerUrl + port + "/game"))
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
                .uri(URI.create(ServerUrl + port + "/game"))
                .header("Authorization", authorization)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> logout() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerUrl + port + "/session"))
                .header("Authorization", authorization)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        authorization = "";
        return response;
    }

    public static HttpResponse<String> join(int gameId, String color) throws IOException, InterruptedException {
        JoinRequest req = new JoinRequest(color, gameId);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerUrl + port + "/game"))
                .header("Authorization", authorization)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
