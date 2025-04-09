package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;


public abstract class Controller {


    Command command;
    Player curPlayer;


    public void action(String json){
        try {
            command = curPlayer.getPlayerState().PlayerAction(json, curPlayer, Optional.empty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        command.execute();
    }


    public abstract void nextState(GameHandler gh);

}
