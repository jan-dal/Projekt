import java.awt.event.*;

// Editor mode <-> Game mode 

public class Game implements ActionListener{
    boolean ingame;
    ChessGUI game;
    
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
        game = new ChessGUI();
        //Player player1 = new Player("Player1","w");
        //Player player2 = new Player("Player2","b");
        game.setupGUI(this);
        game.SetMovementLogic(new EditorMovement());
        ingame = false;
    }
    private void StartChessGame(){
        ChessMovement gamemove = new ChessMovement();
        gamemove.WhiteToMove();
        game.SetMovementLogic(gamemove);
        game.StartTimer();
    }
    private void StopChessGame(){
        game.StopTimer();
        game.SetMovementLogic(new EditorMovement());
    }
}