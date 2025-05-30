package org.example.galaxy_trucker.View.ClientModel;

import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.CommandCompleter;
import org.example.galaxy_trucker.View.TUI.DynamicCompleter;
import org.example.galaxy_trucker.View.TUI.Out;
import org.jline.reader.Completer;

public class PlayerClient {


    private PlayerStateClient state;
    private DynamicCompleter completer;


    public void showGame(Out out){
        state.showGame(out);
    }

    public void showGame(GuiOut out){
        state.showGame(out);
    }

    public void setPlayerState(PlayerStateClient playerState) {
        this.state = playerState;
    }

    public PlayerStateClient getPlayerState() {
        return state;
    }

    public void setCompleter(DynamicCompleter completer) {
        this.completer = completer;
    }

    public DynamicCompleter getCompleter() {
        return this.completer;
    }

}
