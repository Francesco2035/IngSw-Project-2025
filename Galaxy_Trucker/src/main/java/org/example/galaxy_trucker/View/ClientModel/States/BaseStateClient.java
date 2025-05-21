package org.example.galaxy_trucker.View.ClientModel.States;

import org.example.galaxy_trucker.View.TUI.Out;

public class BaseStateClient extends PlayerStateClient{

    @Override
    public void showGame(Out out) {

        StringBuilder toPrint = new StringBuilder();
        toPrint.append("BaseState\n\n");
        toPrint.append(out.showPlayers());
        //toPrint.append(out.printGameboard());
        toPrint.append(out.printBoard());
        out.render(toPrint);
        //out.printMessage(toPrint.toString());
//        out.printMessage("BaseState");
//        out.showPlayers();
//        out.printBoard();
    }

}
