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
    public abstract ArrayList<Integer> GetMoves(int source);
    public abstract void OtherSideToMove();

}

class ChessMovement extends Movement{
    ArrayList<Integer> movelist;
    int[] knight = {6, 10, 15, 17, -6, -10, -15, -17};
    String tomove, opponent;
    int source, curr, dest, x, i;

    @Override
    public ArrayList<Integer> GetMoves(int source){
        ArrayList<Integer> illegal = new ArrayList<Integer>();
        movelist = new ArrayList<Integer>();
        this.source = source;
        opponent = getOpponent(source);
        if(!BoardData.AllSquares[source].emptySquare() && BoardData.AllSquares[source].getSide().equals(tomove)){
            switch(BoardData.AllSquares[source].getType()){
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

        BoardData.AllSquares[source].Deactivate();
        System.out.println(movelist);
        for(int e : movelist){
            BoardData.AllSquares[e].Activate();
            if(isAttacked(BoardData.kingpos(tomove))){
                System.out.println("illegal found!");
                illegal.add(e);
            }
            x = source % 8;
            System.out.println("Check!");
            BoardData.AllSquares[e].Deactivate();
        }
        BoardData.AllSquares[source].Activate();
        movelist.removeAll(illegal);

        System.out.println(movelist);
        System.out.println(illegal);
        System.out.println();
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
        for(int i = 0; i < 8; i++){
            NM();
            if (dest != 64){
                movelist.addAll(CreateList(1,"X"));
            }
        }
    }
    private void PawnMove(){
        if(tomove.equals("w")){
            if(!BoardData.AllSquares[source].hasMoved()){
                movelist.addAll(CreateList(2,"UP"));
            } else {
                movelist.addAll(CreateList(1,"UP"));
            }
            movelist.addAll(CreateList(1,"NE"));
            movelist.addAll(CreateList(1,"NW"));
        } else {
            if(!BoardData.AllSquares[source].hasMoved()){
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


    private boolean isAttacked(int atk){
        ArrayList<String> types = new ArrayList<String>();
        System.out.println(atk);
        System.out.println();
        types.add("R");
        types.add("Q");
        /*
        if(isAttacking(7,"UP",types, atk) || isAttacking(7,"DOWN",types, atk) || 
           isAttacking(7,"RIGHT",types, atk) || isAttacking(7,"LEFT",types, atk)){
               return true;
           }
        */
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
        */
        types.remove("N");
        types.add("P");
        /*
        if(tomove.equals("w")){
            if(isAttacking(1,"NW",types, atk) || isAttacking(1,"NE",types, atk)){
                return true;
            }
        } else {
            if(isAttacking(1,"SW",types, atk) || isAttacking(1,"SE",types, atk)){
                return true;
            }
        }
        */
        return false;
    }

    private void UP(){
        dest -= 8;
        curr = dest;
        if (dest < 0){
            dest = 64;
        }
    }

    private void DOWN(){
        dest += 8;
        curr = dest;
        if (dest > 63){
            dest = 64;
        }
    }

    private void LEFT(){
        dest -= 1;
        x -= 1;
        curr = dest;
        if (x == -1){
            dest = 64;
        }
    }

    private void RIGHT(){
        dest += 1;
        x += 1;
        curr = dest;
        if (x == 8){
            dest = 64;
        }
    }

    private void NE(){
        dest -= 7;
        x += 1;
        curr = dest;
        if(x > 7 || dest < 0){
            dest = 64;
        }
    }

    private void NW(){
        dest -= 9;
        x -= 1;
        curr = dest;
        if(x < 0 || dest < 0){
            dest = 64;
        }
    }

    private void SE(){
        dest += 9;
        x += 1;
        curr = dest;
        if(x > 7 || dest > 63){
            dest = 64;
        }
    }

    private void SW(){
        dest += 7;
        x -= 1;
        curr = dest;
        if(x < 0 || dest > 63){
            dest = 64;
        }
    }

    private void NM(){
        dest = 0;
        curr = source + knight[i];
        if(curr < 0 || curr > 63 || Math.abs(((curr % 8) - x)) > 2){
            dest = 64;
        }
        i++;
    }

    private ArrayList<Integer> CreateList(int len, String fun){
        ArrayList<Integer> list = new ArrayList<Integer>();
        x = source % 8;
        int j = 0;
        dest = source;
        while(true && j < len){
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
            System.out.println("new curr");
            System.out.println(curr);
            if(dest == 64){
                break;
            }
            else if(BoardData.AllSquares[curr].emptySquare()){
                list.add(curr);
                j++;
            }
            else if(BoardData.AllSquares[curr].getSide().equals(opponent)){
                list.add(curr);
                break;
            }
            else{
                break;
            }
        }
        return list;
    }

    private boolean isAttacking(int len, String fun, ArrayList<String> types, int atk){
        x = atk % 8;
        int j = 0;
        dest = atk;
        System.out.println(fun);
        while(true && j < len){
            System.out.print("Iter : ");
            System.out.print(i);
            System.out.println();
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
            System.out.println(curr);
            if(dest == 64){
                return false;
            } 
            else if(BoardData.AllSquares[curr].emptySquare()&&BoardData.AllSquares[curr].isActive()){
                return false;
            }
            else if(!BoardData.AllSquares[curr].isActive() || (BoardData.AllSquares[curr].emptySquare())){
                j++;
                System.out.println("Inactive");
            }
            else if(BoardData.AllSquares[curr].getSide().equals(opponent)){
                System.out.println("Opponent");
                System.out.println(types);
                System.out.println(BoardData.AllSquares[curr].getType());
                if(types.contains(BoardData.AllSquares[curr].getType())){
                    System.out.println("Right type");
                    return true;
                } else {
                    System.out.println("Wrong type");
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return false;
    }



    private String getOpponent(int source){
        String opponent = "w";
        if(BoardData.AllSquares[source].getSide().equals("w")){
            opponent = "b";
        }
        return opponent;
    }
}
class EditorMovement extends Movement{
    @Override
    public ArrayList<Integer> GetMoves(int source){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0; i < 64; i++){
            list.add(i);
        }
        list.remove(source);
        return list;
    }
    @Override
    public void OtherSideToMove(){};
}