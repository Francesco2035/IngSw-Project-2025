package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;

public class ChoosePositionController extends Controller{




    public ChoosePositionController(Player player, String gameId, boolean disconnected) {
        this.curPlayer = player;
        this.gameId = gameId;
    }


    @Override
    public synchronized void action(Command command, GameController gc) { //  devo overridare anche qui ok

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


    @Override
    public void nextState(GameController gc) {
        curPlayer.setState(new AddCrewState());
        PostPrepController newController = new PostPrepController(curPlayer,gameId,disconnected);
        newController.setExceptionListener(exceptionListener);
        gc.setControllerMap(curPlayer, newController);
    }
}
