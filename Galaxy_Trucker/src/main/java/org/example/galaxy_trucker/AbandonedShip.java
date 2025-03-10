package org.example.galaxy_trucker;

import java.util.ArrayList;

public class AbandonedShip extends Card{
    private int requirement;
    private int reward;


    public AbandonedShip(int requirement, int reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.reward = reward;
    }
    public void CardEffect(){
       int AbandonedShipOrder=0;
       boolean AbandonedShipBool=true;
       GameBoard AbandonedShipBoard=this.getBoard();
       ArrayList<Player> AbandonedShipPlayerList = AbandonedShipBoard.getPlayers();
       PlayerPlance AbandonedShipCurrentPlanche;
        int AbandonedShipLen= AbandonedShipPlayerList.size();
        while(AbandonedShipOrder<AbandonedShipLen && AbandonedShipBool ){
            AbandonedShipCurrentPlanche=AbandonedShipPlayerList.get(AbandonedShipOrder).getMyPlance();
           if( AbandonedShipCurrentPlanche.getHumans().size() > this.requirement ){
               AbandonedShipOrder++;
               //il giocatore sceglie se prendere la nave o meno
               //se accetta rimuove a scelta sua un numero di umani pari a requirements
              // AbandonedShipPlayerList.get(AbandonedShipOrder).IcreaseCredits(this.reward);
           }

            AbandonedShipOrder++;
        }


    }
}
