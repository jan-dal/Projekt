// Shows pieces on a chess BoardData
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class ChessBoardGUI extends JPanel{
    Image chessBoardData;
    ImageGUI grid;
    Point click, loc;
    ArrayList<Integer> moveset;
    // Only testing
    Movement move;
    int zorder;

    public ChessBoardGUI(){
        // Setup chessBoardData and sidebars images
        //this.setMinimumSize(new Dimension(800, 800));
        this.setPreferredSize(new Dimension(800, 800));
        this.setBackground(new Color(49,46,43));

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        ImageGUI bottombar = new ImageGUI("img/bottombar.png");
        bottombar.setPreferredSize(new Dimension(800,32));
        ImageGUI sidebar = new ImageGUI("img/leftbar.png");
        sidebar.setMaximumSize(new Dimension(32, 4000));
        sidebar.setPreferredSize(new Dimension(32,768));
        bottombar.setMinimumSize(new Dimension(0, 32));
        bottombar.setMaximumSize(new Dimension(4000,32));
        sidebar.setMinimumSize(new Dimension(32,0));

        JPanel contentpanel = new JPanel();
        contentpanel.setLayout(new BoxLayout(contentpanel, BoxLayout.X_AXIS));
        contentpanel.setBackground(new Color(49,46,43));
        
        this.add(contentpanel);
        this.add(bottombar);

        contentpanel.add(sidebar);

        setupPieces(contentpanel);
    }

    private void setupPieces(JPanel contentpanel){ 
        // Set up grid for pieces

        grid = new ImageGUI("img/chessboard.png");
        grid.setLayout(new GridLayout(8,8,0,0));


        MouseListener eventlistener = new MouseListener();

        for(Piece e : BoardData.AllSquares){
        // Add mouse listeners
            e.addMouseListener(eventlistener);
            e.addMouseMotionListener(eventlistener);
            // Populate grid with pieces
            grid.add(e);
        }

        contentpanel.add(grid);
    }

    public void SetMovementlogic(Movement move){
        this.move = move;
    }

    private void movePiece(Piece piece, MouseEvent e){

        // Piece move piece to location if legal && ingame else set back
        // Update BoardData 
        if(piece.getType() != null){
            int thisX = piece.getLocation().x;
            int thisY = piece.getLocation().y;
    
            int xMoved = (thisX + e.getX()) - (thisX + click.x);
            int yMoved = (thisY + e.getY()) - (thisY + click.y);
    
            int X = thisX + xMoved;
            int Y = thisY + yMoved; 
            piece.setLocation(X, Y);

            piece.repaint();
        }
    }

    private void placePiece(Piece piece){
        if(moveset.contains(getcell(grid.getMousePosition()))){
            BoardData.AllSquares[getcell(grid.getMousePosition())].SetPiece(piece.getType(),piece.getSide());
            piece.RemovePiece();
            move.OtherSideToMove();
        }
        grid.setComponentZOrder(piece, zorder);
        piece.setLocation(loc);
        piece.repaint();
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

    // Mouse listener
    private class MouseListener extends MouseAdapter{
        @Override
        public void mouseDragged(MouseEvent e){
            Piece piece = (Piece)e.getComponent();
            piece.NeutralSquare();
            if(!piece.emptySquare()){
                movePiece(piece, e);
            }
        }
    
        @Override
        public void mousePressed(MouseEvent e){
            // Light up all legal moves for this piece
            Piece piece = (Piece)e.getComponent();
            piece.SourceSquare();
            if(!piece.emptySquare()){
                zorder = grid.getComponentZOrder(piece);
                grid.setComponentZOrder(piece, 0);
            }
            
            click = e.getPoint();
            loc = piece.getLocation();
            moveset = move.GetMoves(getcell(grid.getMousePosition()));
            if(move instanceof ChessMovement && moveset.size() > 0){
                for(int k : moveset){
                    BoardData.AllSquares[k].MovingPossible();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
            Piece piece = (Piece)e.getComponent();
            piece.NeutralSquare();
            if(!piece.emptySquare()){
                placePiece(piece);
            }
            if(move instanceof ChessMovement && moveset.size() > 0){
                for(int k : moveset){
                    BoardData.AllSquares[k].NeutralSquare();
                }
            }
        }
    }
}