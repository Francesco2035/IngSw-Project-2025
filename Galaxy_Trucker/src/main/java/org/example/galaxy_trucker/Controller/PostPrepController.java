package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;

public class PostPrepController extends Controller {

    private int count;
    private boolean disconnected;

    public PostPrepController(Player curPlayer, String gameId, boolean disconnected) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        this.disconnected = disconnected;
        count = curPlayer.getmyPlayerBoard().getHousingUnits().size();
        System.out.println("Count for "+ curPlayer.GetID() + " : " + count);
    }

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


    @Override
    public  void  DefaultAction(GameController gc) {
        PlayerState state = curPlayer.getPlayerState();
        Command cmd =state.createDefaultCommand(gameId,curPlayer);
        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!curPlayer.GetHasActed()) { //has acted non dovrebbe servire nelle azioni non automatiche, potrebbe anche non servire in generale tbh
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
