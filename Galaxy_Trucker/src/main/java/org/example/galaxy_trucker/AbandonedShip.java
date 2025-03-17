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
    @Override
    public void CardEffect(){
       int Order=0;
       boolean Bool=true;
       GameBoard Board=this.getBoard();
       ArrayList<Player> PlayerList = Board.getPlayers();
       PlayerBoard AbandonedShipCurrentPlanche;
        int Len= PlayerList.size();
        IntegerPair coordinates;
        while(Order<Len && Bool ){ // ask all the player by order
            // or untill someone does if they can and want to get the ship

//            AbandonedShipCurrentPlanche=PlayerList.get(Order).getMyPlance(); // get the current active planche
//           if( AbandonedShipCurrentPlanche.getHumans().size() > this.requirement ){
//               //il giocatore sceglie se prendere la nave o meno
//               //se accetta rimuove a scelta sua un numero di umani pari a requirements
//              // AbandonedShipPlayerList.get(AbandonedShipOrder).IcreaseCredits(this.reward);
//               if(PlayerList.get(Order).getConfirm()){
//
//                   //faccio il while che chiede dove uccidere e poi dalla planche ammazzo lì
//                   for (int i = 0; i < this.requirement; i++) {
//                        coordinates=PlayerList.get(Order).getCoordinates();
//                        PlayerList.get(Order).getMyPlance().killHuman(coordinates);
//                   }
//
//
//                    PlayerList.get(Order).movePlayer(this.getTime());
                     Bool=false;
               }
           }

            //Order++;
        }
        return;
    }
}
