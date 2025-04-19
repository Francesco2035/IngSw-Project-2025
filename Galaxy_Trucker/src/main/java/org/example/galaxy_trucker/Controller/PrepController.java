package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

public class PrepController extends Controller implements HourGlassListener {

    GameController gc;
    public PrepController(Player currentPlayer, String gameId, GameController gc) {
        curPlayer = currentPlayer;
        this.gameId = gameId;
        this.gc = gc;
        System.out.println("Prep Controller " + gameId + " - " + curPlayer);
    }

    @Override
    public void nextState(GameController gc) {
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new Waiting());
            gc.setFlightCount(1);
            gc.setControllerMap(curPlayer,new FlightController(curPlayer, gameId, gc));
        }
        gc.setControllerMap(curPlayer,new PostPrepController(curPlayer, gameId));
        curPlayer.setState(new CheckValidity());
    }

    @Override
    public void onFinish() {
        curPlayer.SetReady(true);
        gc.changeState();

    }
}
