package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
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
    private ConcurrentCardListener concurrentCardListener;
    private int id;
    @JsonProperty("Level")
    private int Level;
    @JsonProperty("Time")
    private int Time;
    private GameBoard Board;
    private int DefaultPunishment;
    private  boolean finished;
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
    public void setConcurrentCardListener(ConcurrentCardListener listener){
        this.concurrentCardListener = listener;
    }
    public void removeConcurrentCardListener(){
        this.concurrentCardListener = null;
    }
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
    public void checkPower(double power, int numofDouble) {}
    public void checkMovement(int power, int numofDouble) {}
    public void continueCard(ArrayList<IntegerPair> coordinates, boolean accepted) {}
    public void DefendFromLarge(IntegerPair CannonCoord,IntegerPair EnergyStorage) {}
    public void DefendFromSmall(IntegerPair energy) {}
    public void continueCard(ArrayList<IntegerPair> coordinates) {}
    public void continueCard(){}
    public void killHumans(ArrayList<IntegerPair> coordinates) {}
    public void choosePlanet(int planet){}
    public void keepGoing(){}
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {}
    public void loseCargo(IntegerPair pair, int position){}
    public void ActivateCard(){}
    public int getDefaultPunishment(){return DefaultPunishment;}
    public void setDefaultPunishment(int p){DefaultPunishment = p;}

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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