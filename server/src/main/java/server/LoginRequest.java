package server;

import org.jetbrains.annotations.NotNull;

public record LoginRequest(String username, String password) {
    @NotNull
    @Override
    public String toString() {
        return "LoginRequest:\n" +
                "username='" + username + '\'' + '\n' +
                "password='" + password + '\'' + '\n';
    }
}
