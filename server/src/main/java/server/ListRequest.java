package server;

import org.jetbrains.annotations.NotNull;

public record ListRequest(String authToken) {
    @NotNull
    @Override
    public String toString() {
        return "ListRequest:\n" +
                "authToken='" + authToken+ '\'';
    }
}
