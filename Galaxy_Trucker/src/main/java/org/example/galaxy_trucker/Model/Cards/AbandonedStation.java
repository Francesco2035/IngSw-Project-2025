package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;


import java.util.ArrayList;

public class AbandonedStation extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private Player currentPlayer;
    private boolean flag;
    private int order;
    int totHumans;



    public AbandonedStation(int requirement, ArrayList<Goods> reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.rewardGoods = reward;
        this.order = 0;
        this.totHumans = 0;
        this.flag = false;
        this.currentPlayer = null;

    }


    @Override
    public  void  ActivateCard() {
        currentPlayer.getInputHandler().action();
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
        while(this.order<=PlayerList.size()&& !this.flag) {
            if(order==PlayerList.size()){
                this.finishCard();
                break;
            }
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

            if(CurrentPlanche.getNumHumans()>this.requirement){
                System.out.println(currentPlayer.GetID()+" has enough required housing");
                this.flag = true;
                currentPlayer.setState(PlayerStates.Accepting);
                currentPlayer.setInputHandler(new Accept(this));

            }

            this.order++;
        }
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(PlayerStates.BaseState);
        }
    }

    @Override
    public void continueCard(boolean accepted) {
        if(accepted) {

            currentPlayer.handleCargo(this.rewardGoods);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());

        }
        else{
            currentPlayer.setState(PlayerStates.Waiting);
            this.flag = false;
            this.updateSates();
        }
    }


    //json required
    public AbandonedStation() {}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public ArrayList<Goods> getReward() {return rewardGoods;}
    public void setReward(ArrayList<Goods> reward) {this.rewardGoods = reward;}
}
