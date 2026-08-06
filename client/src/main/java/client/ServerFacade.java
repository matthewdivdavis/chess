package client;
import com.google.gson.Gson;
import exception.DataAccessException;
import exception.ResponseException;
import model.GameData;
import request.*;
import response.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private static String serverUrl;
    private static String authorization;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public HttpResponse<String> register(RegisterRequest req) throws Exception{
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        RegisterResult result = gson.fromJson(response.body(), RegisterResult.class);
        authorization = result.authToken();
        return response;
    }

    public void clear() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/db"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> login(LoginRequest req) throws Exception{
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
                .uri(URI.create(serverUrl + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LoginResult result = gson.fromJson(response.body(), LoginResult.class);
        authorization = result.authToken();
        return response;
    }
    public HttpResponse<String> create(String gameName) throws Exception {
        if(gameName == null){
            throw new DataAccessException("GameName null");
        }
        CreateRequest req = new CreateRequest(authorization, gameName);
        Gson gson = new Gson();
        String json = gson.toJson(req);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("Authorization", authorization)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> observe(int gameId) throws IOException, InterruptedException{
        return list();
    }

    public HttpResponse<String> logout() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .header("Authorization", authorization)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        authorization = null;
        return response;
    }

    public GameResult join(int gameId, String color) throws  DataAccessException, IOException, InterruptedException, ResponseException{
        if(!color.equals("BLACK") && !color.equals("WHITE")){
            throw new DataAccessException("Invalid color");
        }
        JoinRequest req = new JoinRequest(color, gameId);
        Gson gson = new Gson();
        String json = gson.toJson(req);
        HttpResponse<String> response = list();
        if(response.statusCode() != 200){
            ErrorResult result = gson.fromJson(response.body(), ErrorResult.class);
            throw new DataAccessException(result.message());
        }
        ListGamesResult result = gson.fromJson(response.body(), ListGamesResult.class);
        for(GameResult g : result.games()){
            if(g.getGameID() == gameId){
                return g;
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Could not find game");
    }
    public HttpResponse<String> list() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("Authorization", authorization)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public GameData getGame(int gameId) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(serverUrl + "/getGame/" + gameId))
                .header("Authorization", authorization)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        GameResult result = new Gson().fromJson(response.body(), GameResult.class);
        return result.getGameData();
    }

    public String getAuthorization(){
        return authorization;
    }

    public void highlight(HighlightRequest req) throws Exception{

    }
}
