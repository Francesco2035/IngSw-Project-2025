package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.Listeners.HourGlassListener;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;

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

        synchronized (gc) {
            curPlayer.getCommonBoard().getHourglass().stopHourglass();
        }

            if (curPlayer.getmyPlayerBoard().checkValidity()) {
                curPlayer.setState(new AddCrewState());
                gc.setControllerMap(curPlayer, new PostPrepController(curPlayer, gameId));
            } else {
                gc.setControllerMap(curPlayer, new CheckValidityController(curPlayer, gameId));
                curPlayer.setState(new CheckValidity());
            }

    }

    @Override
    public void onFinish() {
        //settiamo player a choosePosition
        //dove è acconsentito un solo tipo di comando che è la Finish Building
        //curPlayer.SetReady(true);
        //gc.changeState();

        synchronized (gc){
            gc.getGame().getPlayers().values()
                    .stream().filter(p -> !p.GetReady())
                    .forEach(p -> p.setState(new ChoosePosition()));
        }
    }
}
