package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

public class PrepController extends Controller implements HourGlassListener {

    GameController gc;
    public PrepController(Player currentPlayer, String gameId, GameController gc, boolean disconnected) {
        curPlayer = currentPlayer;
        this.gameId = gameId;
        this.gc = gc;
        System.out.println("Prep Controller " + gameId + " - " + curPlayer);
        this.playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        this.disconnected = disconnected;
    }

    @Override
    public void nextState(GameController gc) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new AddCrewState());
            gc.setControllerMap(curPlayer,new PostPrepController(curPlayer, gameId,this.disconnected));
        }
        else{
            gc.setControllerMap(curPlayer,new CheckValidityController(curPlayer, gameId,this.disconnected));
            curPlayer.setState(new CheckValidity());
        }
    }

    @Override
    public void onFinish() {
        //settiamo player a choosePosition
        //dove è acconsentito un solo tipo di comando che è la Finish Building
        //curPlayer.SetReady(true);
        //gc.changeState();
    }
}
