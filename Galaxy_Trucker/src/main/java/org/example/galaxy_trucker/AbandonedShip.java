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
        ArrayList<IntegerPair> coordinates;
        while(Order<Len && Bool ){ // ask all the player by order
            // or untill someone does if they can and want to get the ship

            //PER GET HUMANS.SIZE IO HO LE COORDINATE DEI TILE E DA LI MI PRENDO IL GET NUMBER CHE SOMMERO AL VALORE ESTERNO

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
               //se accetta rimuove a scelta sua un numero di umani pari a requirements
              PlayerList.get(Order).IncreaseCredits(this.reward);
              // if(PlayerList.get(Order).getConfirm()) {

               //faccio il while che chiede dove uccidere e poi dalla planche ammazzo lì
                    coordinates=PlayerList.get(Order).getHumanstoKIll();
                    if(coordinates.size()!=this.requirement) {
                        //devo dirgli che ha scelto il num sbagliato di persone da shottare
                        //throw new Exception();
                    }
                    for(int j=0; j<coordinates.size();j++){
                    PlayerList.get(Order).getMyPlance().kill(coordinates.get(j),1,true,true);
                     }
                    Board.movePlayer(PlayerList.get(Order).GetID(),this.getTime());
                     Bool=false;
               //}
           }

    //json
    public AbandonedShip() {}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
}
