package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class SeeBoardsClient extends PlayerStateClient{

    @Override
    public void showGame(Out out) {
        out.seeBoards();
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of("Ok"));
    }
}
