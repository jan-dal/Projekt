// Holds information about the status of pieces
import java.util.*;

public class BoardData {
    // 1D array representing the chessboard
    private static SquareGUI[] Chessboard;
    // 
    private static ArrayList<Piece> White, Black;
    private static Piece Moved;

    // Initialize chessboard from fen
    public static void setBoard(String fen){
        Piece tempPiece;
        Chessboard = new SquareGUI[64];
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
                    tempPiece = CreatePiece(Character.toString(Character.toUpperCase(x)), "b", i);
                    Chessboard[i] = new SquareGUI();
                    Chessboard[i].SetPiece(tempPiece);
                    Black.add(tempPiece);
                } else {
                    if(x == 'K'){
                    }
                    tempPiece = CreatePiece(Character.toString(x),"w", i);
                    Chessboard[i] = new SquareGUI();
                    Chessboard[i].SetPiece(tempPiece);
                    White.add(tempPiece);
                }
            }
            else if(Character.isDigit(x)){
                int p = i;
                for(int k = 0; k < Character.getNumericValue(x); k++){
                    Chessboard[p+k] = new SquareGUI();
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

    public static Piece getKing(String side){
        if(side.equals("w")){
            return White.get(0);
        } else {
            return Black.get(0);
        }
    }

    public static SquareGUI get(int cell){
        return Chessboard[cell];
    }

    public static ArrayList<Piece> getWhite(){
        return White;
    }

    public static ArrayList<Piece> getBlack(){
        return Black;
    }

    public static SquareGUI[] getChessboard(){
        return Chessboard;
    }

    public static void setMoved(Piece p){
        Moved = p;
    }

    public static Piece getMoved(){
        return Moved;
    }

    private static Piece CreatePiece(String type, String Side, int pos){

        switch(type){
            case "P" : return new Pawn(Side, pos, false);
            case "B" : return new Bishop(Side, pos, false);
            case "N" : return new Knight(Side, pos, false);
            case "R" : return new Rook(Side, pos, false);
            case "Q" : return new Queen(Side, pos, false);
            case "K" : return new King(Side, pos, false);
            case "T" : return new Trapper(Side, pos, false);
            default : return null;
        }
    }
}