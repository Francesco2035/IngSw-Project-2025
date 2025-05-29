package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;

public class ReadCardClient extends PlayerStateClient{

    public void showGame(Out out) {
        StringBuilder sb = new StringBuilder();
        sb.append("New card drawn \n\n\n");
        sb.append(out.showCard());
        sb.append("\n\n\n");
        out.render(sb);
    }

    public void showGame(GuiOut out) {
    }

}
