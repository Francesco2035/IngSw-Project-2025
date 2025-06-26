package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

/**
 * The CheckValidityController class is responsible for handling game actions specific to the
 * "Check Validity" state of a Player. It manages commands and transitions between states
 * based on the validity of player's actions and game state.
 * Extends the base Controller class.
 */
public class CheckValidityController extends Controller{


    /**
     * Constructs a CheckValidityController instance to manage the current player's game state and validity checks.
     *
     * @param player       The player for whom the validity is being checked.
     * @param gameId       The identifier of the game session.
     * @param disconnected Indicates whether the player is currently disconnected from the game.
     */
    public CheckValidityController( Player player, String gameId,boolean disconnected) {
        this.gameId = gameId;
        this.curPlayer = player;
    }

    /**
     * Processes the given command by checking its validity, executing it, and transitioning the game
     * to the next state if applicable. Handles exceptions that may occur during command execution.
     *
     * @param command the command to be processed
     * @param gc the game controller to manage game transitions; can be null
     */
    @Override
    public synchronized void action(Command command, GameController gc) {

        System.out.println("CHECK_CONTROLLER");

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            sendException(new IllegalStateException("Command not accepted, you can only remove tile!"));
            //throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }else{
            try {
                System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
                command.execute(curPlayer);
                if(gc != null){ // l'if non vine raggiunto per qualche motivo
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
     * Executes the default action for the current player in the game.
     * This method verifies the player's state and performs the appropriate operations
     * if the player has not yet acted.
     *
     * @param gc The GameController instance managing the game state and logic.
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
                /// forse potrei fare il controllo che sia != null anche se dovrebbe essere ridondante
                cmd.execute(curPlayer);
                nextState(gc);

            } catch (IOException e) {
                playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
                curPlayer.setMyPlance(playerBoardCopy);
                // this.curPlayer.SetHasActed(false);
                throw new ImpossibleActionException("DefaultAction chiamata impossibile da eseguire nel CheckValidityController");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * Handles the transition of the player's state in the game based on the validity of their current state.
     *
     * If the player's state is valid, it transitions to the "ChoosePosition" state and initializes a
     * new {@code ChoosePositionController}. Otherwise, it transitions to the "CheckValidity" state
     * and initializes a new {@code CheckValidityController}.
     *
     * @param gc the {@code GameController} instance managing the current state and functionality of the game
     */
    @Override
    public void nextState(GameController gc) {
        System.out.println("CHECK_CONTROLLER callign next state for " + curPlayer.GetID());
        if (curPlayer.getmyPlayerBoard().checkValidity()) {
            curPlayer.setState(new ChoosePosition());
            ChoosePositionController newController = new ChoosePositionController(curPlayer, gameId,getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer,newController);

        }
        else {
            curPlayer.setState(new CheckValidity());
            CheckValidityController newController = new CheckValidityController(curPlayer,gameId,getDisconnected());
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer, newController);
        }
    }
}
