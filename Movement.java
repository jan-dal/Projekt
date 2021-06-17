// Defines cells a particular piece can move to according to chess rules
// Needs information about checks 
// Board cells:

// A B  C  D  E  F  G  H
// 0 1  2  3  4  5  6  7
// 8 9 10 11 12 13 14 15
//...
// 56 57 58 59 60 61 62 63
import java.util.*;

public abstract class Movement {

    // This method returns a list of possible moves according to movement logic
    public abstract void CalculateMoves();
    public abstract void OtherSideToMove();

}

class ChessMovement extends Movement{
    private ArrayList<Integer> movelist;
    private int[] knightmov = {6, 10, 15, 17, -6, -10, -15, -17};
    private String tomove, opponent;
    private int source, curr, dest, x, i;
    private boolean stop;


    @Override
    public void CalculateMoves(){
        if(tomove.equals("w")){
            for(Piece e : BoardData.getWhite()){
                if(e.getActive()){
                    e.setMoveset(GetMoves(e.getPosition()));
                }
            }
            for(Piece e : BoardData.getBlack()){
                e.setMoveset(null);
            }
        } else {
            for(Piece e : BoardData.getBlack()){
                if(e.getActive()){
                    e.setMoveset(GetMoves(e.getPosition()));
                }
            }
            for(Piece e : BoardData.getWhite()){
                e.setMoveset(null);
            }
        }
        if(IsCheckmate()){
            System.out.println("Checkmate!");
            System.out.println(opponent);
        }
    }


    private ArrayList<Integer> GetMoves(int source){
        movelist = new ArrayList<Integer>();
        this.source = source;
        opponent = getOpponent(source);

        PieceGUI square = BoardData.get(source);
        Piece piece = square.getPiece();


        if(!square.emptySquare() && piece.getSide().equals(tomove)){
            switch(BoardData.get(source).getPiece().getType()){
            case "P": PawnMove();
            break;
            case "B": BishopMove();
            break;
            case "N": KnightMove();
            break;
            case "R": RookMove();
            break;
            case "Q": QueenMove();
            break;
            case "K": KingMove();
            break;
            }
        }

        FilterOutIllegal();

        return movelist;
    }
    public void WhiteToMove(){
        tomove = "w";
    }
    public void BlackToMove(){
        tomove = "b";
    }
    @Override
    public void OtherSideToMove(){
        if(tomove.equals("w")){
            tomove = "b";
        } else{
            tomove = "w";
        }
    }

    // Illegal moves are those that leave the king in check

    private void FilterOutIllegal(){
        ArrayList<Integer> illegal = new ArrayList<Integer>();
        PieceGUI DSquare;
        PieceGUI SrSquare = BoardData.get(source);


        for(int e : movelist){
            DSquare = BoardData.get(e);
            SrSquare.getPiece().setPosition(e);
            SrSquare.setVisible(false);
            DSquare.SetSimPiece(SrSquare.getPiece());
        
            if(isAttacked(BoardData.getKingPos(tomove))){
                illegal.add(e);
            }
            DSquare.RemoveSimPiece();
            SrSquare.getPiece().setPosition(source);
            SrSquare.setVisible(true);
        }
        movelist.removeAll(illegal);
    }
    private boolean IsCheckmate(){
        ArrayList<Integer> list;
        if(tomove.equals("w")){
            for(Piece e : BoardData.getWhite()){
                list = e.getMoveset();
                if(list != null && list.size() > 0){
                    return false;
                }
            }
        } else {
            for(Piece e : BoardData.getBlack()){
                list = e.getMoveset();
                if(list != null && list.size() > 0){
                    return false;
                }
            }
        }
        return true;
    }

    private void BishopMove(){
        movelist.addAll(CreateList(7,"NW"));
        movelist.addAll(CreateList(7,"NE"));
        movelist.addAll(CreateList(7,"SW"));
        movelist.addAll(CreateList(7,"SE"));
    }
    private void RookMove(){
        movelist.addAll(CreateList(7,"UP"));
        movelist.addAll(CreateList(7,"DOWN"));
        movelist.addAll(CreateList(7,"LEFT"));
        movelist.addAll(CreateList(7,"RIGHT"));
    }
    private void QueenMove(){
        RookMove();
        BishopMove();
    }
    private void KingMove(){
        movelist.addAll(CreateList(1,"UP"));
        movelist.addAll(CreateList(1,"DOWN"));
        movelist.addAll(CreateList(1,"LEFT"));
        movelist.addAll(CreateList(1,"RIGHT"));
        movelist.addAll(CreateList(1,"NE"));
        movelist.addAll(CreateList(1,"NW"));
        movelist.addAll(CreateList(1,"SE"));
        movelist.addAll(CreateList(1,"SW"));
    } 
    private void KnightMove(){
        i = 0;
        while(i < 8){
            movelist.addAll(CreateList(1,"NM"));
        }
    }
    private void PawnMove(){
        Piece piece = BoardData.get(source).getPiece();
        if(tomove.equals("w")){
            if(!piece.Moved()){
                movelist.addAll(CreateList(2,"UP"));
            } else {
                movelist.addAll(CreateList(1,"UP"));
            }
            movelist.addAll(CreateList(1,"NE"));
            movelist.addAll(CreateList(1,"NW"));
        } else {
            if(!piece.Moved()){
                movelist.addAll(CreateList(2,"DOWN"));
            } else {
                movelist.addAll(CreateList(1,"DOWN"));
            }
            movelist.addAll(CreateList(1,"SE"));
            movelist.addAll(CreateList(1,"SW"));           
        }
    }
    /*

    private boolean enPessant(){

    }

    private boolean KingsideCastling(){


    
    }

    private boolean QueensideCastling(){
        //The castling must be kingside or queenside.
        //Neither the king nor the chosen rook has previously moved.
        //There are no pieces between the king and the chosen rook.
        //The king is not currently in check.
        //The king does not pass through a square that is attacked by an enemy piece.
        //The king does not end up in check. (True of any legal move.)
    }
    */

    // Check if atk square is attacked by a piece
    private boolean isAttacked(int atk){
        ArrayList<String> types = new ArrayList<String>();
        types.add("R");
        types.add("Q");

        if(isAttacking(7,"UP",types, atk) || isAttacking(7,"DOWN",types, atk) || 
           isAttacking(7,"RIGHT",types, atk) || isAttacking(7,"LEFT",types, atk)){
               return true;
           }
        
        types.remove("R");
        types.add("B");
        if(isAttacking(7,"NW",types, atk) || isAttacking(7,"NE",types, atk) || 
        isAttacking(7,"SW",types, atk) || isAttacking(7,"SE",types, atk)){
            return true;
        }
        types.remove("B");
        types.remove("Q");

        /*
        types.add("N");

        if(isAttacking(7,"NM",types)){
            return true;
        }

        types.remove("N");
        */
        types.add("P");
    
        if(tomove.equals("w")){
            if(isAttacking(1,"NW",types, atk) || isAttacking(1,"NE",types, atk)){
                return true;
            }
        } else {
            if(isAttacking(1,"SW",types, atk) || isAttacking(1,"SE",types, atk)){
                return true;
            }
        }
        return false;
    }


// Movement 


    private void UP(){
        dest -= 8;
        curr = dest;
        if (dest < 0){
            stop = true;
        }
    }

    private void DOWN(){
        dest += 8;
        curr = dest;
        if (dest > 63){
            stop = true;
        }
    }

    private void LEFT(){
        dest -= 1;
        x -= 1;
        curr = dest;
        if (x == -1){
            stop = true;
        }
    }

    private void RIGHT(){
        dest += 1;
        x += 1;
        curr = dest;
        if (x == 8){
            stop = true;
        }
    }

    private void NE(){
        dest -= 7;
        x += 1;
        curr = dest;
        if(x > 7 || dest < 0){
            stop = true;
        }
    }

    private void NW(){
        dest -= 9;
        x -= 1;
        curr = dest;
        if(x < 0 || dest < 0){
            stop = true;
        }
    }

    private void SE(){
        dest += 9;
        x += 1;
        curr = dest;
        if(x > 7 || dest > 63){
            stop = true;
        }
    }

    private void SW(){
        dest += 7;
        x -= 1;
        curr = dest;
        if(x < 0 || dest > 63){
            stop = true;
        }
    }

    private void NM(){
        curr = source + knightmov[i];
        if(curr < 0 || curr > 63 || Math.abs(((curr % 8) - x)) > 2){
            stop = true;
        }
        i++;
    }
    
    private ArrayList<Integer> CreateList(int len, String fun){
        ArrayList<Integer> list = new ArrayList<Integer>();
        x = source % 8;
        stop = false;
        dest = source;
        int j = 0;
        while(j < len){
            switch(fun){
            case "UP"    : UP();
            break;
            case "DOWN"  : DOWN();
            break;
            case "LEFT"  : LEFT();
            break;
            case "RIGHT" : RIGHT();
            break;
            case "NW"    : NW();
            break;
            case "NE"    : NE();
            break;
            case "SW"    : SW();
            break;
            case "SE"    : SE();
            break;
            case "NM"    : NM();
            break;
            }

            if(stop){
                break;
            }
            else if(BoardData.get(curr).emptySquare()){
                list.add(curr);
                j++;
            }
            else if(BoardData.get(curr).getPiece().getSide().equals(opponent)){
                list.add(curr);
                break;
            }
            else{
                break;
            }
        }
        return list;
    }


    // Check if types are attacking atk
    private boolean isAttacking(int len, String fun, ArrayList<String> types, int atk){
        stop = false;
        x = atk % 8;
        int j = 0;
        dest = atk;
        while(j < len){
            switch(fun){
                case "UP" :  UP();
                break;
                case "DOWN" : DOWN();
                break;
                case "LEFT" : LEFT(); 
                break;
                case "RIGHT" : RIGHT();
                break;
                case "NW"    : NW();
                break;
                case "NE"    : NE();
                break;
                case "SW"    : SW();
                break;
                case "SE"    : SE();
                break;
                case "NM"    : NM();
                break;
            }

            if(stop){
                return false;
            }
            else if(BoardData.get(curr).hasSimPiece()){
                return false;
            }
            else if(BoardData.get(curr).emptySquare() || !BoardData.get(curr).getVisible()){
                j++;
            }
            else if(BoardData.get(curr).getPiece().getSide().equals(opponent)){
                if(types.contains(BoardData.get(curr).getPiece().getType())){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }


    private String getOpponent(int source){
        String opponent = "w";
        if(BoardData.get(source).getPiece().getSide().equals("w")){
            opponent = "b";
        }
        return opponent;
    }
}
class EditorMovement extends Movement{

    private ArrayList<Integer> CreateList(int j){
        ArrayList<Integer> m = new ArrayList<Integer>();
        for(int i=0; i < 64; i++){
            m.add(i);
        }
        m.remove(j);
        return m;
    }

    @Override
    public void CalculateMoves(){
        for(Piece e : BoardData.getWhite()){
            e.setMoveset(CreateList(e.getPosition()));
            }
        for(Piece e : BoardData.getBlack()){
            e.setMoveset(CreateList(e.getPosition()));
        }
    }
    @Override
    public void OtherSideToMove(){};
}