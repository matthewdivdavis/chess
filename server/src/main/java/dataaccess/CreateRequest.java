package dataaccess;

import org.jetbrains.annotations.NotNull;

public record CreateRequest(String authToken, String gameName) {
    @NotNull
    @Override
    public String toString() {
        return "CreateRequest:\n" +
                "authToken='" + authToken + '\'' + '\n' +
                "gameName='" + gameName + '\'';
    }
}