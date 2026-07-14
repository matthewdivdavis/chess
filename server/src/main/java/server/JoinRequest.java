package server;

import chess.ChessGame;
import org.jetbrains.annotations.NotNull;

public record JoinRequest(String authToken, ChessGame.TeamColor color, String gameID) {
    @NotNull
    @Override
    public String toString() {
        return "JoinRequest:\n" +
                "authToken='" + authToken + '\'' + '\n' +
                "color='" + color.toString() + '\'' + '\n' +
                "gameID='" + gameID + '\'';
    }
}
