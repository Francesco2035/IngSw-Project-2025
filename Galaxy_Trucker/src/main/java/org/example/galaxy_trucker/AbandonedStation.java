package org.example.galaxy_trucker;

import java.util.ArrayList;

public class AbandonedStation extends Card{
    private int requirement;
    private ArrayList<Goods> rewardGoods;


    public AbandonedStation(int requirement, ArrayList<Goods> reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.rewardGoods = reward;
    }
    @Override
    public void CardEffect(){
        int AbandonedStationOrder=0;
        boolean AbandonedStationBool=true;
        GameBoard AbandonedStationBoard=this.getBoard();
        ArrayList<Player> AbandonedStationPlayerList = AbandonedStationBoard.getPlayers();
        PlayerPlance AbandonedShipCurrentPlanche;
        int AbandonedShipLen= AbandonedStationPlayerList.size();
        while(AbandonedStationOrder<AbandonedShipLen && AbandonedStationBool ){ // ask all the player by order
            // or untill someone does if they can and want to get the ship

            AbandonedShipCurrentPlanche=AbandonedStationPlayerList.get(AbandonedStationOrder).getMyPlance(); // get the current active planche
            if( AbandonedShipCurrentPlanche.getHumans().size() > this.requirement ){
                AbandonedStationOrder++;
                //il giocatore sceglie se prendere la nave o meno
                //se accetta rimuove a scelta sua un numero di umani pari a requirements
                // AbandonedShipPlayerList.get(AbandonedShipOrder).handleCargo(this.reward);
                AbandonedStationBool=false;
                //AbandonedStationPlayerList.get(AbandonedStationOrder).movePlayer(-this.getTime());
            }

            AbandonedStationOrder++;
        }
        return;
    }
}
