package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Player;

/**
 * The CardsController class extends the abstract Controller class, responsible for managing
 * the game actions and states related to the player's interaction with card-based mechanics.
 * It defines the specific transitions and updates needed for this phase of the game.
 */
public class CardsController extends Controller {


    /**
     * Constructs a CardsController instance to manage the card-related mechanics for a specific player
     * in the game, identified by the provided game ID.
     *
     * @param curPlayer the current player interacting with the card mechanics
     * @param gameId the unique identifier of the game session
     * @param disconnected indicates whether the player is currently disconnected
     */
    public CardsController(Player curPlayer, String gameId,boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //this.disconnected = disconnected;
    }

    /**
     * Transitions the game to the next state based on the current player's actions and
     * the status of the card stack. If the card stack is empty, the game will end. Otherwise,
     * it initializes a new FlightController to handle the next phase of the game.
     *
     * @param gc The GameController instance that manages the overall game flow and state transitions.
     */
    @Override
    public void nextState(GameController gc) {
//        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
//            this.setDisconnected(false);
//        }
        if (curPlayer.getCommonBoard().getCardStack().getFullAdventure().isEmpty()){
            gc.setGameOver();
        }

        else{
            FlightController newController = new FlightController(curPlayer, gameId, gc, getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setFlightCount(1);
            gc.setControllerMap(curPlayer,newController);
        }
    }
}
