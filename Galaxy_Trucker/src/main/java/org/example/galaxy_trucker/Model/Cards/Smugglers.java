


package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class Smugglers extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private int Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    Smugglers(int level, int time, GameBoard board, ArrayList<Goods> Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.rewardGoods= Reward;
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
        double PlayerPower;
        ArrayList<IntegerPair> ActiveCannons;
        ArrayList<IntegerPair> coordinates;


        while(Len>Order && Flag){
            ActiveCannons=PlayerList.get(Order).getPower();
            CurrentPlanche=PlayerList.get(Order).getMyPlance();
            PlayerPower=CurrentPlanche.getPower(ActiveCannons);

            if(PlayerPower<requirement) {
                for(int i=0;i<Punishment;i++){
                    int index=PlayerList.get(Order).getGoodsIndex();
                    IntegerPair coord=PlayerList.get(Order).getGoodsCoordinates();
                    PlayerList.get(Order).getMyPlance().removeGood(coord,index);
            }
            }// fine caso sconfitta
            else if (PlayerPower>requirement){
                Flag=false;
                //if(PLayerlist.get(Order).yes()){   //chiedo se vuole prende le ricompense
                PlayerList.get(Order).handleCargo(rewardGoods);
                //PlayerList.get(Order).movePlayer(-this.Time);
                //}
            }//fine caso vittoria
            Order++;

        }
    }


    //json required
    public Smugglers(){}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public ArrayList<Goods> getReward() {return rewardGoods;}
    public void setReward(ArrayList<Goods> reward) {this.rewardGoods = reward;}
    public int getPunishment() {return Punishment;}
    public void setPunishment(int punishment) {Punishment = punishment;}
}
