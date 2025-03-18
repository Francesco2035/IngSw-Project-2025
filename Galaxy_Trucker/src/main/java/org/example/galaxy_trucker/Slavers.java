package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Slavers extends Card{
    private int requirement;
    private int reward;
    private int Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    public Slavers(int level, int time, GameBoard board, int Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
    }
    @Override
    public void CardEffect(){
        int Order=0;
        int AttackNumber=0;
        boolean Flag=true;
        boolean Attacked=false;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<IntegerPair> ActiveCannons;
        ArrayList<IntegerPair> coordinates;


        double PlayerPower;


        while(Len>Order && Flag){

            ActiveCannons=PlayerList.get(Order).getPower();
            CurrentPlanche=PlayerList.get(Order).getMyPlance();
            PlayerPower=CurrentPlanche.getPower(ActiveCannons);
            if(PlayerPower<requirement) {
                coordinates=PlayerList.get(Order).getHumanstoKIll();
                if(coordinates.size()!=this.requirement) {
                    //devo dirgli che ha scelto il num sbagliato di persone da shottare
                    //throw new Exception();
                }
                for(int j=0; j<coordinates.size();j++){
                    PlayerList.get(Order).getMyPlance().kill(coordinates.get(j),1,true,true);
                }
            }// fine caso sconfitta
            else if (PlayerPower>requirement){
                Flag=false;
                //if(PLayerlist.get(Order).yes()){   //chiedo se vuole prende le ricompense
                PlayerList.get(Order).IncreaseCredits(reward);
                //PlayerList.get(Order).movePlayer(-this.Time);
                //}
            }//fine caso vittoria
            Order++;

        }
    }
    //json required
    public Slavers(){}
    public int getPunishment() {return Punishment;}
    public void setPunishment(int punishment) {Punishment = punishment;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}


}
