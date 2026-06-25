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
            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
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
        }
        if(piece.getPieceType() == PieceType.ROOK){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}};
            for (int[] dir : directions){
                int r = myPosition.getRow() + dir[0];
                int c = myPosition.getColumn() + dir[1];
                while(r >= 1 && r <= 8 && c >= 1 && c <= 8){
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
        }
        if(piece.getPieceType() == PieceType.QUEEN){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] dir : directions){
                int r = myPosition.getRow() + dir[0];
                int c = myPosition.getColumn() + dir[1];
                while(r >= 1 && r <= 8 && c >= 1 && c <= 8){
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
        }
        if(piece.getPieceType() == PieceType.KING){
            int[][] directions = {{0, 1}, {0,-1}, {1, 0}, {-1,0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] dir : directions){
                int r = myPosition.getRow() + dir[0];
                int c = myPosition.getColumn() + dir[1];
                if(r >= 1 && r <= 8 && c >= 1 && c <= 8){
                    if(board.getPiece(new ChessPosition(r, c)) == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    }
                    else if(board.getPiece(new ChessPosition(r, c)).pieceColor != piece.pieceColor){
                        moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                    }
                }
            }
        }
        if(piece.getPieceType() == PieceType.PAWN && pieceColor == ChessGame.TeamColor.BLACK){
            int r = myPosition.getRow();
            int c = myPosition.getColumn();
            if(board.getPiece(new ChessPosition(r-1, c)) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c), null));
            } else if (board.getPiece(new ChessPosition(r-1, c-1)) != null
                    && board.getPiece(new ChessPosition(r-1, c-1)).pieceColor != piece.pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c - 1), null));
            } else if (board.getPiece(new ChessPosition(r-1, c+1)) != null
                    && board.getPiece(new ChessPosition(r-1, c+1)).pieceColor != piece.pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(r - 1, c + 1), null));
            }
        }
        return moves;
    }
}
