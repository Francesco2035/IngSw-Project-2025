package org.example.galaxy_trucker.View.ClientModel;

import org.example.galaxy_trucker.Commands.InputReader;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;
import org.example.galaxy_trucker.View.TUI.Out;
import org.example.galaxy_trucker.View.ViewPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerClient {


    private PlayerStateClient state;


    public void showGame(Out out){
        state.showGame(out);
    }

    public void setPlayerState(PlayerStateClient playerState) {
        this.state = playerState;
    }

    public PlayerStateClient getPlayerState() {
        return state;
    }

}
