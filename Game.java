import java.awt.event.*;

// Editor mode <-> Game mode 

public class Game implements ActionListener{
    private boolean ingame;
    private ChessGUI game;
    
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if (command.equals("Save")){
            System.out.println("Saved");
        }
        if (command.equals("Load")){
            System.out.println("Loading...");
        }
        if (command.equals("Start game")){
            if(!ingame){
                StartChessGame();
                ingame = true;
            }
        }
        if (command.equals("Stop game")){
            if(ingame){
                StopChessGame();
                ingame = false;
            }
        }

    }
    public Game(){
        BoardData.setBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        //Player player1 = new Player("Player1","w");
        //Player player2 = new Player("Player2","b");
        game = new ChessGUI();
        game.setupGUI(this);
        EditorMovementControl k = new EditorMovementControl();
        k.setGrid(game.getChessboard().getGrid());
        SetMovementControl(k);
        ingame = false;
    }
    private void StartChessGame(){
        GameMovementControl k = new GameMovementControl();
        k.setToMove("w");
        k.setGrid(game.getChessboard().getGrid());
        k.CalculateAllMoves();
        SetMovementControl(k);
        game.getInfobox().getTimer1().setTimer();
        game.getInfobox().getTimer2().setTimer();
        game.getInfobox().getTimer1().StartTimer();
        game.getInfobox().getTimer2().StartTimer();
    }
    private void StopChessGame(){
        EditorMovementControl k = new EditorMovementControl();
        k.setGrid(game.getChessboard().getGrid());
        game.getInfobox().getTimer1().ResetTimer();
        game.getInfobox().getTimer2().ResetTimer();
        SetMovementControl(k);
    }

    private void SetMovementControl(MovementControl m){
        for(SquareGUI e : BoardData.getChessboard()){

            for(MouseListener i : e.getMouseListeners()){
                e.removeMouseListener(i);
            }
            for(MouseMotionListener i : e.getMouseMotionListeners()){
                e.removeMouseMotionListener(i);
            }
            e.addMouseListener(m);
            e.addMouseMotionListener(m);
        }
    }
}