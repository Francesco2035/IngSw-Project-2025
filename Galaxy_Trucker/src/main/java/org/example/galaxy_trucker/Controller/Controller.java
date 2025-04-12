package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;


public abstract class Controller {


    Command command;
    Player curPlayer;
    PlayerBoard playerBoardCopy;

    public void action(String json){
        try {
            command = curPlayer.getPlayerState().PlayerAction(json, curPlayer);
            playerBoardCopy = curPlayer.getmyPlayerBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            command.execute();
        } catch (Exception e) {
            curPlayer.setMyPlance(playerBoardCopy);
            throw new IllegalCallerException("illegal execution of command" + command.toString());
        }
    }


    public abstract void nextState(GameHandler gh);

}
