package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;

/**
 * LoginController is responsible for handling the login phase of the game.
 * It manages the transition from the login state to the ship-building stage
 * for the current player. This class plays a key role in ensuring proper
 * state progression and re-connection handling during the gameplay.
 */
public class LoginController extends Controller {


    /**
     * Constructs a LoginController instance to manage the login phase for a specific player and game.
     *
     * @param curPlayer The current player who is in the login phase.
     * @param gameId The unique identifier of the game session.
     */
    public LoginController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //this.disconnected = false;
    }

    /**
     * Transitions the current player to the next state in the game lifecycle.
     * Specifically, it changes the state from the login phase to the ship-building phase,
     * handles reconnection status, and assigns a new controller for the next phase.
     *
     * @param gc the {@code GameController} instance managing game-wide state and operations.
     */
    @Override
    public void nextState(GameController gc) {
        System.out.println("login change state for "+ curPlayer.GetID());
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
            setDisconnected(false);
        }

        curPlayer.setState(new BuildingShip());
        PrepController newController  = new PrepController(curPlayer, gameId, gc, getDisconnected());
        curPlayer.getCommonBoard().getHourglass().setListener(newController);
        newController.setExceptionListener(exceptionListener);
        newController.setVv(gc.getVirtualViewMap().get(curPlayer.GetID()));
        gc.setBuildingCount(1);
        gc.setControllerMap(curPlayer,newController);
    }
}