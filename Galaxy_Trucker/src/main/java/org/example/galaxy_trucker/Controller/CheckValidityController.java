package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

public class CheckValidityController extends Controller{


    public CheckValidityController( Player player, String gameId,boolean disconnected) {
        this.gameId = gameId;
        this.curPlayer = player;
    }

    @Override
    public synchronized void action(Command command, GameController gc) { //  devo overridare anche qui ok

        System.out.println("CHECK_CONTROLLER");

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            sendException(new IllegalStateException("Command not accepted, you can only remove tile!"));
            //throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }else{
            try {
                System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
                command.execute(curPlayer);
                if(gc != null){
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


    @Override
    public void nextState(GameController gc) {
        System.out.println("CHECK_CONTROLLER callign next state for " + curPlayer.GetID());
        if (curPlayer.getmyPlayerBoard().checkValidity()) {
            curPlayer.setState(new ChoosePosition());
            ChoosePositionController newController = new ChoosePositionController(curPlayer, gameId,disconnected);
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer,newController);

        }
        else {
            curPlayer.setState(new CheckValidity());
            CheckValidityController newController = new CheckValidityController(curPlayer,gameId,disconnected);
            newController.setExceptionListener(exceptionListener);
            gc.setControllerMap(curPlayer, newController);
        }
    }
}
