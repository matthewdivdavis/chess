package model;
import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private final String username;
    private String authToken;

    public AuthData(String username){
        this.username = username;
        this.authToken = generateToken();
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }
}
