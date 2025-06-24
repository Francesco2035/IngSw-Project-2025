package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.ExceptionListener;
import org.example.galaxy_trucker.Controller.Messages.ExceptionEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;


public abstract class Controller {



    //Command command;
    Player curPlayer;
    String gameId;
    PlayerBoard playerBoardCopy;
    private boolean disconnected;
    ExceptionListener exceptionListener;


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

    public synchronized void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public abstract void nextState(GameController gc);

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public void removeExceptionListener() {
        this.exceptionListener = null;
    } //TODO test

    public void sendException(Exception e) { //TODO test
        if (exceptionListener != null) {
            ExceptionEvent event = new ExceptionEvent(e.getMessage());
            exceptionListener.exceptionOccured(event);
        }
        else{
            System.out.println("Exception occurred but listener set as null");
        }
    }


    public synchronized boolean getDisconnected(){
        return disconnected;
    }


}
