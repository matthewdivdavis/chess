package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor team;
    boolean blackCastle;
    boolean whiteCaste;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team = TeamColor.WHITE;
        // false implies they have not castled yet
        blackCastle = false;
        whiteCaste = false;
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
        this.team = team;
    }
    public void switchTurn(TeamColor team) {
        if(team == TeamColor.WHITE) {
            this.team = TeamColor.BLACK;
        }
        else{
            this.team = TeamColor.WHITE;
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
        // Create a new piece and get all the current moves
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        // loop through the moves and add the valid moves
        for(ChessMove move : moves){
            // Make a new board to test moves on
            ChessGame new_board = new ChessGame();
            new_board.setBoard(board);
            // See if spot has opposing team in it
            if(board.getPiece(move.getEndPosition()) != null
                    && board.getPiece(move.getEndPosition()).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                new_board.helpMove(move);
                if (!new_board.isInCheck(board.getPiece(startPosition).getTeamColor())) {
                    valid.add(move);
                }
            }
            // See if spot is empty
            else if(board.getPiece(move.getEndPosition()) == null){
                new_board.helpMove(move);
                if(!new_board.isInCheck(board.getPiece(startPosition).getTeamColor())){
                    valid.add(move);
                }
            }
        }
//        for(ChessMove move : castle(startPosition)){
//            System.out.println("In 'castle' function");
//            valid.add(move);
//        }

        return valid;
    }
    public Collection<ChessMove> castle(ChessPosition startPosition){
        Collection<ChessMove> valid = new ArrayList<>();
        // BLACK CASTLE RIGHT
        if(board.getPiece(startPosition) != null
                &&board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING
                && board.getPiece(startPosition).getTeamColor() == TeamColor.BLACK
                && !blackCastle
                && board.getPiece(new ChessPosition(8, 8)) != null
                && board.getPiece(new ChessPosition(8, 8)).getTeamColor()== TeamColor.BLACK
                && board.getPiece(new ChessPosition(8, 8)).getPieceType() == ChessPiece.PieceType.ROOK
                && board.getPiece(new ChessPosition(8, 7)) == null
                && board.getPiece(new ChessPosition(8, 6)) == null){
            valid.add(new ChessMove(startPosition, new ChessPosition(8 , 7), ChessPiece.PieceType.KING));
        }
        // BLACK CASTE LEFT
        if(board.getPiece(startPosition) != null
                && board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING
                && board.getPiece(startPosition).getTeamColor() == TeamColor.BLACK
                && !blackCastle
                && board.getPiece(new ChessPosition(8, 1)) != null
                && board.getPiece(new ChessPosition(8, 1)).getTeamColor()== TeamColor.BLACK
                && board.getPiece(new ChessPosition(8, 1)).getPieceType() == ChessPiece.PieceType.ROOK
                && board.getPiece(new ChessPosition(8, 2)) == null
                && board.getPiece(new ChessPosition(8, 3)) == null
                && board.getPiece(new ChessPosition(8, 4)) == null){
            valid.add(new ChessMove(startPosition, new ChessPosition(8 , 3), ChessPiece.PieceType.KING));
        }
        // WHITE CASTLE RIGHT
        if(board.getPiece(startPosition) != null
                &&board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING
                && board.getPiece(startPosition).getTeamColor() == TeamColor.WHITE
                && !whiteCaste
                && board.getPiece(new ChessPosition(1, 8)) != null
                && board.getPiece(new ChessPosition(1, 8)).getTeamColor()== TeamColor.WHITE
                && board.getPiece(new ChessPosition(1, 8)).getPieceType() == ChessPiece.PieceType.ROOK
                && board.getPiece(new ChessPosition(1, 7)) == null
                && board.getPiece(new ChessPosition(1, 6)) == null){
            valid.add(new ChessMove(startPosition, new ChessPosition(1 , 7), ChessPiece.PieceType.KING));
        }
        // WHITE CASTE LEFT
        if(board.getPiece(startPosition) != null
                &&board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING
                && board.getPiece(startPosition).getTeamColor() == TeamColor.WHITE
                && !whiteCaste
                && board.getPiece(new ChessPosition(1, 1)) != null
                && board.getPiece(new ChessPosition(1, 1)).getTeamColor()== TeamColor.WHITE
                && board.getPiece(new ChessPosition(1, 1)).getPieceType() == ChessPiece.PieceType.ROOK
                && board.getPiece(new ChessPosition(1, 2)) == null
                && board.getPiece(new ChessPosition(1, 3)) == null
                && board.getPiece(new ChessPosition(1, 4)) == null){
            valid.add(new ChessMove(startPosition, new ChessPosition(1 , 3), ChessPiece.PieceType.KING));
        }
        return valid;
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
                if(board.getPiece(new ChessPosition(r, c)) != null
                        && board.getPiece(new ChessPosition(r, c)).getTeamColor() != teamColor){
                    for(ChessMove move : board.getPiece(new ChessPosition(r, c)).pieceMoves(board, new ChessPosition(r, c))){
                        if(move.getEndPosition().equals(king)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) != null
                && board.getPiece(move.getStartPosition()).getTeamColor() == getTeamTurn()
                && validMoves(move.getStartPosition()).contains(move)){
            if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN
                    && move.getPromotionPiece() != null){
                ChessPiece piece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
                switchTurn(piece.getTeamColor());
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition());
                return;
            }
            switchTurn(board.getPiece(move.getStartPosition()).getTeamColor());
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.removePiece(move.getStartPosition());
        }
        else{
            throw new InvalidMoveException();
        }
    }
    public void helpMove(ChessMove move) {
        switchTurn(board.getPiece(move.getStartPosition()).getTeamColor());
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // loop through the board
        // if every move results in
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                if(board.getPiece(new ChessPosition(r, c)) != null
                        && board.getPiece(new ChessPosition(r, c)).getTeamColor() == teamColor){
                    // make collection of valid moves for the piece
                    Collection<ChessMove> moves = validMoves(new ChessPosition(r, c));
                    // make a board to test on
                    ChessGame newBoard = new ChessGame();
                    for(ChessMove move : moves){
                        newBoard.setBoard(board);
                        newBoard.helpMove(move);
                        if(!newBoard.isInCheck(teamColor)){
                            return false;
                        }
                    }
                }
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
        if(isInCheck(teamColor)){
            return false;
        }
        for(int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                if (board.getPiece(new ChessPosition(r, c)) != null
                        && board.getPiece(new ChessPosition(r, c)).getTeamColor() == teamColor) {
                    if (!validMoves(new ChessPosition(r, c)).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
//        this.board = new ChessBoard();
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                if(board.getPiece(new ChessPosition(r, c)) != null){
                    this.board.addPiece(new ChessPosition(r, c), board.getPiece(new ChessPosition(r, c)));
                }
                else{
                    this.board.addPiece(new ChessPosition(r, c), null);
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, team);
    }
}