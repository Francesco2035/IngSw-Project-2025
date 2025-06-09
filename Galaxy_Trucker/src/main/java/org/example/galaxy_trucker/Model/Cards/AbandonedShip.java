package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.Accepting;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.Killing;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;



/// in caso di disconnession il player sempluicemente non accetta se deve accttare la nave
/// devo dividere la accept e la kill

public class AbandonedShip extends Card{
    private int requirement;
    private int reward;
    private Player currentPlayer;
    private boolean flag;
    private int order;
    private int totHumans;
    private ArrayList<Player> losers;


    public AbandonedShip(int requirement, int reward, int level, int time, GameBoard board) {
        super(level, time, board);
        this.requirement = requirement;
        this.reward = reward;
        this.currentPlayer = new Player();
        this.flag = false;
        this.order = 0;
        totHumans=0;
    }

    @Override
    public void CardEffect() throws InterruptedException {
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        Thread.sleep(3000);
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
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
            System.out.println("Cchecking:"+currentPlayer.GetID());
            if(CurrentPlanche.getNumHumans()>=requirement){
                this.totHumans=CurrentPlanche.getNumHumans();
                System.out.println(currentPlayer.GetID()+" has enough required housing");
                this.flag = true;
                currentPlayer.setState(new Accepting());
                //currentPlayer.setInputHandler(new AcceptKilling(this));
                currentPlayer.setCard(this);
            }

            this.order++;
        }

    }
    @Override
    public void continueCard(boolean accepted) {


        if(accepted){
            System.out.println(currentPlayer.GetID()+" has accepted");
            this.currentPlayer.setState(new Killing());
        }
        else{
            System.out.println(currentPlayer.GetID()+" has refused");
            currentPlayer.setState(new Waiting());
            this.flag = false;
            this.updateSates();
        }
    }
//    @Override
//    public  void  ActivateCard() {
//
//        System.out.println("ActivateCard");
//        currentPlayer.getInputHandler().action();
//    }

    @Override
    public void finishCard() {
        System.out.println("card finished");
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());

        }
        losers.remove(getBoard().checkDoubleLap());/// così non ho doppioni :3
        losers.addAll(getBoard().checkDoubleLap());
        for(Player p: losers){
            getBoard().abandonRace(p);
        }
        this.setFinished(true);

    }

    @Override
    public void killHumans (ArrayList<IntegerPair> coordinates) {
        if(coordinates!=null) {
            if (coordinates.size() != this.requirement) {
                //devo dirgli che ha scelto il num sbagliato di persone da shottare
                this.currentPlayer.setState(new Accepting());
                throw new WrongNumofHumansException("wrong number of humans");
            }

            ///  fai l try catch e opera sulla copia :)
            PlayerBoard curr= currentPlayer.getmyPlayerBoard();
            Tile tiles[][]=curr.getPlayerBoard();
            try{
            for (IntegerPair coordinate : coordinates) {
                System.out.println("killing humans in "+coordinate.getFirst()+" "+coordinate.getSecond());

                curr.performAction(tiles[coordinate.getFirst()][coordinate.getSecond()].getComponent(),new KillCrewAction(curr), new Killing());
            }
            }
            catch (Exception e){
                //devo rimanere allo stato di dare gli umani ezzz
                System.out.println("non ce sta più nessuno qui");
                this.currentPlayer.setState(new Accepting());
                throw new ImpossibleBoardChangeException("there was an error in killing humans");

            }
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());


            if(currentPlayer.getmyPlayerBoard().getNumHumans()==0){
                losers.add(currentPlayer);
            }
            this.finishCard();
        }
//        else{
//            currentPlayer.setState(new Waiting());
//            this.flag = false;
//            this.updateSates();
//        }
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
