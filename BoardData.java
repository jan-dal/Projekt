// Holds information about the status of pieces

public class BoardData {
    static Piece[] AllSquares;
    static int Wkp, Bkp;

    // Initialize chessboard from fen
    public static void setBoard(String fen){
        AllSquares = new Piece[64];
        
        int i = 0;
        for(char x : fen.toCharArray()){
            if(Character.isWhitespace(x)){
                break;
            }
            else if(Character.isLetter(x)){
                if(Character.isLowerCase(x)){
                    if(x == 'k'){
                        Bkp = i;
                    }
                    AllSquares[i] = new Piece(Character.toString(Character.toUpperCase(x)), "b");
                } else {
                    if(x == 'K'){
                        Wkp = i;
                    }
                    AllSquares[i] = new Piece(Character.toString(x), "w");
                }
            }
            else if(Character.isDigit(x)){
                int p = i;
                for(int k = 0; k < Character.getNumericValue(x); k++){
                    AllSquares[p+k] = new Piece();
                    i++;
                }
                i--;
            }
            if(x != '/'){
                i++;
            }
        }
    }

    public static int kingpos(String side){
        if(side.equals("w")){
            return Wkp;
        } else {
            return Bkp;
        }
    }


}
