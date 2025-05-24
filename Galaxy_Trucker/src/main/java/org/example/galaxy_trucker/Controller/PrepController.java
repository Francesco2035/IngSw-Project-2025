package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.HourGlassListener;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;

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
    public void action(Command cmd, GameController gc){
        System.out.println("PREP_CONTROLLER");
        super.action(cmd, gc);
    }


    @Override
    public void nextState(GameController gc) {
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
            this.disconnected = false;
        }
        synchronized (gc) {
            curPlayer.getCommonBoard().getHourglass().stopHourglass();
        }
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new AddCrewState());
            gc.setControllerMap(curPlayer,new PostPrepController(curPlayer, gameId,this.disconnected));
        }
        else{
            //se la nave non è valida tolgo il razzo dalla gameboard, va ancora sistemato il fatto del cambio di stato/comandi chiamabili
            synchronized (curPlayer.getCommonBoard()) {
                curPlayer.getCommonBoard().removePlayerAndShift(curPlayer);
            }

            gc.setControllerMap(curPlayer,new CheckValidityController(curPlayer, gameId,this.disconnected));
            curPlayer.setState(new CheckValidity());
        }


    }

    @Override
    public void onFinish() {
        //settiamo player a choosePosition
        //dove è acconsentito un solo tipo di comando che è la Finish Building
        //che setta curPlayer ready true

        synchronized (gc){
            gc.getGame().getPlayers().values()
                    .stream().filter(p -> !p.GetReady())
                    .forEach(p -> p.setState(new ChoosePosition()));
        }
    }
}
