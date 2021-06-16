import javax.swing.*;
import java.awt.*;
// P - Pawn
// B - Bishop
// N - Knight
// R - Rook
// Q - Queen
// K - King


public class Piece extends JLabel{
    String Type, Side;
    boolean active, moved;

    public Piece(String Type, String Side){
        this.Type = Type;
        this.Side = Side;
        active = true;
        moved = false;
        this.setOpaque(false);
        this.setIcon(new ImageIcon("img/Chess_" + Type + Side + ".png"));
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }
    public Piece(){
    // For a future piece
        Type = null;
        Side = null;
        active = false;
        moved = false;
        this.setOpaque(false);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }
    public void SetPiece(String Type, String Side){
        this.Type = Type;
        this.Side = Side;
        moved = true;
        active = true;
        this.setOpaque(false);
        this.setIcon(new ImageIcon("img/Chess_" + Type + Side + ".png"));
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    public void Moved(){
        moved = true;
    }

    public boolean hasMoved(){
        return moved;
    }

    public boolean isActive(){
        return active;
    }

    public void Activate(){
        active = true;
    }

    public void Deactivate(){
        active = false;
    }

    public String getType(){
        return Type;
    }

    public String getSide(){
        return Side;
    }

    public void RemovePiece(){
        this.setIcon(null);
        this.Type = null;
        this.Side = null;
        active = false;
        moved = false;
        repaint();
    }

    public boolean emptySquare(){
        return Type == null;
    }

    public void AttackedPiece(){
        this.setOpaque(true);
        setBackground(new Color(188,75,81)); //Red
        repaint();
    }
    public void MovingPossible(){
        this.setOpaque(true);
        setBackground(new Color(255,255,153)); //Yellow
        repaint();
    }

    public void SourceSquare(){
        this.setOpaque(true);
        setBackground(new Color(255,255,112)); //Faint 
        repaint();
    }

    public void CurrentSquare(){
        this.setOpaque(true);
        setBackground(new Color(255,255,51)); // Strong yellow
        repaint();
    }

    public void NeutralSquare(){
        this.setOpaque(false);
        repaint();
    }

}