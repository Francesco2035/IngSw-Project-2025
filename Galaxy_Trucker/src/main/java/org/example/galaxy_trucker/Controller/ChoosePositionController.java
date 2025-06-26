package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

/**
 * The ChoosePositionController class is a specific implementation of the abstract Controller
 * class. It handles the logic for the "Choose Position" phase in the game, allowing players
 * to select their position on the game board. The controller ensures that commands executed
 * during this phase are valid according to the player's current state.
 */
public class ChoosePositionController extends Controller{


    /**
     * Constructor for the ChoosePositionController class.
     *
     * @param player       The player currently making a decision or whose turn it is.
     * @param gameId       The unique identifier of the game.
     * @param disconnected A flag indicating whether the player is currently disconnected.
     */
    public ChoosePositionController(Player player, String gameId, boolean disconnected) {
        this.curPlayer = player;
        this.gameId = gameId;
    }


    /**
     * Executes an action on the current player based on the given command and game controller context.
     * This method processes a player's command, validates its state, executes it if allowable,
     * and transitions the game to the next state if successful. In case of an error, the player's state
     * is reverted, and the exception is sent back to the client.
     *
     * @param command The command issued by the player to be executed.
     * @param gc The game controller that handles the game state and flow.
     */
    @Override
    public synchronized void action(Command command, GameController gc) {  //  devo overridare anche qui ok

        System.out.println("ChoosePositionController action");

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            sendException(new IllegalStateException("Command not accepted, you can only choose a position!"));
        }
        else{
            try {
                System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
                command.execute(curPlayer);
                nextState(gc);
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
     * Executes the default action for the current player in the game.
     * This method interacts with the game controller to perform a player's default move,
     * updating game state and handling any errors that might occur during execution.
     *
     * @param gc the game controller instance that manages the game's flow and state
     */
    @Override
    public  void  DefaultAction(GameController gc) {
        PlayerState state = curPlayer.getPlayerState();
        Command cmd =state.createDefaultCommand(gameId,curPlayer);
        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!curPlayer.GetHasActed()) { //has acted non dovrebbe servire nelle azioni non automatiche, potrebbe anche non servire in generale tbh
            try {
                this.curPlayer.SetHasActed(true);
                System.out.println("DefaultAction called for " + curPlayer.GetID());
                /// forse potrei fare il controllo che sia != null anche se dovrebbe esssere ridondante
                cmd.execute(curPlayer);
                nextState(gc);

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
     * Transitions the current state to the next state in the game's flow.
     *
     * @param gc the GameController object responsible for managing game logic
     */
    @Override
    public void nextState(GameController gc) {
        curPlayer.setState(new AddCrewState());
        PostPrepController newController = new PostPrepController(curPlayer,gameId,getDisconnected());
        newController.setExceptionListener(exceptionListener);
        gc.setControllerMap(curPlayer, newController);
    }
}
