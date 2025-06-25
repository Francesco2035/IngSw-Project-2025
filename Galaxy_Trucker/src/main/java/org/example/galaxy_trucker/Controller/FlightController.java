package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

/**
 * The FlightController class is a specific implementation of the Controller class
 * that represents a phase in the game where a player transitions through the "Flight" state.
 * This controller handles commands and transitions associated with the flight mechanics
 * or phase, and determines the game's behavior during this part of the game flow.
 */
public class FlightController extends Controller {


    /**
     * Constructs a FlightController instance, which manages the "Flight" phase for a specific player
     * within the game. This controller handles transitions and player states during the Flight phase
     * and interacts with the overarching GameController for phase management.
     *
     * @param curPlayer     The player currently involved in the Flight phase.
     * @param gameId        The unique identifier for the game instance.
     * @param gc            An instance of the GameController coordinating the game flow.
     * @param disconnected  A boolean indicating whether the player is currently disconnected.
     */
    public FlightController(Player curPlayer, String gameId, GameController gc,boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        //curPlayer.setState(new BaseState());
        //this.disconnected = disconnected;
    }

    /**
     * Executes the action logic for the FlightController, printing a debug message
     * and delegating the execution to the parent class's action method.
     *
     * @param cmd the command to be processed, representing a specific player action or state change
     * @param gc the game controller instance, managing the overall game state and operations
     */
    @Override
    public void action(Command cmd, GameController gc){
        System.out.println("FLIGHT_CONTROLLER");
        super.action(cmd, gc);
    }


    /**
     * Transitions the game state from the current "Flight" state to the appropriate
     * next state by updating the relevant controller. Ensures that the player's
     * connection status is verified and handled, and initializes a new controller
     * for the subsequent game phase.
     *
     * @param gc the GameController instance that manages the overall game flow
     *           and state transitions across multiple controllers.
     */
    @Override
    public void nextState(GameController gc) {
        System.out.println("Calling next state in fc for :" +curPlayer.GetID());
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
           setDisconnected(false);
        }
        CardsController newController = new CardsController(curPlayer, gameId, getDisconnected());
        newController.setExceptionListener(exceptionListener);
        gc.setControllerMap(curPlayer,newController);
    }
}
