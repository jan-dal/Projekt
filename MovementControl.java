import java.awt.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class MovementControl extends MouseAdapter{
    Movement move;
    private ImageGUI grid;
    private Point click, loc;
    private int zorder;


    public void setGrid(ImageGUI x){
        grid = x;
    }
    public void SetMovementlogic(Movement move){
        this.move = move;
        move.CalculateMoves();
    }

    @Override
    public void mousePressed(MouseEvent e){
        // Light up all legal moves for this piece
        PieceGUI square = (PieceGUI)e.getComponent();
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
            moveset = square.getPiece().getMoveset();

        // Lightup all squares 
            if(move instanceof ChessMovement && moveset != null){
                for(int k : moveset){
                    BoardData.get(k).MovingPossible();
                }
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent e){
        PieceGUI square = (PieceGUI)e.getComponent();
        if(!square.emptySquare()){
            square.NeutralSquare();
            movePiece(square, e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        PieceGUI square = (PieceGUI)e.getComponent();
        ArrayList<Integer> moveset;
        square.NeutralSquare();

        if(!square.emptySquare()){
            grid.setComponentZOrder(square, zorder);
            moveset = square.getPiece().getMoveset();
            if(moveset != null && moveset.contains(getcell(grid.getMousePosition()))){
                placePiece(square);
            } else {
                square.setLocation(loc);
            }

            if(move instanceof ChessMovement && moveset != null){
                for(int k : moveset){
                    BoardData.get(k).NeutralSquare();
                }
            }
        }
    }
    
    private void movePiece(PieceGUI square, MouseEvent e){
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

    private void placePiece(PieceGUI square){
        int pos = getcell(grid.getMousePosition());
        Piece source = square.getPiece();
        PieceGUI dest = BoardData.get(pos);

        BoardData.setMoved(source);
        source.setMoved(true);
        source.setPosition(pos);

        if(!dest.emptySquare()){
            dest.getPiece().setActive(false);
        }

        dest.SetPiece(source);

        square.RemovePiece();
        
        square.setLocation(loc);
        
        move.OtherSideToMove();
        move.CalculateMoves();
    }

    private int getcell(Point pos){

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
}
