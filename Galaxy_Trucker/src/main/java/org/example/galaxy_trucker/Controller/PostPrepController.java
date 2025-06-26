package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

/**
 * PostPrepController handles the state of the game after the preparation phase,
 * allowing players to perform actions and transitioning to the next state when
 * certain conditions are met.
 *
 * This controller manages player interactions, command executions, and
 * handles default actions for disconnected players during this phase of the game.
 * It ensures synchronization during actions and performs error handling in case
 * of invalid operations or exceptions during command execution.
 *
 * Responsibilities:
 * - Tracks the count of housing units related to the current player's board.
 * - Validates and executes player commands if they are allowed in the current player state.
 * - Handles the default command execution for players who are either inactive or disconnected.
 * - Manages transitions from the current state to the next game state (FlightController).
 *
 * Constructor:
 * - Initializes the controller for a specific player, game session, and their connection status.
 *
 * Methods:
 * - action(Command command, GameController gc): Executes a player command if valid.
 *   Handles errors during execution and enforces transition to the next state when conditions are met.
 * - DefaultAction(GameController gc): Executes default actions for the player, primarily useful
 *   for inactive or disconnected players, with error handling in place.
 * - nextState(GameController gc): Transitions the current player to the next game state,
 *   instantiating a new controller for the subsequent phase and updating the game context.
 */
public class PostPrepController extends Controller {

    private int count;
    private boolean disconnected;

    /**
     * Constructs a new PostPrepController instance.
     *
     * @param curPlayer the current player for whom the controller is being created
     * @param gameId the unique identifier of the game
     * @param disconnected indicates whether the player is disconnected
     */
    public PostPrepController(Player curPlayer, String gameId, boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        this.disconnected = disconnected;
        count = curPlayer.getmyPlayerBoard().getHousingUnits().size();
        System.out.println("Count for "+ curPlayer.GetID() + " : " + count);
    }

    /**
     * Executes an action based on the provided {@link Command} within the current game state.
     * This method ensures the command is allowed in the player's current state,
     * executes it, and handles any exceptions that may occur.
     *
     * @param command The {@link Command} to be executed. It contains information such as game ID,
     *                player ID, and the specific action to be performed.
     * @param gc      The {@link GameController} instance managing the game state and flow.
     */
    @Override
    public synchronized void action(Command command, GameController gc) {/// devo fare la default anche quas
        System.out.println("POST_PREP_CONTROLLER");
        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            sendException(new IllegalStateException("You can only add aliens or humans!"));
            //throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }
        else{
            try {
                System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
                command.execute(curPlayer);
                count --;
                System.out.println("Count for "+ curPlayer.GetID() + " : " + count);
                if (count == 0) {
                    System.out.println("Changing state");
                    nextState(gc);
                }

            } catch (Exception e) {
                curPlayer.setMyPlance(playerBoardCopy);
                playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
                sendException(e);
                //throw new IllegalCallerException("illegal execution of command" + command.toString());
                e.printStackTrace();
            }
        }



    }


    /**
     * Executes the default action for the current player in the game, transitioning to the next state if necessary.
     * This method ensures the player's default command is executed while handling errors or interruptions.
     *
     * @param gc the GameController instance managing the game state and flow
     * @throws ImpossibleActionException if there is an error during the execution of the default command
     * @throws RuntimeException if an interruption occurs during the execution
     */
    @Override
    public  void  DefaultAction(GameController gc) {  
        PlayerState state = curPlayer.getPlayerState();
        Command cmd =state.createDefaultCommand(gameId,curPlayer);
        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!curPlayer.GetHasActed() ) { //has acted non dovrebbe servire nelle azioni non automatiche, potrebbe anche non servire in generale tbh
            try {
                this.curPlayer.SetHasActed(true);
                System.out.println("DefaultAction called for " + curPlayer.GetID()+ " disconnected: " + disconnected);
                /// forse potrei fare il controllo che sia != null anche se dovrebbe esssere ridondante
                cmd.execute(curPlayer);
                count = 0;
                // curPlayer.SetReady(true);
                if (count == 0) {
                    System.out.println("Changing state");
                    if(gc != null){
                        nextState(gc);
                    }
                }

            } catch (IOException e) {
                playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
                curPlayer.setMyPlance(playerBoardCopy);
                // this.curPlayer.SetHasActed(false);
                throw new ImpossibleActionException("errore nelle azioni di default :)");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * Transitions the game to the next state for the current player.
     * If the player is not disconnected, it ensures the player remains active,
     * updates the flight count, and sets up a new FlightController for the player.
     *
     * @param gc the {@link GameController} instance managing the game state and flow
     */
    @Override
    public void nextState(GameController gc) {
        System.out.println("Calling nextState for player: "+ curPlayer.GetID());
        if (!gc.getVirtualViewMap().get(curPlayer.GetID()).getDisconnected()){ ///  la virtual view sa sempre se è disconnesso, questo è il caso in cui il player si sia riconnesso
            this.disconnected = false;
        }

        gc.setFlightCount(1);
        FlightController newController = new FlightController( curPlayer, gameId, gc,this.disconnected);
        newController.setExceptionListener(exceptionListener);
        gc.setControllerMap(curPlayer,newController);

    }
}
