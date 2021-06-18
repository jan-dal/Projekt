import java.awt.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public abstract class MovementControl extends MouseAdapter{
    protected ImageGUI grid;
    protected Point click, loc;
    protected int zorder;

    // What happens when a piece is placed
    protected abstract void placePiece(SquareGUI square);
    protected void movePiece(SquareGUI square, MouseEvent e){
        // Piece move piece to location if legal && ingame else set back
        // Update BoardData 
        int thisX = square.getLocation().x;
        int thisY = square.getLocation().y;
    
        int xMoved = (thisX + e.getX()) - (thisX + click.x);
        int yMoved = (thisY + e.getY()) - (thisY + click.y);
    
        int X = thisX + xMoved;
        int Y = thisY + yMoved; 
        square.setLocation(X, Y);
    }

    protected int getcell(Point pos){

        if (pos != null){
            int gridX = grid.getWidth();
            int gridY = grid.getHeight();
            int x = (int)pos.getX();
            int y = (int)pos.getY();
    
            int mx = (int)(x*8 / gridX);
            int my = (int)(y*8 / gridY);
    
            return my*8 + mx;
        } else {
            return 0;
        }
    }

    public void setGrid(ImageGUI x){
        grid = x;
    }
}


class GameMovementControl extends MovementControl{
    String tomove;

    public boolean IsCheckmate(){
        if(tomove.equals("w")){
            for(Piece e : BoardData.getWhite()){
                if(e.getMovelist() != null && e.getMovelist().size() > 0){
                    return false;
                }
            }
        } else {
            for(Piece e : BoardData.getBlack()){
                if(e.getMovelist() != null && e.getMovelist().size() > 0){
                    return false;
                }
            }
        }
        return true;
    }

    public void CalculateAllMoves(){
        if(tomove.equals("w")){
            for(Piece e : BoardData.getWhite()){
                if(e.getActive()){
                    e.updateMovelist();
                }
            }
            for(Piece e : BoardData.getBlack()){
                e.setMovelist(null);
            }
        } else {
            for(Piece e : BoardData.getBlack()){
                if(e.getActive()){
                    e.updateMovelist();
                }
            }
            for(Piece e : BoardData.getWhite()){
                e.setMovelist(null);
            }
        }
        if(IsCheckmate()){
            System.out.println("Checkmated!");
            System.out.println(tomove);
        }
    }

    public void setToMove(String m){
        tomove = m;
    }

    public void nextToMove(){
        if(tomove.equals("w")){
            tomove = "b";
        } else {
            tomove = "w";
        }
    }


    @Override
    public void placePiece(SquareGUI square){
        int pos = getcell(grid.getMousePosition());
        Piece source = square.getPiece();
        SquareGUI dest = BoardData.get(pos);

        BoardData.setMoved(source);
        source.setMoved(true);
        source.setPosition(pos);

        if(!dest.emptySquare()){
            dest.getPiece().setActive(false);
        }

        dest.SetPiece(source);

        square.RemovePiece();
        
        square.setLocation(loc);
        
        nextToMove();
        CalculateAllMoves();

        // Other side to move
        // Calcuate moves in here
        //move.CalculateMoves();
    }

    @Override
    public void mousePressed(MouseEvent e){
        // Light up all legal moves for this piece
        SquareGUI square = (SquareGUI)e.getComponent();
        ArrayList<Integer> moveset;
        if(square.emptySquare()){
            square.SourceSquare();
        } 
        else {
        // Make the piece stand out
            square.SourceSquare();
            zorder = grid.getComponentZOrder(square);
            grid.setComponentZOrder(square, 0);


        // Gather information about location of the piece
            click = e.getPoint();
            loc = square.getLocation();
            moveset = square.getPiece().getMovelist();

        // Lightup all squares 
            if(moveset != null){
                for(int k : moveset){
                    BoardData.get(k).MovingPossible();
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        SquareGUI square = (SquareGUI)e.getComponent();
        if(!square.emptySquare()){
            square.NeutralSquare();
            movePiece(square, e);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e){
        SquareGUI square = (SquareGUI)e.getComponent();
        ArrayList<Integer> moveset;
        square.NeutralSquare();

        if(!square.emptySquare()){
            grid.setComponentZOrder(square, zorder);
            moveset = square.getPiece().getMovelist();
            if(moveset != null && moveset.contains(getcell(grid.getMousePosition()))){
                placePiece(square);
            } else {
                square.setLocation(loc);
            }

            if(moveset != null){
                for(int k : moveset){
                    BoardData.get(k).NeutralSquare();
                }
            }
        }
    }

}

class EditorMovementControl extends MovementControl{

    //TODO Add right click mouse event
    //TODO Remove piece enteierly

    @Override
    public void placePiece(SquareGUI square){
        int pos = getcell(grid.getMousePosition());
        Piece source = square.getPiece();
        SquareGUI dest = BoardData.get(pos);

        source.setPosition(pos);
        dest.SetPiece(source);
        square.RemovePiece();
        square.setLocation(loc);
    }

    @Override
    public void mousePressed(MouseEvent e){
        // Light up all legal moves for this piece
        SquareGUI square = (SquareGUI)e.getComponent();
        square.SourceSquare();
        if(!square.emptySquare()){

            zorder = grid.getComponentZOrder(square);
            grid.setComponentZOrder(square, 0);

        // Gather information about location of the piece
            click = e.getPoint();
            loc = square.getLocation();

        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        SquareGUI square = (SquareGUI)e.getComponent();
        if(!square.emptySquare()){
            square.NeutralSquare();
            movePiece(square, e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        SquareGUI square = (SquareGUI)e.getComponent();
        square.NeutralSquare();
        if(!square.emptySquare() && BoardData.get(getcell(grid.getMousePosition())).emptySquare()){
            grid.setComponentZOrder(square, zorder);
            placePiece(square);
        } else if(!square.emptySquare()){
            grid.setComponentZOrder(square, zorder);
            square.setLocation(loc);
        }
    }
}