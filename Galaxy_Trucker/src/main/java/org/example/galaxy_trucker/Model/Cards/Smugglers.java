


package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;

public class Smugglers extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private int Punishment;
    private int order;
    private Player currentPlayer;
    private boolean defeated;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    Smugglers(int level, int time, GameBoard board, ArrayList<Goods> Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.rewardGoods= Reward;
        this.Punishment = Punsihment;
        this.defeated=false;
        this.currentPlayer=null;
        this.order=0;
    }


    @Override
    public void CardEffect(){

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(PlayerStates.Waiting);
        }
        this.updateSates();
    }


    @Override
    public void updateSates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();

            this.currentPlayer.setState(PlayerStates.GiveAttack);
            this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

//    @Override
//    public void continueCard(ArrayList<IntegerPair> cannons) {
//        double power= currentPlayer.getMyPlance().getPower(cannons);
//        if(power>this.getRequirement()){
//            this.currentPlayer.setState(PlayerStates.Accepting);
//            this.currentPlayer.setInputHandler(new Accept(this));
//            this.defeated=true;
//        }
//        else if(power<this.getRequirement()){
//            this.currentPlayer.setState(PlayerStates.Killing);
//            this.currentPlayer.setInputHandler(new Killing(this));
//
//            //steal shit su PlayerBoard il player non ha scelta
//        }
//    }






    @Override
    public void continueCard(boolean accepted){
        if(accepted){
            currentPlayer.handleCargo(this.rewardGoods);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());
        }

        this.finishCard();
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
