package model;
import java.util.UUID;

public class AuthData {
    private final String username;
    private final String authToken;

    public AuthData(String username){
        this.username = username;
        this.authToken = generateToken();
    }

    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
    public String getAuthToken(){
        return authToken;
    }
    public String getUsername(){
        return username;
    }
}
