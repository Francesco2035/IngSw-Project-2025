package org.example.galaxy_trucker;

import java.util.ArrayList;

public class OpenSpace extends Card{
    OpenSpace(int level,GameBoard board){
        super(level, 0 ,board);
    }
    public void CardEffect(){
        int OpenSpaceOrder=0;
         GameBoard OpenSpaceBoard=this.getBoard();
        ArrayList<Player> OpenSpacePlayerList = OpenSpaceBoard.getPlayers();
        PlayerPlance OpenspaceCurrentPlanche;
        int OpenSpaceMovement=0;
        while(OpenSpaceOrder<OpenSpacePlayerList.size()){
            OpenspaceCurrentPlanche=OpenSpacePlayerList.get(OpenSpaceOrder).getMyPlance();
            //OpenSpaceMovement=OpenspaceCurrentPlanche.getPower();
            //OpenSpacePlayerList.get(OpenSpaceOrder).movePlayer(OpenSpaceMovement);
            //o simili a seconda di come decidiamo di fare il movement
            OpenSpaceOrder++;

        }

    return;
    }
}
