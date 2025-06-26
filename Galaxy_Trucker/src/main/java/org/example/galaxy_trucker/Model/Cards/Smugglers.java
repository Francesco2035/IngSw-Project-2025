


package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;
import static java.util.Collections.max;

public class Smugglers extends Card{
    private int requirement;
    @JsonProperty("rewardGoods")
    private ArrayList<Goods> rewardGoods;
    private int Punishment;
    private int tmpPunishment;
    private int order;
    private Player currentPlayer;
    private boolean defeated;
    private double currentpower;
    private int energyUsage;
    private boolean isaPunishment;

    ArrayList<Player> losers;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack

    ///  come pirati e per fottere prendo le prime cose che trovo e bona così
    Smugglers(int level, int time, GameBoard board, ArrayList<Goods> Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.rewardGoods= Reward;
        this.Punishment = Punsihment;
        this.defeated=false;
        this.currentPlayer=new Player();
        this.order=0;
        this.tmpPunishment=0;
        this.energyUsage=0;
        this.isaPunishment=false;
    }

    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Smugglers",-1,-1,-1,-1));
        }
    }

    @Override
    public void CardEffect(){
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateStates();
    }


    @Override
    public void updateStates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

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
        this.currentpower = power;
        this.energyUsage = numofDouble;
        if(numofDouble==0){
            this.checkStrength();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
//
        }




    }





    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(isaPunishment){
            this.consumeEnergy2(coordinates);
            return;
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
                throw new WrongNumofEnergyExeption("wrong number of energy cells");
            }
        }
        this.checkStrength();

    }



    public void consumeEnergy2(ArrayList<IntegerPair> coordinates) {

        if(coordinates.size()!=this.energyUsage){
            System.out.println("----->"+this.energyUsage);
            throw new WrongNumofEnergyExeption("wrong number of energy cells to steal you had to give "+this.energyUsage+" energy cells, you gave "+coordinates.size());
        }
        PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        // opero sulla copia
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){

                throw new WrongNumofEnergyExeption("no energy here to consume");
            }
        }
        this.isaPunishment=false;
        this.updateStates();


    }



    /// devo far si che se ho 0 energia termina subito perché senno devoi fare un consume energy vuoto
    public void checkStrength(){


        if(this.currentpower>this.getRequirement()){


            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
            this.defeated=true;
            System.out.println("defeated");

        }
        else if(this.currentpower<this.getRequirement()) {

            System.out.println("hai perso :3");
            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
            HashMap<Integer, ArrayList<IntegerPair>> cargoH = CurrentPlanche.getStoredGoods();
            this.tmpPunishment = Punishment;
            if (!cargoH.isEmpty()) {

                this.setDefaultPunishment(tmpPunishment);
                this.currentPlayer.setState(new HandleTheft());

             }
            else {
                System.out.println("no goods found");
                this.isaPunishment = true;
                int totenergy=0;
                for(PowerCenter i: CurrentPlanche.getPowerCenters() ){
                    totenergy+=i.getType();
                    System.out.println( "tot energy:" +totenergy);
                }


                /// potrei dire che non è necessario fare nulla se ho anche 0 energie e andare avant diretto :)
                energyUsage = min(tmpPunishment, CurrentPlanche.getEnergy());
                System.out.println("energy usage (Check Strength):" +energyUsage);
                this.setDefaultPunishment(this.energyUsage);
                int test = this.getDefaultPunishment();
                if (test ==2 ){System.out.println("sgr");}
                currentPlayer.setState(new ConsumingEnergy()); /// se il player va in consuming energy e poi si disconnette ma non ha abbastanza energie  sei fottuto
            /// non dovrebbe succedere perche prendo il minimo tra i due ma non si sa mai :)
            }
                ///manca il loseCargo lose cargo semplicemente lancia

        }
        else {
            this.currentPlayer.setState(new Waiting());
            this.updateStates();
        }

    }









    @Override
    public void continueCard(boolean accepted){
        if(accepted){

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have accepted to loot the smugglers",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has accepted to loot the smugglers",-1,-1,-1,-1));
            }

            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());
            currentPlayer.setState(new HandleCargo());
            currentPlayer.getmyPlayerBoard().setRewards(rewardGoods);
        }
        else{

            ArrayList<Player> PlayerListMsg = this.getBoard().getPlayers();
            for(Player p : PlayerListMsg){
                if(p.GetID()== currentPlayer.GetID()){
                    this.sendRandomEffect(p.GetID(),new LogEvent("You have refused to loot the smugglers",-1,-1,-1,-1));
                }
                this.sendRandomEffect(p.GetID(),new LogEvent(currentPlayer.GetID()+" has refused to loot the smugglers",-1,-1,-1,-1));
            }


            this.finishCard();
        }
    }



    @Override
    public void loseCargo(IntegerPair coord,int index){

        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        ArrayList<Storage> storages=CurrentPlanche.getStorages();
        Tile tiles[][]=CurrentPlanche.getPlayerBoard();


        HashMap<Integer,ArrayList<IntegerPair>> cargoH= CurrentPlanche.getStoredGoods();

        int max;
        max= max(cargoH.keySet());
        System.out.println("\npre furto devo rubare "+tmpPunishment+"\n");

        if(cargoH.isEmpty()){
            System.out.println("ho rubato tutto, passo alle energie");
            energyUsage=min(tmpPunishment,CurrentPlanche.getEnergy());
            System.out.println("energy usage (lose Cargo) :" +energyUsage);
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment=true;
            currentPlayer.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        }

        if(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond())==null ){
            throw new InvalidInput("there is nothing here ");
        }
        else {
            if (storages.contains(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond()).getComponent())){
                int i=storages.indexOf(CurrentPlanche.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
                Storage currStorage=storages.get(i);
                if (index>=currStorage.getGoods().size() || index<0){
                    throw new InvalidInput("there is nothing at this position in the storage");
                }
                if(currStorage.getValue(index)==max ){//iterator.next da il primo elemento non chiederti perché
                   CurrentPlanche.performAction(tiles[coord.getFirst()][coord.getSecond()].getComponent(), new GetGoodAction(index,CurrentPlanche,coord.getFirst(),coord.getSecond()),new HandleTheft());///prega dio sia giusto :)
                    this.tmpPunishment--;
                   this.setDefaultPunishment(tmpPunishment);



                }
                else {
                    throw new InvalidInput("this isnt the most valuable good you own");
                }

            }
            else {
                throw new InvalidInput("this isn't a storage ");
            }
        }
        System.out.println("\npost furto devo rubare "+tmpPunishment +"\n");

        if(tmpPunishment==0){
            System.out.println("finished stealing");
            this.updateStates();
            return;
        }
        if(cargoH.isEmpty()){
            System.out.println("ho rubato tutto, passo alle energie");
            energyUsage=min(tmpPunishment,CurrentPlanche.getEnergy());
            this.setDefaultPunishment(energyUsage);
            this.isaPunishment=true;
            currentPlayer.setState(new ConsumingEnergy()); // potrebbe non fare l'update?
            this.setDefaultPunishment(energyUsage);
            return;
        }
        else {
            currentPlayer.setState(new HandleTheft());
        }


    }


        @Override
        public void finishCard() {
            checkLosers();
            System.out.println("card finished");
            this.setFinished(true);
        }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void keepGoing(){
        this.finishCard();
    }



    //json required
    public Smugglers(){}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public ArrayList<Goods> getReward() {return rewardGoods;}
    public void setReward(ArrayList<Goods> reward) {this.rewardGoods = reward;}


    ///public int getDefaultPunishment() {return Punishment;}
    public void setPunishment(int punishment) {Punishment = punishment;}
}
