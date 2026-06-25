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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);
        List<ChessMove> moves = new ArrayList<>();
        if(piece.getPieceType() == PieceType.BISHOP){
            int r = myPosition.getRow() + 1, c = myPosition.getColumn() + 1;
            while(r <= 8 && c <= 8){
                while(c <= 8 && r <= 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r++;
                    c++;
                }
            }
            r = myPosition.getRow() - 1;
            c = myPosition.getColumn() - 1;
            while(r > 0 && c > 0){
                while(c > 0 && r > 0){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r--;
                    c--;
                }
            }

            r = myPosition.getRow() + 1;
            c = myPosition.getColumn() - 1;
            while(r <= 8 && c > 0){
                while(c > 0 && r <= 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r++;
                    c--;
                }
            }
            r = myPosition.getRow() - 1;
            c = myPosition.getColumn() + 1;
            while(r > 0 && c <= 8){
                while(c <= 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    r--;
                    c++;
                }
            }


//            System.out.println("Break");
//            for(ChessMove i : moves){
//                System.out.println(i);
//            }
        }
        return moves;
    }
}
