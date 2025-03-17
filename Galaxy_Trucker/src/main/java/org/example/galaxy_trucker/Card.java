package org.example.galaxy_trucker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;



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




public class Card {
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


    //json required
    public Card() {}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getLevel() {return Level;}
    public void setLevel(int level) {Level = level;}
    public void setTime(int time) {Time = time;}
    public void setBoard(GameBoard board) {Board = board;}
}