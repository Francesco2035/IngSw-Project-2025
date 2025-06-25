package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.ExceptionListener;
import org.example.galaxy_trucker.Controller.Messages.ExceptionEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;


/**
 * The Controller class is an abstract representation of the game's state management and player interaction.
 * It handles game actions, transitions between states, and manages interactions with exceptions
 * and player connection statuses. Each concrete implementation of this class defines specific
 * behaviors for different phases or mechanics of the game.
 */
public abstract class Controller {



    //Command command;
    Player curPlayer;
    String gameId;
    PlayerBoard playerBoardCopy;
    private boolean disconnected;
    ExceptionListener exceptionListener;


    /**
     * Executes the provided command for the current player and manages the state transition
     * in the game controller. If the command is not allowed in the player's current state,
     * an exception is sent to the exception listener. If an error occurs during the command
     * execution, the player's board is rolled back to its previous state, and an exception
     * is sent to the exception listener.
     *
     * @param command The command to be executed. This determines the action to be performed by the player.
     * @param gc The game controller responsible for changing the state after the command is executed.
     */
    public synchronized void action(Command command, GameController gc) { // è realmente necessario avere questa action qui? non è sempre overridata?

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())) {
            sendException(new IllegalStateException("You can't do this command!"));
            //throw new IllegalStateException("Command not accepted: " + command.getClass() + " \n" + curPlayer.getPlayerState());
        }

        else{
            try {
                System.out.println("Action called for " + gameId + ": " + command.getTitle() + " " + command.playerId);
                command.execute(curPlayer);
                gc.changeState();
            } catch (Exception e) {
                playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
                playerBoardCopy.setRewardsListener(curPlayer.getmyPlayerBoard().getRewardsListener());
                if (playerBoardCopy.getRewardsListener() == null) {
                    System.out.println("No rewards listener available mannaggia la democrazia cristiana");
                }
                curPlayer.setMyPlance(playerBoardCopy);
                e.printStackTrace();
                sendException(e);
                //throw new IllegalCallerException("illegal execution of command" + command.toString());
            }
        }


       // this.curPlayer.SetHasActed(true);
    }

    /**
     * Executes the default action for the current player based on their state and the provided game controller.
     *
     * @param gc the game controller to manage the game state and execute transitions; it may be null,
     *           in which case certain operations will be handled differently
     */
    public void DefaultAction(GameController gc) { //TODO test
       PlayerState state = curPlayer.getPlayerState();
       Command cmd =state.createDefaultCommand(gameId,curPlayer);
       playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
       System.out.println("def action "+ this.getClass().getSimpleName()+ " "+ curPlayer.GetHasActed()+ " " +curPlayer.getPlayerState().getClass().getSimpleName());
       if (cmd != null && !curPlayer.GetHasActed()) { //has acted non dovrebbe servire nelle azioni non automatiche, potrebbe anche non servire in generale tbh
           try {

               this.curPlayer.SetHasActed(true);

               if(gc ==null){
                   Thread.sleep(50);
               }
               else{
                   Thread.sleep(4000);
               }

               System.out.println("DefaultAction called for " + curPlayer.GetID()+ " disconnected: " + disconnected);
                /// forse potrei fare il controllo che sia != null anche se dovrebbe esssere ridondante
               cmd.execute(curPlayer);
               //this.curPlayer.SetHasActed(true);
              // curPlayer.SetReady(true);
               if(gc!= null){
                   System.out.println("changing state...");
                   gc.changeState();
               }
           } catch (IOException e) {
               System.out.println(e);
               playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
               curPlayer.setMyPlance(playerBoardCopy);
               this.curPlayer.SetHasActed(false);
               throw new ImpossibleActionException("errore nelle azioni di default :)");
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           System.out.println("def action finished dentro l'if "+ this.getClass().getSimpleName()+ " "+ curPlayer.GetHasActed() +  " " +curPlayer.getPlayerState().getClass().getSimpleName());

       }
        System.out.println("def action finished fuori if  "+ this.getClass().getSimpleName()+ " "+ curPlayer.GetHasActed() +  " " +curPlayer.getPlayerState().getClass().getSimpleName());

    }

    /**
     * Sets the disconnected state of the controller.
     *
     * @param disconnected a boolean indicating whether the controller is disconnected (true)
     *                     or connected (false).
     */
    public synchronized void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    /**
     * Advances the game to its next state by processing the current game context and
     * determining the appropriate transition.
     *
     * @param gc the GameController instance managing the current game state and actions
     */
    public abstract void nextState(GameController gc);

    /**
     * Sets the ExceptionListener to handle exception events.
     *
     * @param exceptionListener the ExceptionListener instance to be set. This listener will handle
     *                          exception events generated within the system.
     */
    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    /**
     * Removes the current exception listener, if any, by setting it to null.
     * This effectively disables the handling of exceptions via the exception listener mechanism.
     */
    public void removeExceptionListener() {
        this.exceptionListener = null;
    } //TODO test

    /**
     * Sends an exception event through the exception listener if it is not null.
     * If the listener is null, logs a message indicating the absence of a listener.
     *
     * @param e the exception to be processed and sent to the listener
     */
    public void sendException(Exception e) { //TODO test
        if (exceptionListener != null) {
            ExceptionEvent event = new ExceptionEvent(e.getMessage());
            exceptionListener.exceptionOccured(event);
        }
        else{
            System.out.println("Exception occurred but listener set as null");
        }
    }


    /**
     * Retrieves the disconnected state of the controller.
     *
     * @return true if the controller is currently in a disconnected state, false otherwise.
     */
    public synchronized boolean getDisconnected(){
        return disconnected;
    }


}
