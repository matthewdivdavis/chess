package server;

import org.jetbrains.annotations.NotNull;

public record RegisterRequest(String username, String password, String email) {
    @NotNull
    @Override
    public String toString() {
        return "RegisterRequest:\n" +
                "username='" + username + '\'' + '\n' +
                "password='" + password + '\'' + '\n' +
                "email='" + email + '\'';
    }
}
