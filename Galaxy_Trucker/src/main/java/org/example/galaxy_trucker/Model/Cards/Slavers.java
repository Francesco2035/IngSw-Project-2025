package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class Slavers extends Card{
    private int requirement;
    private int reward;
    private int Punishment;
    private int order;
    private Player currentPlayer;
    private boolean defeated;
    private double currentpower;
    private  int energyUsage;

    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    public Slavers(int level, int time, GameBoard board, int Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.defeated = false;
        this.currentPlayer=null;
        this.order=0;
        this.currentpower=0;
        this.energyUsage=0;

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
            this.currentpower=0;
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

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
//        if(power>this.getRequirement()){
//            this.currentPlayer.setState(new Accepting());
//            //this.currentPlayer.setInputHandler(new Accept(this));
//            this.defeated=true;
//        }
//        else if(power<this.getRequirement()){
//            this.currentPlayer.setState(new Killing());
//            //this.currentPlayer.setInputHandler(new Killing(this));
//        }

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
            this.currentPlayer.setState(new Killing());
            //this.currentPlayer.setInputHandler(new Killing(this));
        }

    }


    @Override
    public void killHumans(ArrayList<IntegerPair> coordinates){
        if (coordinates.size() != this.requirement) {
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
            //throw new Exception();
        }

//        for (int j = 0; j < coordinates.size(); j++) {
//            currentPlayer.getMyPlance().kill(coordinates.get(j), 1, true, true);
//        }
//        this.updateSates();
    }





    @Override
    public void continueCard(boolean accepted){
        if(accepted){
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());
        }

        this.finishCard();
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
