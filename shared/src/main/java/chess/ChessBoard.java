package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position){
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }
    public ChessPosition getKing(ChessGame.TeamColor color){
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                if(getPiece(new ChessPosition(r, c)) != null
                        && getPiece(new ChessPosition(r, c)).getTeamColor() == color
                        && getPiece(new ChessPosition(r, c)).getPieceType() == ChessPiece.PieceType.KING){
                    return new ChessPosition(r, c);
                }
            }
        }
        return new ChessPosition(1, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**a
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] backRow = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        for(int i = 1; i <= 8; i++){
            // Black Pieces
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i - 1]));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            // White Pieces
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, backRow[i - 1]));
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for( int r = 8; r >= 1; r--){
            sb.append(r).append(" ");
            for(int c = 1; c <= 8; c++){
                ChessPiece piece = getPiece(new ChessPosition(r, c));
                if(piece == null){
                    sb.append(". ");
                }
                else{
                    char symbol;
                    switch (piece.getPieceType()) {
                        case KING:
                            symbol = 'K';
                            break;
                        case QUEEN:
                            symbol = 'Q';
                            break;
                        case ROOK:
                            symbol = 'R';
                            break;
                        case BISHOP:
                            symbol = 'B';
                            break;
                        case KNIGHT:
                            symbol = 'N';
                            break;
                        case PAWN:
                            symbol = 'P';
                            break;
                        default:
                            symbol = '?';
                    }
                    if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        symbol = Character.toLowerCase(symbol);
                    }
                    sb.append(symbol).append(" ");
                }
            }
            sb.append("\n");
        }
        sb.append("  1 2 3 4 5 6 7 8");
        return sb.toString();
    }
}