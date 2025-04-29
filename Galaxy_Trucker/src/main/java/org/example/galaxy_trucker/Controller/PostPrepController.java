package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.CheckValidity;

public class PostPrepController extends Controller {

    private int count;

    public PostPrepController(Player curPlayer, String gameId) {
        this.curPlayer = curPlayer;
        this.gameId = gameId;
        count = curPlayer.getmyPlayerBoard().getHousingUnits().size();
    }

    @Override
    public synchronized void action(Command command, GameController gc,boolean disconnected) {

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }

        try {
            System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
            command.execute(curPlayer);
            count --;
            if (count == 0) {
                nextState(gc);
            }

        } catch (Exception e) {
            curPlayer.setMyPlance(playerBoardCopy);
            playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());

            //throw new IllegalCallerException("illegal execution of command" + command.toString());
            System.out.println(e);
        }

    }


    @Override
    public void nextState(GameController gc) {

        gc.setFlightCount(1);
        gc.setControllerMap(curPlayer, new FlightController( curPlayer, gameId, gc,this.disconnected));

    }
}
