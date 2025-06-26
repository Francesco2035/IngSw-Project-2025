package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.ControllerHourGlassListener;
import org.example.galaxy_trucker.ClientServer.Messages.HourglassEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;

/**
 * The PrepController class represents the preparation phase in the game's lifecycle.
 * It extends the Controller class and implements the ControllerHourGlassListener interface
 * to handle specific events and states related to the preparation phase of a player.
 * During this phase, players configure or modify their game resources and transition to
 * subsequent states based on their setup's validity.
 */
public class PrepController extends Controller implements ControllerHourGlassListener {

    private VirtualView vv;

    /**
     * Sets the VirtualView instance for this controller.
     *
     * @param vv the VirtualView instance to be set
     */
    public void setVv(VirtualView vv) {
        this.vv = vv;
    }

    /**
     * Represents the game controller associated with the current game session.
     * This variable is used to coordinate and manage the game's progression, state, and related interactions
     * within the context of the PrepController.
     */
    GameController gc;
    /**
     * Constructor for the PrepController class responsible for initializing and setting up the game state
     * for the preparation phase.
     *
     * @param currentPlayer The current player object that represents the player in the game.
     * @param gameId The unique identifier for the game session.
     * @param gc The GameController instance managing the game logic.
     * @param disconnected A boolean flag indicating if the player is disconnected.
     */
    public PrepController(Player currentPlayer, String gameId, GameController gc, boolean disconnected) {
        curPlayer = currentPlayer;
        this.gameId = gameId;
        this.gc = gc;
        System.out.println("Prep Controller " + gameId + " - " + curPlayer);
        this.playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        //this.disconnected = disconnected;
    }

    /**
     * Performs an action based on the provided command in the given game controller context.
     *
     * @param cmd the command object containing information about the action to be performed
     * @param gc the game controller that manages the game state and orchestrates interactions
     */
    @Override
    public void action(Command cmd, GameController gc){
        System.out.println("PREP_CONTROLLER");
        super.action(cmd, gc);
    }


    /**
     * Transitions the game to the next state based on the current player's actions and game board validity.
     * Handles reconnection scenarios, validates the player's board state, and updates the game controller
     * with the appropriate controller for the next game phase.
     *
     * @param gc the GameController instance that manages the overall game state and interactions
     */
    @Override
    public void nextState(GameController gc) {
        System.out.println("called next state prepcontroller");
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
            setDisconnected(false);
        }
        synchronized (gc) {
            curPlayer.getCommonBoard().getHourglass().stopHourglass();
        }
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new AddCrewState());
            PostPrepController newController = new PostPrepController(curPlayer, gameId,getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer,newController);
        }
        else{  
            //se la nave non è valida tolgo il razzo dalla gameboard, va ancora sistemato il fatto del cambio di stato/comandi chiamabili
            synchronized (curPlayer.getCommonBoard()) {
                curPlayer.getCommonBoard().removePlayerAndShift(curPlayer);
            }

            CheckValidityController newController = new CheckValidityController(curPlayer, gameId, getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer,newController);
            curPlayer.setState(new CheckValidity());
        }


    }

    /**
     * Triggered when the hourglass timer finishes. This method is responsible
     * for transitioning players who have not marked themselves as ready to
     * a state where they can only issue the "Finish Building" command.
     *
     * The method performs the following actions:
     * - Prints a log message indicating that the hourglass has finished.
     * - Synchronizes on the game controller instance to ensure thread safety.
     * - Iterates over all players in the game.
     * - For each player who is not ready (checked via the `GetReady` method),
     *   transitions their state to `ChoosePosition` using the*/
    @Override
    public void onFinish() {  

        System.out.println("Hourglass finish ");
        //settiamo player a choosePosition
        //dove è acconsentito un solo tipo di comando che è la Finish Building
        //che setta curPlayer ready true

        synchronized (gc){
            gc.getGame().getPlayers().values()
                    .stream().filter(p -> !p.GetReady())
                    .forEach(p -> p.setState(new ChoosePosition()));
        }
    }

    /**
     * Updates the state or logic of the controller based on the provided hourglass event.
     * Typically used to handle specific timing or state changes related to an hourglass event in the game.
     *
     * @param event the HourglassEvent containing information about the event,
     *              including a message and whether the hourglass has started.
     */
    @Override
    public void hourglassUpdate(HourglassEvent event) {
        vv.sendHourglass(event);
    }
}
