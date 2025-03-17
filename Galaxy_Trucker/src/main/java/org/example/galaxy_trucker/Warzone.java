package org.example.galaxy_trucker;


import com.fasterxml.jackson.annotation.JsonProperty;

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



    public Warzone(int level, int time, GameBoard board, int[] RequirementOrder, int[] PunishmentOrder, int Punishment1, int Punishment2, int Punishment3, ArrayList<Integer> Punishment4) {
        super(level, time, board);
        RequirementsType=RequirementOrder;
        PunishmentType=PunishmentOrder;
        PunishmentMovement=Punishment1;
        PunishmentHumans=Punishment2;
        PunishmentCargo=Punishment3;
        PunishmentShots=Punishment4;


    }

//    @Override
//    public void CardEffect() {
//        GameBoard Board=this.getBoard();
//        ArrayList<Player> PlayerList = Board.getPlayers();
//        Player MinimumPlayer=PlayerList.get(0);
//        for(int i=0; i<RequirementsType.length; i++){
//            if(RequirementsType[i]==1){
//                MinimumPlayer= this.checkPower();
//            }
//            else if(RequirementsType[i]==2){
//                MinimumPlayer=this.checkPeople();
//            }
//            else if(RequirementsType[i]==3){
//                MinimumPlayer=this.checkMovement();
//            }
//
//            if(PunishmentType[i]==1){
//                this.loseTime(MinimumPlayer);
//            }
//            else if(PunishmentType[i]==2){
//                this.losePeople(MinimumPlayer);
//            }
//            else if(PunishmentType[i]==3){
//                this.loseCargo(MinimumPlayer);
//            }
//            else{
//                this.getShot(MinimumPlayer);
//            }
//        }
//        return;
//    }

    //controlli su chi è il peggiore
//
//    public Player checkPower() {
//        int Order=0;
//        GameBoard Board=this.getBoard();
//        ArrayList<Player> PlayerList = Board.getPlayers();
//        PlayerPlance CurrentPlanche;
//        int Len= PlayerList.size(); // quanti player ho
//        int PlayerPower;
//        Player Worst=PlayerList.get(0);
//        int Minimum=1000000;
//
//        for(int i=0; i<PlayerList.size(); i++){
//            if(PlayerList.get(i).getPower()<Minimum){
//                Worst=PlayerList.get(i);
//                Minimum=PlayerList.get(i).getPower();
//            }
//        }
//        return Worst;
//    }
//
//    public Player checkPeople() {
//        int Order=0;
//        GameBoard Board=this.getBoard();
//        ArrayList<Player> PlayerList = Board.getPlayers();
//        PlayerPlance CurrentPlanche;
//        int Len= PlayerList.size(); // quanti player ho
//        int PlayerPower;
//        Player Worst=PlayerList.get(0);
//        int Minimum=1000000;
//
//        for(int i=0; i<PlayerList.size(); i++){
//            if(PlayerList.get(i).getPower()<Minimum){
//                Worst=PlayerList.get(i);
//                Minimum=PlayerList.get(i).getHumans();
//            }
//        }
//        return Worst;
//    }
//
//    public Player checkMovement() {
//        int Order=0;
//        GameBoard Board=this.getBoard();
//        ArrayList<Player> PlayerList = Board.getPlayers();
//        PlayerPlance CurrentPlanche;
//        int Len= PlayerList.size(); // quanti player ho
//        int PlayerPower;
//        Player Worst=PlayerList.get(0);
//        int Minimum=1000000;
//
//        for(int i=0; i<PlayerList.size(); i++){
//            if(PlayerList.get(i).getPower()<Minimum){
//                Worst=PlayerList.get(i);
//                Minimum=PlayerList.get(i).getMovement();
//            }
//        }
//        return Worst;
//    }
//    public void loseTime(Player Worst) {
//        Worst.movePlayer(PunishmentMovement);
//        return;
//    }
//    public void loseCargo(Player Worst) {
//        Worst.loseCargo(PunishmentCargo);
//    }
//    public void losePeople(Player Worst) {
//        Worst.killHumans(PunishmentHumans);
//
//    }
//    public  void getShot(Player Worst) {
//        int Order=0;
//        int AttackNumber=0;
//        boolean Flag=true;
//        boolean Attacked=false;
//        GameBoard Board=this.getBoard();
//        ArrayList<Player> PlayerList = Board.getPlayers();
//        PlayerPlance CurrentPlanche;
//        int Len= PlayerList.size();
//        int[][] ValidPlanche;
//        int Movement;
//        int Lines [] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int Line; // gli attacchi son fissi per tutti i player quindi tiro già la sequenza di dadi
//        for(int i=0;i<PunishmentShots.size();i++){
//            Lines[i]=PlayerList.get(0).RollDice();
//        }
//        CurrentPlanche=Worst.getMyPlance();
//        ValidPlanche=CurrentPlanche.getValidPlance(); //prendo la planche da controllare
//        AttackNumber=0;
//
//        while(PunishmentShots.size()>AttackNumber) { // faccio tutti gli atttacchi
//            Line=Lines[AttackNumber]; // prendo la linea da attaccà
//
//            if (PunishmentShots.get(AttackNumber).getFirst() == 0) { //sinistra
//                Movement = 0;
//
//                while (Movement < 10 && Attacked == false) {
//                    if (ValidPlanche[Line][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                        //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
//                        Attacked = true;
//                    }
//
//                    Movement++;
//                }
//            }
//            if (PunishmentShots.get(AttackNumber).getFirst() == 1) {//sopra
//                Movement = 0;
//                while (Movement < 10 && Attacked == false) {
//                    if (ValidPlanche[Movement][Line] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                        //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
//                        Attacked  = true;
//                    }
//
//                    Movement++;
//                }
//            }
//            if (PunishmentShots.get(AttackNumber).getFirst() == 2) {// destra
//                Movement = 9;
//                while (Movement >= 0 && Attacked == false) {
//                    if (ValidPlanche[Line][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                        //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
//                        Attacked = true;
//                    }
//                }
//                Movement--;
//            } else { //sotto
//                Movement = 9;
//                while (Movement >= 0 && Attacked == false) {
//                    if (ValidPlanche[Movement][Line] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                        //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
//                        Attacked = true;
//                    }
//
//                    Movement--;
//                }
//
//            }
//            AttackNumber++;
//        }
//
//
//    }



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
