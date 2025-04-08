


package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class Smugglers extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private int Punishment;
    private int order;
    private Player currentPlayer;
    private boolean defeated;
    private double currentpower;
    private int energyUsage;
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
            p.setState(new Waiting());
        }
        this.updateSates();
    }


    @Override
    public void updateSates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
            this.currentpower=0;
            this.currentPlayer.setState(new GiveAttack());
            //this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

    @Override
    public void checkPower(double power, int numofDouble) {
//
        if(numofDouble==0){
            this.checkStrength();
        }
        else {
            this.currentpower = power;
            this.energyUsage = numofDouble;
            this.currentPlayer.setState(new ConsumingEnergy());
//
        }




    }


    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if(coordinates.size()!=this.energyUsage){
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
            ///  devo fare si che in caso di errore torni alla give attack
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        /// opero sulla copia
        for(IntegerPair i:coordinates){
            CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
        }
        this.checkStrength();

    }

    public void checkStrength(){


        if(this.currentpower>this.getRequirement()){
            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
            this.defeated=true;
        }
        else if(this.currentpower<this.getRequirement()){

           ///manca il loseCargo

        }
    }









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
