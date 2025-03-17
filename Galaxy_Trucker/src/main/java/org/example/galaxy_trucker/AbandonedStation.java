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
        int Order=0;
        boolean AbandonedStationBool=true;
        GameBoard AbandonedStationBoard=this.getBoard();
        ArrayList<Player> PlayerList = AbandonedStationBoard.getPlayers();
        PlayerBoard AbandonedShipCurrentPlanche;
        int AbandonedShipLen= PlayerList.size();
        while(Order<AbandonedShipLen && AbandonedStationBool ){ // ask all the player by order
            // or untill someone does if they can and want to get the ship

            AbandonedShipCurrentPlanche=PlayerList.get(Order).getMyPlance(); // get the current active planche
            ArrayList<IntegerPair> HousingCoords=AbandonedShipCurrentPlanche.gethousingUnits();
            Tile TileBoard[][]=AbandonedShipCurrentPlanche.getPlayerBoard();
            int totHumans=0;

            for(int i=0; i<AbandonedShipCurrentPlanche.gethousingUnits().size();i++ ){
                //somma per vedere il tot umani
                totHumans+=TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent().getNumHumans();
            }

            if(totHumans>=this.requirement){

                //il giocatore sceglie se prendere la nave o meno

                //tecnicamente dovrebbe anche gestire gli spostamenti, non ha senso che sia io a gestire entrambe le cose

                PlayerList.get(Order).handleCargo(this.rewardGoods);
                AbandonedStationBool=false;
                //AbandonedStationPlayerList.get(AbandonedStationOrder).movePlayer(-this.getTime());
            }

            Order++;
        }
        return;
    }
}
