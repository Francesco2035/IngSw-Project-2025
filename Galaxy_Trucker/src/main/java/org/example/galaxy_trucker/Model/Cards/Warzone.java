package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
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



    public Warzone(int level, int time, GameBoard board, int RequirementOrder[], int PunishmentOrder[], int Punishment1, int Punishment2, int Punishment3, ArrayList<Integer> Punishment4) {
        super(level, time, board);
        RequirementsType=RequirementOrder;
        PunishmentType=PunishmentOrder;
        PunishmentMovement=Punishment1;
        PunishmentHumans=Punishment2;
        PunishmentCargo=Punishment3;
        PunishmentShots=Punishment4;


    }




    //controlli su chi è il peggiore




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