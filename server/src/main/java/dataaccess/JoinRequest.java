package dataaccess;

import chess.ChessGame;

public record JoinRequest(String authToken, ChessGame.TeamColor color, String gameID) {
}
