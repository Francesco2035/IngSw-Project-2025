package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Stardust extends Card {
    public Stardust(int level, GameBoard board){
        super(level, 0 ,board);
    }
    @Override
    public void CardEffect () {

        GameBoard StarPowderBoard=this.getBoard();
        ArrayList<Player> StarPowderPlayerList = StarPowderBoard.getPlayers();
        PlayerBoard StarPowderCurrentPlanche;
        int StarPowderOrder=StarPowderPlayerList.size();
        int StarpowderMovement=0;
        while(StarPowderOrder>=0){
            StarPowderCurrentPlanche=StarPowderPlayerList.get(StarPowderOrder).getMyPlance();
            //StarpowderMovement=-StarPowderCurrentPlanche.CountExposed();
            //StarPowderPlayerList.get(StarPowderOrder).movePlayer(StarPowderMovement);
            StarPowderOrder--;
        }

        return;
    }
}

