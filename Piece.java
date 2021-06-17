import java.util.*;

public class Piece implements Comparable<Piece>{
    String Type, Side;
    ArrayList<Integer> moveset;
    int Position, Value;
    boolean Moved, active;

    public Piece(String type, String side, int position, boolean lasttomove, boolean moved){
        Type = type;
        Side = side;
        Position = position;
        active = true;
        moveset = null;
        Moved = moved;
        giveValue();
    }  

// Getters
    public String getType(){
        return Type;
    }

    public String getSide(){
        return Side;
    }
    
    public int getValue(){
        return Value;
    }

    public int getPosition(){
        return Position;
    }

    public boolean getActive(){
        return active;
    }

    public boolean Moved(){
        return Moved;
    }

    public ArrayList<Integer> getMoveset(){
        return moveset;
    }

// Setters
    public void setSide(String side){
        Side = side;
    }

    public void setMoved(boolean x){
        Moved = x;
    }

    public void setPosition(int pos){
        Position = pos;
    }

    public void setMoveset(ArrayList<Integer> l){
        moveset = l;
    }

    public void setActive(boolean x){
        active = x;
    }


    @Override
    public int compareTo(Piece o) {
        if(Value > o.getValue()){
            return -1;
        } else if(Value == o.getValue()){
            return 0;
        } else {
            return 1;
        }
    }

    private void giveValue(){
        switch(Type){
            case "P" : Value = 1;
            break;
            case "N" : Value = 3;
            break;
            case "B" : Value = 3;
            break;
            case "R" : Value = 5;
            break;
            case "Q" : Value = 9;
            break;
            case "K" : Value = 100;
            break;
        }
    }
}
