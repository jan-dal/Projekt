// Holds information about the status of pieces
import java.util.*;

public class BoardData {
    // 1D array representing the chessboard
    private static PieceGUI[] Chessboard;
    // 
    private static ArrayList<Piece> White, Black;
    private static Piece Moved;

    // Initialize chessboard from fen
    public static void setBoard(String fen){
        Piece tempPiece;
        Chessboard = new PieceGUI[64];
        White = new ArrayList<Piece>();
        Black = new ArrayList<Piece>();
        
    // Convert fen string to array

        int i = 0;
        for(char x : fen.toCharArray()){
            if(Character.isWhitespace(x)){
                break;
            }
            else if(Character.isLetter(x)){
                if(Character.isLowerCase(x)){
                    if(x == 'k'){
                    }
                    tempPiece = new Piece(Character.toString(Character.toUpperCase(x)), "b", i, false, false);
                    Chessboard[i] = new PieceGUI();
                    Chessboard[i].SetPiece(tempPiece);
                    Black.add(tempPiece);
                } else {
                    if(x == 'K'){
                    }
                    tempPiece = new Piece(Character.toString(x), "w", i, false, false);
                    Chessboard[i] = new PieceGUI();
                    Chessboard[i].SetPiece(tempPiece);
                    White.add(tempPiece);
                }
            }
            else if(Character.isDigit(x)){
                int p = i;
                for(int k = 0; k < Character.getNumericValue(x); k++){
                    Chessboard[p+k] = new PieceGUI();
                    i++;
                }
                i--;
            }
            if(x != '/'){
                i++;
            }
        }
        Collections.sort(White);
        Collections.sort(Black);
    }

    public static int getKingPos(String side){
        if(side.equals("w")){
            return White.get(0).getPosition();
        } else {
            return Black.get(0).getPosition();
        }
    }

    public static PieceGUI get(int cell){
        return Chessboard[cell];
    }

    public static ArrayList<Piece> getWhite(){
        return White;
    }

    public static ArrayList<Piece> getBlack(){
        return Black;
    }

    public static PieceGUI[] getChessboard(){
        return Chessboard;
    }

    public static void setMoved(Piece p){
        Moved = p;
    }

    public static Piece getMoved(){
        return Moved;
    }
}