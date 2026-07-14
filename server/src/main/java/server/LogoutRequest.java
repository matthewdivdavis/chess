package server;

import org.jetbrains.annotations.NotNull;

public record LogoutRequest(String authToken) {
    @NotNull
    @Override
    public String toString() {
        return "LogoutRequest:\n" +
                "authToken='" + authToken + '\'';
    }
}
