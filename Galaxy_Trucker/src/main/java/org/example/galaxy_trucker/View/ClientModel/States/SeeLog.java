package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.ASCII_ART;
import org.example.galaxy_trucker.View.TUI.Out;

import java.util.ArrayList;
import java.util.List;

public class SeeLog extends PlayerStateClient{


    @Override
    public void showGame(Out out){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.Border);
        toPrint.append(out.showLog());
        toPrint.append(ASCII_ART.Border);
        out.render(toPrint);
    }



    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<>(List.of());
    }
}
