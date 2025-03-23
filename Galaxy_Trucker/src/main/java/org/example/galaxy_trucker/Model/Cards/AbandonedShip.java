package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;


public class AbandonedShip extends Card{
    private int requirement;
    private int reward;
    private Player currentPlayer;
    private boolean flag;
    private int order;


    public AbandonedShip(int requirement, int reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.reward = reward;
        this.currentPlayer = null;
        this.flag = false;
        this.order = 0;
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
        while(this.order<PlayerList.size()&& !this.flag) {
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();
            Tile TileBoard[][] = CurrentPlanche.getPlayerBoard();
            ArrayList<IntegerPair> HousingCoords = CurrentPlanche.gethousingUnits();
            int totHumans = 0;

            for (int i = 0; i < CurrentPlanche.gethousingUnits().size(); i++) {
                //somma per vedere il tot umani
                totHumans += TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent().getAbility();
            }
            if(totHumans>this.requirement){
                this.flag = true;
                currentPlayer.setState(PlayerStates.Killing);
            }

            this.order++;
        }
        if(order==PlayerList.size()){
            this.finishCard();
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
    public void continueCard(ArrayList<IntegerPair> coordinates, boolean accepted) {
        if(accepted) {
            if (coordinates.size() != this.requirement) {
                //devo dirgli che ha scelto il num sbagliato di persone da shottare
                //throw new Exception();
            }

            for (int j = 0; j < coordinates.size(); j++) {
                currentPlayer.getMyPlance().kill(coordinates.get(j), 1, true, true);
            }
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());

            this.finishCard();
        }
        else{
            currentPlayer.setState(PlayerStates.Waiting);
            this.flag = false;
            this.updateSates();
        }
    }
    //json
    public AbandonedShip() {}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
}
