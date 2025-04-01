package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingHumanGetter;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;


import java.util.ArrayList;

public class AbandonedStation extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private Player currentPlayer;
    private boolean flag;
    private int order;



    public AbandonedStation(int requirement, ArrayList<Goods> reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.rewardGoods = reward;
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
        while(this.order<PlayerList.size()&& !this.flag) {
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();
            Tile TileBoard[][] = CurrentPlanche.getPlayerBoard();
            ArrayList<IntegerPair> HousingCoords=new ArrayList<>();
            if(CurrentPlanche.getClassifiedTiles().containsKey(ModularHousingUnit.class)) {
                HousingCoords = CurrentPlanche.getClassifiedTiles().get(ModularHousingUnit.class);
            }
            if(CurrentPlanche.getValidPlayerBoard()[6][6]==1) {
                HousingCoords.add(new IntegerPair(6, 6));
            }
            int totHumans = 0;

            for (int i = 0; i < HousingCoords.size(); i++) {
                //somma per vedere il tot umani
                totHumans += ((ArrayList<Integer>) TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent().
                        get(new HousingHumanGetter(TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent()))).getFirst();

            }
            if(totHumans>this.requirement){
                this.flag = true;
                currentPlayer.setState(PlayerStates.Accepting);
                currentPlayer.setInputHandler(new Accept(this));

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
    public void continueCard(boolean accepted) {
        if(accepted) {

            currentPlayer.handleCargo(this.rewardGoods);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());

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
