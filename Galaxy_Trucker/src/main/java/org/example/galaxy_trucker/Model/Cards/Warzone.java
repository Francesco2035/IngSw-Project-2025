package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidDefenceEceptiopn;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Exceptions.WrongNumofHumansException;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;

//import org.example.galaxy_trucker.Model.InputHandlers.GiveAttack;
//import org.example.galaxy_trucker.Model.InputHandlers.GiveSpeed;
//import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;


//RISCRIVI ZONA DI GUERRA CHE PRENDE L'INPUT UN ARRAY DI METODI DA CHIAMARE E LI DIVIDE IN CONTROLLI
//E PUNIZIONI QUINDI HA TUTTI I METODI E BONA COSì;

//schiaccia contol shif alt freccetta su e giù per dulicare il curosre

//ordine controlli: 1 cannoni,  2 umani, 3 movimento,
//ordine punizioni: 1 movimento, 2 umani, 3 cargo, 4 spari

public class Warzone extends Card{
    @JsonProperty("RequirementOrder")
    private int[] RequirementsType;
    @JsonProperty("PunishmentOrder")
    private int[] PunishmentType;

    @JsonProperty("Punishment1")
    private int PunishmentMovement;
    @JsonProperty("Punishment2")
    private int PunishmentHumans;
    @JsonProperty("Punishment3")
    private int PunishmentCargo;
    @JsonProperty("Punishment4")
    private ArrayList<Integer> PunishmentShots;
    private  Player currentPlayer;
    private Player Worst;
    private double Minimum;
    private int PlayerOrder;
    private int ChallengeOrder;
    private int done;
    private int ShotsOrder;
    private int ShotsLine;
    private IntegerPair hit;
    private int[] lines;
    private double currentpower;
    private int currentmovement;
    private int energyUsage;




    public Warzone(int level, int time, GameBoard board, int RequirementOrder[], int PunishmentOrder[], int Punishment1, int Punishment2, int Punishment3, ArrayList<Integer> Punishment4) {
        super(level, time, board);
        RequirementsType=RequirementOrder;
        PunishmentType=PunishmentOrder;
        PunishmentMovement=Punishment1;
        PunishmentHumans=Punishment2;
        PunishmentCargo=Punishment3;
        PunishmentShots=Punishment4;

        this.PlayerOrder = 0;
        this.ChallengeOrder = 0;
        this.currentPlayer = null;
        this.done = 0;
        this.Worst = null;
        this.Minimum = 10000000;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;
        this.lines = new int[PunishmentShots.size()/2];
        for(int i=0;i< PunishmentShots.size()/2;i++){
            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
        }
        this.hit = null;
        this.currentpower=0;
        this.currentmovement=0;


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
        if(this.PlayerOrder<PlayerList.size()){
            currentPlayer = PlayerList.get(this.PlayerOrder);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
            if(RequirementsType[ChallengeOrder]==1){

                this.currentPlayer.setState(new GiveAttack());
                //this.currentPlayer.setInputHandler(new GiveAttack(this));

            }
            if(RequirementsType[ChallengeOrder]==2){

                this.currentPlayer.setState(new GiveSpeed());
                //this.currentPlayer.setInputHandler(new GiveSpeed(this));

            }
            else{

                this.checkPeople();
            }


            this.PlayerOrder++;
        }
        else{
            this.PlayerOrder=0;
            if(this.PunishmentType[ChallengeOrder]==1){
                this.loseTime();
            }
            else if(this.PunishmentType[ChallengeOrder]==2){
                this.Worst.setState(new Killing());
                //this.currentPlayer.setInputHandler(new Killing(this));
            }
            else if(this.PunishmentType[ChallengeOrder]==3){
              //  chiamo il metodo di fottitura eterna :)
            }
            else {
                this.continueCard();
            }
            this.Minimum=1000000;
            this.ChallengeOrder++;
        }
    }

//    @Override
//    public  void  ActivateCard() {
//        currentPlayer.getInputHandler().action();
//    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.done==PlayerList.size()) {
            for (int i = 0; i < PlayerList.size(); i++) {
                PlayerList.get(i).setState(new BaseState());
            }
        }
        else{
            done++;
        }
    }
//
//    @Override
//    public void continueCard(ArrayList<IntegerPair> coordinates) {
//        if (RequirementsType[ChallengeOrder]==1){
//            checkPower(coordinates);
//        }
//        else {
//            checkMovement(coordinates);
//        }
//    }






    //controlli su chi è il peggiore



    /// fornisce la potenza dei cannoni
    @Override
    public void checkPower(double power, int numofDouble) {
//            double movement= currentPlayer.getMyPlance().getPower(coordinates);

        this.currentpower = power;
        this.energyUsage=numofDouble;
        this.currentPlayer.setState(new ConsumingEnergy());


//        if(power<Minimum){
//                this.Worst=currentPlayer;
//                this.Minimum=power;
//            }
//        this.currentPlayer.setState(new Waiting());
//        this.updateSates();
    }



    /// da la potenza motrice
    @Override
    public void checkMovement(int movement, int numofDouble) {
//        double movement= currentPlayer.getMyPlance().getEnginePower(coordinates);
        this.currentmovement=movement;
        this.energyUsage=numofDouble;
        this.currentPlayer.setState(new ConsumingEnergy());

//
//        if(movement<Minimum){
//            this.Worst=currentPlayer;
//            this.Minimum=movement;
//        }
//        this.currentPlayer.setState(new Waiting());
//        this.updateSates();

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
        if(true) {
            this.checkStrength();
        }
        else{
            this.checkSpeed();
        }
    }

    public void checkStrength(){
        if(this.currentpower<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=this.currentpower;
        }
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }

    public void checkSpeed(){
        if(this.currentmovement<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=this.currentmovement;
        }
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }



    public void checkPeople() {
        int Order=0;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        int PlayerPower;


        for(int i=0; i<PlayerList.size(); i++){
            CurrentPlanche=PlayerList.get(i).getmyPlayerBoard(); // get the current active planche




            Tile TileBoard[][]=CurrentPlanche.getPlayerBoard();
            int totHumans = CurrentPlanche.getNumHumans();


//            for (int j = 0; i < HousingCoords.size(); j++) {
//                //somma per vedere il tot umani
//                totHumans += TileBoard[HousingCoords.get(j).getFirst()][HousingCoords.get(j).getSecond()].getComponent().getAbility();
//            }


            if(totHumans<Minimum){
                Worst=PlayerList.get(i);
                Minimum=totHumans;
            }
        }
        this.PlayerOrder=PlayerList.size();
        this.updateSates();
    }



    public void loseTime() {
        this.getBoard().movePlayer(Worst.GetID(),this.PunishmentMovement);
        return;
    }


    public void loseCargo() {
//
//
//        for(int i=0;i<PunishmentCargo;i++){
//            int index=Worst.getGoodsIndex();
//            IntegerPair coord=Worst.getGoodsCoordinates();
//
//            Worst.getMyPlance().removeGood(coord,index);
//        }
    }



    @Override
    public void killHumans(ArrayList<IntegerPair> coordinates){
        if (coordinates.size() != this.PunishmentHumans) {

            throw new WrongNumofHumansException("Hai sbagliato il numero di umani");
        }

        PlayerBoard curr= Worst.getmyPlayerBoard();
        Tile tiles[][]=curr.getPlayerBoard();
        for (IntegerPair coordinate : coordinates) {
            System.out.println("killing humans in "+coordinate.getFirst()+" "+coordinate.getSecond());

            curr.performAction(tiles[coordinate.getFirst()][coordinate.getSecond()].getComponent(),new KillCrewAction(curr), Worst.getPlayerState());
        }
        this.updateSates();
    }


    @Override
    public void continueCard() {
        int Movement;
        boolean shotsFlag= false;
        while (this.ShotsOrder < PunishmentShots.size() && shotsFlag == false) {

            PlayerBoard CurrentPlanche = Worst.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            if (PunishmentShots.get(ShotsOrder) == 0) { //sinistra
                Movement = 0;
                while (Movement < 10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                        }
                        else {//colpo piccolo
                            Worst.setState(new DefendingFromSmall());
                        }
                    }


                    Movement++;
                }
            }
            if (PunishmentShots.get(ShotsOrder) == 1) {//sopra
                Movement = 0;
                while (Movement < 10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                        }
                        else {//colpo piccolo
                            Worst.setState(new DefendingFromSmall());
                        }

                    }

                    Movement++;
                }
            }
            if (PunishmentShots.get(ShotsOrder) == 2) {// destra
                Movement = 9;
                while (Movement >= 0 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder/2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());
                        }
                        else {//colpo piccolo
                            Worst.setState(new DefendingFromSmall());
                        }

                    }
                    Movement--;
                }

            }
            else { //sotto
                Movement = 9;
                while (Movement >= 0 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {
                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
                            CurrentPlanche.destroy(hit.getFirst(), hit.getSecond());

                            /// IMPORTANTE
                        /// /devo far eil controllo validity, se invalido va in scelta tronconi, altrimenti va avanti
                            /// deve andare in scelta tronconi
                        }
                        else {//colpo piccolo
                            Worst.setState(new DefendingFromSmall());
                        }
                    }


                    Movement--;
                }

            }

            this.ShotsOrder += 2;
        }
        if(this.ShotsOrder >=PunishmentShots.size() ){
            this.ShotsOrder = 0;
            this.updateSates();
        }
    }


    @Override
    public void DefendFromSmall(IntegerPair energy){
        PlayerBoard currentBoard =this.Worst.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if (PunishmentShots.get(ShotsOrder + 1) == 0 && (currentBoard.getShield()[PunishmentShots.get(ShotsOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side");
            }
            else {
                currentBoard.performAction(tiles[hit.getFirst()][hit.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());
            }
        }
        else {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            if (currentBoard.getBroken()){
                this.currentPlayer.setState(new HandleDestruction());
                return;
            }
        }
        this.continueCard();
    }


    @Override
    public void keepGoing(){
        continueCard();
    }





    //json required
    public Warzone() {}
    public int[] getRequirementsType() {
        return RequirementsType;
    }
    public void setRequirementsType(int requirementsType) {RequirementsType = new int[]{requirementsType};}
    public int[] getPunishmentType() {
        return PunishmentType;
    }
    public void setPunishmentType(int punishmentType) {
        PunishmentType = new int[]{punishmentType};
    }
    public int getPunishmentMovement() {
        return PunishmentMovement;
    }
    public void setPunishmentMovement(int punishmentMovement) {
        PunishmentMovement = punishmentMovement;
    }
    public int getPunishmentHumans() {
        return PunishmentHumans;
    }
    public void setPunishmentHumans(int punishmentHumans) {
        PunishmentHumans = punishmentHumans;
    }
    public int getPunishmentCargo() {
        return PunishmentCargo;
    }
    public void setPunishmentCargo(int punishmentCargo) {
        PunishmentCargo = punishmentCargo;
    }
    public ArrayList<Integer> getPunishmentShots() {
        return PunishmentShots;
    }
    public void setPunishmentShots(ArrayList<Integer> punishmentShots) {
        PunishmentShots = punishmentShots;
    }
}