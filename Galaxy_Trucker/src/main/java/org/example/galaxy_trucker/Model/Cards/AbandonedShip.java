package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.GetterHandler.HousingUnitGetter;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.InputHandlers.AcceptKilling;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Boards.SetterHandler.HousingUnitSetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingHumanGetter;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

import java.util.ArrayList;


public class AbandonedShip extends Card{
    private int requirement;
    private int reward;
    private Player currentPlayer;
    private boolean flag;
    private int order;
    private int totHumans;


    public AbandonedShip(int requirement, int reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.reward = reward;
        this.currentPlayer = null;
        this.flag = false;
        this.order = 0;
        totHumans=0;
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
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();
            Tile TileBoard[][] = CurrentPlanche.getPlayerBoard();
            ArrayList<IntegerPair> HousingCoords=new ArrayList<>();
            if(CurrentPlanche.getClassifiedTiles().containsKey(modularHousingUnit.class)) {
                 HousingCoords = CurrentPlanche.getClassifiedTiles().get(modularHousingUnit.class);
            }
            if(CurrentPlanche.getValidPlayerBoard()[6][6]==1) {
                HousingCoords.add(new IntegerPair(6,6));
            }
            this.totHumans = 0;

            System.out.println("numofHousingCoords: "+HousingCoords.size());
            for (int i = 0; i < HousingCoords.size(); i++) {
                //somma per vedere il tot umani
                totHumans += ((int) TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent()
                        .get(new HousingHumanGetter(TileBoard[HousingCoords.get(i).getFirst()][HousingCoords.get(i).getSecond()].getComponent())));
            }
            HousingCoords.remove(new IntegerPair(6,6));
            System.out.println("totHumans di"+currentPlayer.GetID()+": "+totHumans);
            if(totHumans>this.requirement){
                System.out.println(currentPlayer.GetID()+" has enough required housing");
                this.flag = true;
                currentPlayer.setState(PlayerStates.AcceptKilling);
                currentPlayer.setInputHandler(new AcceptKilling(this));
                currentPlayer.setCard(this);
            }

            this.order++;
        }

    }
    @Override
    public  void  ActivateCard() {

        System.out.println("ActivateCard");
        currentPlayer.getInputHandler().action();
    }

    @Override
    public void finishCard() {
        System.out.println("card finished");
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
                throw new WrongNumofHumansException("wrong number of humans");
            }

//            for (int j = 0; j < coordinates.size(); j++) {
//                currentPlayer.getMyPlance().kill(coordinates.get(j), 1, true, true);
//            }

            for (IntegerPair coordinate : coordinates) {
                System.out.println("killing humans in "+coordinate.getFirst()+" "+coordinate.getSecond());
                currentPlayer.getMyPlance().setGetter(new HousingUnitGetter(currentPlayer.getMyPlance(),
                        coordinate, 1, false, false));
                currentPlayer.getMyPlance().getGetter().get();
            }
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());

            this.finishCard();
        }
        else{
            currentPlayer.setState(PlayerStates.Waiting);
            this.flag = false;
            this.updateSates();
        }
    }

    public int getTotHumans() {
        return totHumans;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json
    public AbandonedShip() {}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
}
