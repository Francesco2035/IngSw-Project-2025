package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
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
    boolean disconnected;
    ExceptionListener exceptionListener;


    public synchronized void action(Command command, GameController gc) {

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())) {
            sendException(new IllegalStateException("You can't do this command!"));
            //throw new IllegalStateException("Command not accepted: " + command.getClass() + " \n" + curPlayer.getPlayerState());
        }

        try {
            System.out.println("Action called for " + gameId + ": " + command.getTitle() + " " + command.playerId);
            command.execute(curPlayer);
             gc.changeState(); ///da scommentare era per fare i tests skskk
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
       // this.curPlayer.SetHasActed(true);
    }

    public  void  DefaultAction(GameController gc) {
       PlayerState state = curPlayer.getPlayerState();
       Command cmd =state.createDefaultCommand(gameId,curPlayer);
       playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
       if (!curPlayer.GetHasActed()) { //has acted non dovrebbe servire nelle azioni non automatiche, potrebbe anche non servire in generale tbh
           try {
               this.curPlayer.SetHasActed(true);
               System.out.println("DefaultAction called for " + curPlayer.GetID());

               cmd.execute(curPlayer);
              // curPlayer.SetReady(true);
               gc.changeState();
           } catch (IOException e) {
               playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
               curPlayer.setMyPlance(playerBoardCopy);
              // this.curPlayer.SetHasActed(false);
               throw new ImpossibleActionException("errore nelle azioni di default :)");
           }
       }

    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public abstract void nextState(GameController gc);

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public void removeExceptionListener() {
        this.exceptionListener = null;
    }

    public void sendException(Exception e) {
        if (exceptionListener != null) {
            ExceptionEvent event = new ExceptionEvent(e.getMessage());
            exceptionListener.exceptionOccured(event);
        }
        else{
            System.out.println("Exception occured but listener not set");
        }
    }

}
