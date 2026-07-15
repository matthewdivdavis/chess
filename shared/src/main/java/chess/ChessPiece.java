package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public String toString() {
        return pieceColor + ", " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> getMovesLong(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int[][] directions){
        List<ChessMove> moves = new ArrayList<>();
        for (int[] dir : directions) {
            int r = myPosition.getRow() + dir[0];
            int c = myPosition.getColumn() + dir[1];
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                if(board.getPiece(new ChessPosition(r, c)) == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r += dir[0];
                    c += dir[1];
                }
                else if(board.getPiece(new ChessPosition(r, c)).pieceColor != piece.pieceColor){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r += dir[0];
                    c += dir[1];
                    break;
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }
    public Collection<ChessMove> getMovesShort(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int[][] directions) {
        List<ChessMove> moves = new ArrayList<>();
        for(int[] dir : directions){
            int r = myPosition.getRow() +dir[0];
            int c = myPosition.getColumn() + dir[1];
            if(r >= 1 && r <= 8 && c >= 1 && c <= 8){
                if(board.getPiece(new ChessPosition(r, c)) == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
                else if (board.getPiece(new ChessPosition(r, c)).pieceColor != piece.pieceColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);
        List<ChessMove> moves = new ArrayList<>();
        // BISHOP
        if(piece.getPieceType() == PieceType.BISHOP){
            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            return getMovesLong(board, myPosition, piece, directions);
        }
        //ROOK
        if(piece.getPieceType() == PieceType.ROOK){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}};
            return getMovesLong(board, myPosition, piece, directions);
        }
        // QUEEN
        if(piece.getPieceType() == PieceType.QUEEN){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            return getMovesLong(board, myPosition, piece, directions);
        }
        // KNIGHT
        if(piece.getPieceType() == PieceType.KNIGHT){
            int[][] directions = {{2, 1}, {2, -1}, {-1, 2}, {1, 2}, {-2, 1}, {-2, -1},{-1, -2}, {1, -2}};
            return getMovesShort(board, myPosition, piece, directions);
        }
        // KING
        if(piece.getPieceType() == PieceType.KING){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            return getMovesShort(board, myPosition, piece, directions);
        }
// BLACK PAWN
        if(piece.getPieceType() == PieceType.PAWN && pieceColor == ChessGame.TeamColor.BLACK
                && myPosition.getRow() != 1){

            int r = myPosition.getRow();
            int c = myPosition.getColumn();
            // case 1: move pawn forward
            if(board.getPiece(new ChessPosition(r-1, c)) == null){
                if(r == 2){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c), type));
                        }
                    }
                }
                else if(r == 7 && board.getPiece(new ChessPosition(r-2, c)) == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r - 2, c), null));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c), null));
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c), null));
                }
            }

            // case 2: pawn capture (left)
            if (c != 1 && board.getPiece(new ChessPosition(r-1, c-1)) != null
                    && board.getPiece(new ChessPosition(r-1, c-1)).pieceColor != piece.pieceColor){
                if(r == 2){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c - 1), type));
                        }
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c - 1), null));
                }
            }
            // case 3: pawn capture (right)
            if (c != 8 && board.getPiece(new ChessPosition(r-1, c+1)) != null
                    && board.getPiece(new ChessPosition(r-1, c+1)).pieceColor != piece.pieceColor){
                if(r == 2){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c+1), type));
                        }
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c + 1), null));
                }
            }
        }

// PAWN WHITE
        if(piece.getPieceType() == PieceType.PAWN && pieceColor == ChessGame.TeamColor.WHITE
                && myPosition.getRow() != 8){

            int r = myPosition.getRow();
            int c = myPosition.getColumn();
            // case 1: move pawn forward
            if(board.getPiece(new ChessPosition(r+1, c)) == null){
                if(r == 7){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c), type));
                        }
                    }
                }
                else if(r == 2 && board.getPiece(new ChessPosition(r+2, c)) == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + 2, c), null));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c), null));
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c), null));
                }
            }

            // case 2: pawn capture (left)
            if (c != 1 && board.getPiece(new ChessPosition(r+1, c-1)) != null
                    && board.getPiece(new ChessPosition(r+1, c-1)).pieceColor != piece.pieceColor){
                if(r == 7){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c - 1), type));
                        }
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c - 1), null));
                }
            }
            // case 3: pawn capture (right)
            if (c != 8 && board.getPiece(new ChessPosition(r+1, c+1)) != null
                    && board.getPiece(new ChessPosition(r+1, c+1)).pieceColor != piece.pieceColor){
                if(r == 7){
                    for(PieceType type : PieceType.values()){
                        if(type != PieceType.PAWN && type != PieceType.KING){
                            moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c+1), type));
                        }
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + 1, c + 1), null));
                }
            }
        }
        return moves;
    }
}
