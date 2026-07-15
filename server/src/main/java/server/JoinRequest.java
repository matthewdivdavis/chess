package server;

import chess.ChessGame;
import org.jetbrains.annotations.NotNull;

public record JoinRequest(String playerColor, int gameID) {
    @NotNull
    @Override
    public String toString() {
        return "JoinRequest:\n" +
                "color='" + playerColor + '\'' + '\n' +
                "gameID='" + gameID + '\'';
    }
}
