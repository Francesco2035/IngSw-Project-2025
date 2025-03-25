package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;

public class Slavers extends Card{
    private int requirement;
    private int reward;
    private int Punishment;
    private int order;
    private Player currentPlayer;
    private boolean defeated;

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
//        }
//    }

    @Override
    public void killHmans(ArrayList<IntegerPair> coordinates){
        if (coordinates.size() != this.requirement) {
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
            //throw new Exception();
        }

        for (int j = 0; j < coordinates.size(); j++) {
            currentPlayer.getMyPlance().kill(coordinates.get(j), 1, true, true);
        }
        this.updateSates();
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
