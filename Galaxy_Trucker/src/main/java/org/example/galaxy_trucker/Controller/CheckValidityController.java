package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosePosition;

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
            throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }

        try {
            System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
            command.execute(curPlayer);
            nextState(gc);
        } catch (Exception e) {
            curPlayer.setMyPlance(playerBoardCopy);
            playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());

            //throw new IllegalCallerException("illegal execution of command" + command.toString());
            System.out.println(e);
        }

    }


    @Override
    public void nextState(GameController gc) {
        System.out.println("CHECK_CONTROLLER callign next state for " + curPlayer.GetID());
        if (curPlayer.getmyPlayerBoard().checkValidity()){
            curPlayer.setState(new ChoosePosition());
            System.out.print("ops");
            gc.setControllerMap(curPlayer,new PrepController(curPlayer, gameId,gc,disconnected));

        }
        else {
            gc.setControllerMap(curPlayer, new CheckValidityController(curPlayer, gameId,this.disconnected));
        }
    }
}
