package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

public class FlightController extends Controller {


    public FlightController(Player curPlayer, String gameId, GameController gc,boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //curPlayer.setState(new BaseState());
        //this.disconnected = disconnected;
    }

    @Override
    public void action(Command cmd, GameController gc){
        System.out.println("FLIGHT_CONTROLLER");
        super.action(cmd, gc);
    }


    @Override
    public void nextState(GameController gc) { //TODO test
        System.out.println("Calling next state in fc for :" +curPlayer.GetID());
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
           setDisconnected(false);
        }
        CardsController newController = new CardsController(curPlayer, gameId, getDisconnected());
        newController.setExceptionListener(exceptionListener);
        gc.setControllerMap(curPlayer,newController);
    }
}
