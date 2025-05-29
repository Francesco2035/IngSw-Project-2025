package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;

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
            throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }

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
