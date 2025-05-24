package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.*;
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

///ordine controlli: 1 cannoni,  2 movimento, 3 umani
///
/// CONTROLLA IL JSON TI SCONGIURO
///
///ordine punizioni: 1 movimento, 2 umani, 3 cargo, 4 spari

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





    /// caso base è che non attiva nulla, e per le punizioni prendi le prime cose che trovi e non si difende
    public Warzone(int level, int time, GameBoard board, int RequirementOrder[], int PunishmentOrder[], int Punishment1, int Punishment2, int Punishment3, ArrayList<Integer> Punishment4) {
        super(level, time, board);
        RequirementsType=RequirementOrder;
        PunishmentType=PunishmentOrder;
        PunishmentMovement=Punishment1;
        PunishmentHumans=Punishment2;
        PunishmentCargo=Punishment3;
        this.PunishmentShots=Punishment4;

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
        this.hit = new IntegerPair(0,0);
        this.currentpower=0;
        this.currentmovement=0;


    }



    @Override
    public void CardEffect(){
        if (this.hit==null) {
            this.hit = new IntegerPair(0,0);

        }
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateSates();
    }


    @Override
    public void updateSates(){
        if(ChallengeOrder<3) {
            System.out.println("challenge number: " + this.ChallengeOrder);
            GameBoard Board = this.getBoard();
            ArrayList<Player> PlayerList = Board.getPlayers();

            if (this.PlayerOrder < PlayerList.size()) {
                if (currentPlayer != null) {
                    currentPlayer.setState(new Waiting());
                }
                currentPlayer = PlayerList.get(this.PlayerOrder);
                PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard();
                if (RequirementsType[ChallengeOrder] == 1) {
                    System.out.println("checking attack of: " + currentPlayer.GetID());
                    this.currentPlayer.setState(new GiveAttack());
                    //this.currentPlayer.setInputHandler(new GiveAttack(this));

                } else if (RequirementsType[ChallengeOrder] == 2) {
                    System.out.println("checking speed of: " + currentPlayer.GetID());
                    this.currentPlayer.setState(new GiveSpeed());
                    //this.currentPlayer.setInputHandler(new GiveSpeed(this));

                } else { //problema qui:)
                    System.out.println("checking people");
                    this.checkPeople();
                }


                this.PlayerOrder++;
            } else {
                System.out.println("the worst is: " + Worst.GetID());
                this.PlayerOrder = 0;
                if (this.PunishmentType[ChallengeOrder] == 1) {
                    this.Minimum = 100000;
                    this.ChallengeOrder++;
                    this.loseTime();
                    return; // serve perché lose time ri attiva update states al completamento quinid devo fare finta di averlo finito prima di chiamarlo e poi terminarlo appena è finitra la chiamat
                } else if (this.PunishmentType[ChallengeOrder] == 2) {
                    System.out.println(Worst.GetID() + " has to kill" + this.PunishmentHumans);
                    this.setDefaultPunishment(this.PunishmentHumans);
                    this.Worst.setState(new Killing());
                    //this.currentPlayer.setInputHandler(new Killing(this));
                } else if (this.PunishmentType[ChallengeOrder] == 3) {
                    //  chiamo il metodo di fottitura eterna :)
                } else {
                    this.Minimum = 1000000;
                    this.ChallengeOrder++;
                    this.continueCard();
                    return; // stessa cosa di lose time dato che ci torno automaticamente incremento qui che è meglio
                }
                this.Minimum = 1000000;
                this.ChallengeOrder++;
            }
        }
        else{
            System.out.println("all challenges completed");
            this.finishCard();
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
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        System.out.println("card finished");
        this.setFinished(true);
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
        if(numofDouble==0){
            this.checkStrength();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
        }

    }



    /// da la potenza motrice
    @Override
    public void checkMovement(int movement, int numofDouble) {
//        double movement= currentPlayer.getMyPlance().getEnginePower(coordinates);
        this.currentmovement=movement;
        this.energyUsage=numofDouble;
        if(numofDouble==0){
            this.checkSpeed();
        }
        else {

            this.currentPlayer.setState(new ConsumingEnergy());
        }

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
        if(coordinates==null) {
            System.out.println("coordinates is null");
            if (RequirementsType[ChallengeOrder] == 1) {
                this.currentPlayer.setState(new GiveAttack());
                System.out.println("going back to checking atttack of " + currentPlayer.GetID());
            } else {
                this.currentPlayer.setState(new GiveSpeed());
                System.out.println("going back to checking speed of " + currentPlayer.GetID());
            }
        }

        if(coordinates.size()!=this.energyUsage){

            if(RequirementsType[ChallengeOrder]==1){


                this.currentPlayer.setState(new GiveAttack());
            System.out.println("going back to checking attack of "+currentPlayer.GetID());
            }
            else {
                this.currentPlayer.setState(new GiveSpeed());
            System.out.println("going back to checking speed of "+currentPlayer.GetID());
            }
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
            ///  devo fare si che in caso di errore torni alla give attack
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
                if(RequirementsType[ChallengeOrder]==1){
                    this.currentPlayer.setState(new GiveAttack());
                    System.out.println("going back to checking attack of "+currentPlayer.GetID());
                }
                else {
                    this.currentPlayer.setState(new GiveSpeed());
                    System.out.println("going back to checking speed of "+currentPlayer.GetID());
                }
                throw new WrongNumofEnergyExeption("no energy to consume");
            }
        }

        if(RequirementsType[ChallengeOrder]==1) {
            this.checkStrength();
        }
        else{
            this.checkSpeed();
        }
    }

    public void checkStrength(){
        System.out.println("checking strength of "+currentPlayer.GetID()+ " strength is "+this.currentpower);
        if(this.currentpower<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=this.currentpower;
            System.out.println(currentPlayer.GetID()+" is the worst, the minimum is now"+this.currentpower);
        }
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }

    public void checkSpeed(){
        System.out.println("checking speed of "+currentPlayer.GetID()+" speed is: "+this.currentmovement);
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
        this.getBoard().movePlayer(Worst.GetID(),-this.PunishmentMovement);
        System.out.println(this.Worst.GetID()+" loses the time");

        this.updateSates();

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

        System.out.println("killing "+PunishmentHumans+" humans of "+Worst.GetID());
        /// worst e non current


        if (coordinates.size() != this.PunishmentHumans) {
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
            throw new WrongNumofHumansException("wrong number of humans");
        }

        ///  fai l try catch e opera sulla copia :)
        PlayerBoard curr= this.Worst.getmyPlayerBoard();
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
    public void continueCard() {

        /// current player va sostituito con worst

        System.out.println("shooting at "+this.Worst.GetID());

        int Movement;
        boolean shotsFlag= false;
        while (this.ShotsOrder < PunishmentShots.size() && shotsFlag == false) {

            System.out.println("attack number: "+this.ShotsOrder/2 +" on: "+Worst.GetID());

            PlayerBoard CurrentPlanche = Worst.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            if (PunishmentShots.get(ShotsOrder) == 0) { //sinistra
                Movement = 0;
                while (Movement < 10  && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
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
            else if (PunishmentShots.get(ShotsOrder) == 1) {//sopra
                Movement = 0;
                while (Movement < 10 && lines[ShotsOrder/2]<10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

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
            else if (PunishmentShots.get(ShotsOrder) == 2) {// destra
                Movement = 9;
                while (Movement >= 0   && lines[ShotsOrder/2]<10&& shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder/2]);
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare

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
                        if(PunishmentShots.get(ShotsOrder+1) == 1){//colpo grande nulla da fare
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
        if(this.ShotsOrder >=PunishmentShots.size() ){
            this.ShotsOrder = 0;
            this.updateSates();
        }
    }


    @Override
    public void DefendFromSmall(IntegerPair energy, Player player){
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if ((currentBoard.getShield()[PunishmentShots.get(ShotsOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+getPunishmentShots().get(ShotsOrder));
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