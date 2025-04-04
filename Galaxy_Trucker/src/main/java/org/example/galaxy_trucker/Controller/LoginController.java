package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.io.File;

public class LoginController extends Controller {

    private Player player;
    private String text;
    private Game model;
    private String ID;
    private boolean ready;
    private int lv;

    //



    public LoginController(Game model) {

        this.model = model;

    }

    @Override
    public void action(File json){

        //json viene letto / stringa

        //switch case in base allo stato del player

        switch (player.getPlayerState()){

            case NotReady -> {
                if (text.equals("quit")){
                    model.RemovePlayer(ID);
                }
                if (ready){
                    model.getPlayers().get(ID).setState(PlayerStates.Ready);
                }
            }

            case Ready -> {
                if (text.equals("quit")){
                    model.RemovePlayer(ID);
                }
                if (!ready){
                    model.getPlayers().get(ID).setState(PlayerStates.NotReady);
                }

            }

            default -> {

                model.NewPlayer(ID);
                model.getPlayers().get(ID).setState(PlayerStates.NotReady);
            }
        }


        // passo la stringa del mio id
        // scelgo tipo di game (se sono il primo ad entrare)
        // metto ready
        // metto notReady


    }

}
