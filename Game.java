import java.awt.event.*;

// Editor mode <-> Game mode 

public class Game implements ActionListener{
    private boolean ingame;
    private ChessGUI game;
    private MovementControl eventlistener;
    
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
        eventlistener = new MovementControl();
        eventlistener.SetMovementlogic(new EditorMovement());
        game = new ChessGUI();
        game.setupGUI(this, eventlistener);
        ingame = false;
    }
    private void StartChessGame(){
        ChessMovement gamemove = new ChessMovement();
        gamemove.WhiteToMove();
        eventlistener.SetMovementlogic(gamemove);
        game.getInfobox().getTimer1().StartTimer();
        game.getInfobox().getTimer2().StartTimer();
    }
    private void StopChessGame(){
        game.getInfobox().getTimer1().ResetTimer();
        game.getInfobox().getTimer2().ResetTimer();
        eventlistener.SetMovementlogic(new EditorMovement());
    }
}