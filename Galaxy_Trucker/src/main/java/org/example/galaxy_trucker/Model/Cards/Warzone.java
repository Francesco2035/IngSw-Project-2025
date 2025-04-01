package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.GetterHandler.EngineGetter;
import org.example.galaxy_trucker.Model.Boards.GetterHandler.PlasmaDrillsGetter;
import org.example.galaxy_trucker.Model.InputHandlers.GiveAttack;
import org.example.galaxy_trucker.Model.InputHandlers.GiveSpeed;
import org.example.galaxy_trucker.Model.InputHandlers.Killing;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Boards.SetterHandler.HousingUnitSetter;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
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
        this.Minimum = 0;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;
        this.lines = new int[PunishmentShots.size()/2];
        for(int i=0;i< PunishmentShots.size()/2;i++){
            lines[i] = this.getBoard().getPlayers().getFirst().RollDice();
        }
        this.hit = null;


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
        if(this.PlayerOrder<PlayerList.size()){
            currentPlayer = PlayerList.get(this.PlayerOrder);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();
            if(RequirementsType[ChallengeOrder]==1){
                this.Minimum=1000000;
                this.currentPlayer.setState(PlayerStates.GiveAttack);
                this.currentPlayer.setInputHandler(new GiveAttack(this));

            }
            if(RequirementsType[ChallengeOrder]==2){
                this.Minimum=1000000;
                this.currentPlayer.setState(PlayerStates.GiveSpeed);
                this.currentPlayer.setInputHandler(new GiveSpeed(this));

            }
            else{
                this.Minimum=1000000;
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
                this.currentPlayer.setState(PlayerStates.Killing);
                this.currentPlayer.setInputHandler(new Killing(this));
            }
            else if(this.PunishmentType[ChallengeOrder]==3){
              //  chiamo il metodo di fottitura eterna :)
            }
            else {
                this.continueCard();
            }
            this.ChallengeOrder++;
        }
    }

    @Override
    public  void  ActivateCard() {
        currentPlayer.getInputHandler().action();
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.done==PlayerList.size()) {
            for (int i = 0; i < PlayerList.size(); i++) {
                PlayerList.get(i).setState(PlayerStates.BaseState);
            }
        }
        else{
            done++;
        }
    }

    @Override
    public void continueCard(ArrayList<IntegerPair> coordinates) {
        if (RequirementsType[ChallengeOrder]==1){
            checkPower(coordinates);
        }
        else {
            checkMovement(coordinates);
        }
    }






    //controlli su chi è il peggiore

    public void checkPower(ArrayList<IntegerPair> coordinates) {
//            double movement= currentPlayer.getMyPlance().getPower(coordinates);

        currentPlayer.getMyPlance().setGetter(new PlasmaDrillsGetter(currentPlayer.getMyPlance(), coordinates));
        double power = ((Double) currentPlayer.getMyPlance().getGetter().get());


        if(power<Minimum){
                this.Worst=currentPlayer;
                this.Minimum=power;
            }
        this.currentPlayer.setState(PlayerStates.Waiting);
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
            CurrentPlanche=PlayerList.get(i).getMyPlance(); // get the current active planche


            ArrayList<IntegerPair> HousingCoords=new ArrayList<>();
            if(CurrentPlanche.getClassifiedTiles().containsKey(ModularHousingUnit.class)) {
                HousingCoords = CurrentPlanche.getClassifiedTiles().get(ModularHousingUnit.class);
            }
            if(CurrentPlanche.getValidPlayerBoard()[6][6]==1) {
                HousingCoords.add(new IntegerPair(6,6));
            }

            Tile TileBoard[][]=CurrentPlanche.getPlayerBoard();
            int totHumans = 0;


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

    public void checkMovement(ArrayList<IntegerPair> coordinates) {
//        double movement= currentPlayer.getMyPlance().getEnginePower(coordinates);


        currentPlayer.getMyPlance().setGetter(new EngineGetter(currentPlayer.getMyPlance(),
                coordinates));
        double movement = ((Double) currentPlayer.getMyPlance().getGetter().get());


        if(movement<Minimum){
            this.Worst=currentPlayer;
            this.Minimum=movement;
        }
        this.currentPlayer.setState(PlayerStates.Waiting);
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
            //devo dirgli che ha scelto il num sbagliato di persone da shottare
            //throw new Exception();
        }

        for (IntegerPair coordinate : coordinates) {
            currentPlayer.getMyPlance().setSetter(new HousingUnitSetter(currentPlayer.getMyPlance(),
                    coordinate, 1, true, true));
            currentPlayer.getMyPlance().getSetter().set();
        }
        this.updateSates();
    }


    @Override
    public void continueCard() {
        int Movement;
        boolean shotsFlag= false;
        while (this.ShotsOrder < PunishmentShots.size() && shotsFlag == false) {

            PlayerBoard CurrentPlanche = currentPlayer.getMyPlance(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            if (PunishmentShots.get(ShotsOrder) == 0) { //sinistra
                Movement = 0;
                while (Movement < 10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                        shotsFlag = true;
                        hit.setValue(Movement, lines[ShotsOrder / 2]);
                        currentPlayer.setState(PlayerStates.DefendingFromShots);
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
                        currentPlayer.setState(PlayerStates.DefendingFromShots);

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
                        currentPlayer.setState(PlayerStates.DefendingFromShots);

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
                        currentPlayer.setState(PlayerStates.DefendingFromShots);
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
    public void DefendFromShots(IntegerPair coordinates) {
        PlayerBoard currentBoard =this.currentPlayer.getMyPlance();
        Tile[][] tiles =currentBoard.getPlayerBoard();

        if(coordinates !=null) {
//            if (!(PunishmentShots.get(ShotsOrder + 1) == 0 && (currentBoard.getTile(coordinates.getFirst(), coordinates.getSecond()).getComponent().getAbility(0).contains(PunishmentShots.get(ShotsOrder))) || PunishmentShots.get(ShotsOrder + 1) == 1)) {
//                // non dovrei attivare lo scudo o lo scudo è sbagliato
//            }
        }
        if(coordinates ==null) { // se sono entrambi nulli non mi son difeso quindi vengo colpito
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
        }
        this.continueCard();
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