package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;


public abstract class Controller {


    //Command command;
    Player curPlayer;
    String gameId;
    PlayerBoard playerBoardCopy;


    public synchronized void action(Command command, GameController gc) {

        playerBoardCopy = curPlayer.getmyPlayerBoard().clone();
        if (!command.allowedIn(curPlayer.getPlayerState())){
            throw new IllegalStateException("Command not accepted: "+ command.getClass()+" \n" +curPlayer.getPlayerState());
        }

        try {
            System.out.println("Action called for " + gameId + ": " + command.getTitle() + " "+ command.playerId);
            command.execute(curPlayer);
            gc.changeState();
        } catch (Exception e) {
            playerBoardCopy.setListener(curPlayer.getmyPlayerBoard().getListener());
            curPlayer.setMyPlance(playerBoardCopy);
            //throw new IllegalCallerException("illegal execution of command" + command.toString());
            System.out.println(e);
        }

    }


    public abstract void nextState(GameController gc);

}
