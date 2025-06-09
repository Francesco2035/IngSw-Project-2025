package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
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
    ArrayList<Player> losers;

    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack

    /// caso base come pirati però per gli umani semplicemente uccido i primi che becco e bona così
    public Slavers(int level, int time, GameBoard board, int Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.defeated = false;
        this.currentPlayer=new Player();
        this.order=0;
        this.currentpower=0;
        this.energyUsage=0;

    }

    @Override
    public void CardEffect(){
        losers = new ArrayList<>();
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
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            currentPlayer = PlayerList.get(this.order);
            this.currentpower=0;
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

            this.currentPlayer.setState(new GiveAttack());
            //this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
            System.out.println(this.order);
        }
        else{
            System.out.println("finita");
            this.finishCard();
        }
    }

    @Override
    public void checkPower(double power, int numofDouble) {
        this.energyUsage = numofDouble;
        this.currentpower = power;
        if(numofDouble==0){
            this.checkStrength();
        }
        else {
            this.currentpower = power;
            this.currentPlayer.setState(new ConsumingEnergy());

//
        }
    }



    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(coordinates.size()!=this.energyUsage){
            currentPlayer.setState(new GiveAttack());
            throw new WrongNumofEnergyExeption("wrong number of energy cells");
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        /// opero sulla copia
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){
                currentPlayer.setState(new GiveAttack());

                throw new WrongNumofEnergyExeption("no energy cell in: "+i.getFirst()+" "+i.getSecond());
            }
        }
        this.checkStrength();

    }

    public void checkStrength(){
            System.out.println("Checking strengthof: "+currentPlayer.GetID());
        if(this.currentpower>this.getRequirement()){
            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
            this.defeated=true;
            System.out.println("defeated");
        }
        else if(this.currentpower<this.getRequirement()){
            if(this.currentPlayer.getmyPlayerBoard().getNumHumans()<this.Punishment){ // dovrebbe bastare a evitare il caso in cui uno è forzato ad uccidere più umani di quanti de abbia
                losers.add(currentPlayer);
                this.updateSates();
                return;
            }

            this.setDefaultPunishment(this.Punishment);
            this.currentPlayer.setState(new Killing());
            //this.currentPlayer.setInputHandler(new Killing(this));
        }
        else {
            this.currentPlayer.setState(new Waiting());
            this.updateSates();
        }

    }


    @Override
    public void killHumans(ArrayList<IntegerPair> coordinates){

        if (coordinates.size() != this.Punishment) {
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
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
            throw new ImpossibleBoardChangeException("there was an error in killing humans");

        }
        this.updateSates();
    }





    @Override
    public void continueCard(boolean accepted){
        if(accepted){
            currentPlayer.IncreaseCredits(this.reward);

            //non ricordo se metto il time positivo o negativo nel json se positivo devo fare meno time;
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());
        }

        this.finishCard();
    }


    @Override
    public void finishCard() {
        GameBoard Board = this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for (int i = 0; i < PlayerList.size(); i++) {
            PlayerList.get(i).setState(new BaseState());

        }

        losers.remove(getBoard().checkDoubleLap());/// così non ho doppioni :3
        losers.addAll(getBoard().checkDoubleLap());

        for(Player p: getBoard().getPlayers()){
            if(p.getmyPlayerBoard().getNumHumans()==0){
                losers.remove(p);
                losers.add(p);
            }
        }

        for(Player p: losers){
            getBoard().abandonRace(p);
        }

        System.out.println("card finished");
        this.setFinished(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json required
    public Slavers(){}
    public int getDefaultPunishment() {return Punishment;}
    public void setPunishment(int punishment) {Punishment = punishment;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}


}
