package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.io.Serializable;
import java.util.ArrayList;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "Card"
)


@JsonSubTypes({
        @JsonSubTypes.Type(value = Slavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = Smugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = Pirates.class, name = "Pirates"),
        @JsonSubTypes.Type(value = SolarSystem.class, name = "SolarSystem"),
        @JsonSubTypes.Type(value = AbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = AbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = Meteorites.class, name = "Meteorites"),
        @JsonSubTypes.Type(value = OpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = Warzone.class, name = "Warzone"),
        @JsonSubTypes.Type(value = Stardust.class, name = "Stardust"),
        @JsonSubTypes.Type(value = Epidemic.class, name = "Epidemic")
})





public class Card implements Serializable {
    private int id;
    @JsonProperty("Level")
    private int Level;
    @JsonProperty("Time")
    private int Time;
    private GameBoard Board;
    // IMPORTANTE ESERCITATORE DICE DI ATTIVARE EFFETTI DELLE CARTE A POSTERIORI DELLE SCELTE DEI PLAYER
    // NON SO SE HA SENSO MA LUI DICE DI FARE COSI, QUINDI I METODI DI CARD EFFECT DOVREBBERO ESSERE
    //INTERAMENTE DEFINITI SENZA CHIEDERE ALTRI IMPUT AI PLAYER, SEMPLICEMENTE MODIFICHERANNO IL MODELLO

    // puoi invece che salvarti come attributo la GameBoard posso passarla per risparmiare in memoria ma non è un big problema  :)
    public Card(int level, int time, GameBoard board) {
        this.Level = level;
        this.Time = time;
        this.Board = board;
    }

//    public GameBoard getBoard() {return this.Board;}


    //toOverride
    public void CardEffect(){}
    public int getTime() {
        return this.Time;
    }
    public GameBoard getBoard() {
        return this.Board;
    }
    public void updateSates(){}
    public void finishCard() {}
    public void continueCard(boolean accepted) {}
    public void continueCard(double power) {}
    public void continueCard(int power) {}
    public void continueCard(ArrayList<IntegerPair> coordinates, boolean accepted) {}
    public void DefendFromMeteorites(IntegerPair CannonCoord, IntegerPair ShieldCoord, IntegerPair EnergyStorage) {}
    public void DefendFromShots(IntegerPair coordinates) {}
    public void continueCard(ArrayList<IntegerPair> coordinates) {}
    public void continueCard(){}
    public void killHumans(ArrayList<IntegerPair> coordinates) {}
    public void choosePlanet(int planet, boolean accepted){}

    public void ActivateCard(){
    }


    //json required
    public Card() {}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getLevel() {return Level;}
    public void setLevel(int level) {Level = level;}
    public void setTime(int time) {Time = time;}
    public void setBoard(GameBoard board) {Board = board;}
}