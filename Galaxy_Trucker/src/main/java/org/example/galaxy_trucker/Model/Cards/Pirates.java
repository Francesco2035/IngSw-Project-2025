package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
//import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidDefenceEceptiopn;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;


//inserire le chiamate alle interfacce
public class Pirates extends Card{
    private int requirement;
    private int reward;
    private int PlayerOrder;
    private int ShotsOrder;
    private int ShotsLine;
    private IntegerPair hit;
    private  boolean defeated;
    private int order;
    private Player currentPlayer;
    private int[] lines;
    private double currentpower;
    private int energyUsage;

    @JsonProperty("punishment")
    private ArrayList<Integer> Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack


    /// il caso base è che non attivi mai cannoni doppi e chenon si difenda dagli spari in caso di sconfitta, in caso di vittoria non accetta la ricompensa
    public Pirates(int level, int time, GameBoard board, int Reward, int Requirement, ArrayList<Integer> Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;

        this.defeated = false;
        this.currentPlayer = new Player();
        this.lines = new int[Punsihment.size()/2];
        for(int i=0;i< Punishment.size()/2;i++){
            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
        }
        this.hit = new IntegerPair(0,0);
        this.currentpower = 0;
        this.energyUsage = 0;
        this.order = 0;


    }
 @Override
    public void CardEffect(){

        this.hit =new IntegerPair(0,0);
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
        System.out.println("player number "+this.order);
        if(this.order<PlayerList.size() && !this.defeated){
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            currentPlayer = PlayerList.get(this.order);
            System.out.println("current is:"+currentPlayer.GetID());
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
                throw new WrongNumofEnergyExeption("no energy here:"+ i.getFirst()+ " "+i.getSecond());
            }
        }
        this.checkStrength();

    }

    public void checkStrength(){

        if(this.currentpower>this.getRequirement()){
            this.defeated=true;
            this.currentPlayer.setState(new Accepting());
            //this.currentPlayer.setInputHandler(new Accept(this));
        }
        else if (this.currentpower<this.getRequirement()){
            this.continueCard();
        }
        else {
            this.currentPlayer.setState(new Waiting());
            this.updateSates();
        }


    }



    @Override
    public void continueCard() {
        int Movement;
        boolean shotsFlag= false;
            while (this.ShotsOrder < Punishment.size() && shotsFlag == false) {

                System.out.println("attack number: "+this.ShotsOrder/2 +" on: "+currentPlayer.GetID());

            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            if (Punishment.get(ShotsOrder) == 0) { //sinistra
                Movement = 0;
                while (Movement < 10  && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                this.continueCard();
                                return;
                            }


                        }
                        else {//colpo piccolo
                            currentPlayer.setState(new DefendingFromSmall());
                        }
                        }


                    Movement++;
                }
            }
            else if (Punishment.get(ShotsOrder) == 1) {//sopra
                Movement = 0;
                while (Movement < 10 && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            currentPlayer.setState(new DefendingFromSmall());
                        }

                    }

                    Movement++;
                }
            }
            else if (Punishment.get(ShotsOrder) == 2) {// destra
                Movement = 9;
                while (Movement >= 0   && lines[ShotsOrder/2]<10&& shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder/2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                System.out.println("non si è rotto nulla");
                                this.ShotsOrder+=2;
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            currentPlayer.setState(new DefendingFromSmall());
                        }

                    }
                    Movement--;
                }

            }
            else { //sotto
                Movement = 9;
                while (Movement >= 0  && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {
                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(Punishment.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                            CurrentPlanche.handleAttack(hit.getFirst(), hit.getSecond());
                            if (CurrentPlanche.getBroken()){
                                System.out.println("rottura nave");
                                this.currentPlayer.setState(new HandleDestruction());
                                return;

                            }
                            else{
                                this.ShotsOrder+=2;
                                this.continueCard();
                                return;
                            }

                        }
                        else {//colpo piccolo
                            currentPlayer.setState(new DefendingFromSmall());
                        }
                        }


                    Movement--;
                }

            }
            if(shotsFlag == false){
            this.ShotsOrder += 2;
            }
        }
        if(this.ShotsOrder >=Punishment.size() ){
            this.ShotsOrder = 0;
            this.updateSates();
        }
    }

    @Override
    public void continueCard(boolean accepted){
        if(accepted){
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), -this.getTime());
        }

        this.finishCard();
    }

    @Override
    public void DefendFromSmall(IntegerPair energy){
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if ((currentBoard.getShield()[Punishment.get(ShotsOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+getPunisment().get(ShotsOrder));
            }
            else {
                try {
                    currentBoard.performAction(tiles[energy.getFirst()][energy.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());
                }
                catch (Exception e){
                    throw new ImpossibleBoardChangeException("There was no energy to use here");
                }
                System.out.println("DefendFromSmall");
            }
        }
        else {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            if (currentBoard.getBroken()){
                System.out.println("rottura nave");
                this.currentPlayer.setState(new HandleDestruction());
                return;

            }
            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
        }
        this.ShotsOrder+=2;
        this.continueCard();
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        this.setFinished(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getShotsOrder() {
        return ShotsOrder;
    }

    //json required
    public Pirates(){}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public ArrayList<Integer> getPunisment() {return Punishment;}
    public void setPunisment(ArrayList<Integer> punisment) {Punishment = punisment;}
}
