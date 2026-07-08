package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor team;

    public ChessGame() {
        board = new ChessBoard();
        // not sure about setting it to white. We'll see.
        team = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(team == TeamColor.WHITE){
            team = TeamColor.BLACK;
        }
        else{
            team = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Get move list
        // loop through list and return non-nulls
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        isInCheck(board.getPiece(startPosition).getTeamColor());

        for(ChessMove move : moves){
            if(board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getEndPosition()).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                valid.add(move);
            }
        }
        return valid;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // loop through the board, if any piece on the board is not the team color, get their move list
        // if anything on that move list == kings position then return true else false
        ChessPosition king = board.getKing(teamColor);
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                if(board.getPiece(new ChessPosition(r, c)) != null){
                    for(ChessMove move : board.getPiece(new ChessPosition(r, c)).pieceMoves(board, new ChessPosition(r, c))){
                        if(move.getEndPosition() == king){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // loop through the board, if any piece on the board is not the team color, get their move list
        // if anything on that move list == kings position then return true else false
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                System.out.println(board.getPiece(new ChessPosition(r, c)));
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
