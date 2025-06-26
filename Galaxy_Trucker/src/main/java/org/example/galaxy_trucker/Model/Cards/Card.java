package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Controller.Listeners.RandomCardEffectListener;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Controller.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


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
    public ConcurrentCardListener concurrentCardListener;
    public HashMap<String, RandomCardEffectListener> RandomCardEffectListeners = new HashMap<>();


    private  final Object lock = new Object();

    public  Object getLock() {
        return lock;
    }

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

    public void checkLosers(){
        System.out.println("ODDIO CHECK LOSERS");
        ArrayList<Player> losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        losers.removeAll(getBoard().checkDoubleLap());   // così non ho doppioni :3
        losers.addAll(getBoard().checkDoubleLap());
        for(Player p: losers){
            getBoard().abandonRace(p, "You have been doubled",true);
        }

        losers.clear();
        PlayerList = Board.getPlayers();
        for (Player p : PlayerList){
            if (p.getmyPlayerBoard().getNumHumans() == 0){
                losers.add(p);
            }
        }

        for(Player p: losers){
            getBoard().abandonRace(p, "No crew left",true);
        }

    }


    //toOverride
    public void setConcurrentCardListener(ConcurrentCardListener listener){
        this.concurrentCardListener = listener;
        this.concurrentCardListener.onConcurrentCard(false);

    }
    public void removeConcurrentCardListener(){
        this.concurrentCardListener = null;
    }
    public void CardEffect() throws InterruptedException {}
    public int getTime() {
        return this.Time;
    }
    public GameBoard getBoard() {
        return this.Board;
    }
    public void updateStates() throws InterruptedException {}
    public void finishCard() {}
    public void continueCard(boolean accepted) {}
    public void checkPower(double power, int numofDouble) throws InterruptedException {}
    public void checkMovement(int power, int numofDouble) throws InterruptedException {}
    public void continueCard(ArrayList<IntegerPair> coordinates, boolean accepted) {}
    public void DefendFromLarge(IntegerPair CannonCoord, IntegerPair EnergyStorage, Player player) throws InterruptedException {}
    public void DefendFromSmall(IntegerPair energy, Player player) throws InterruptedException {}
    public void continueCard(ArrayList<IntegerPair> coordinates) {}
    public void continueCard() throws InterruptedException {}
    public void killHumans(ArrayList<IntegerPair> coordinates) throws InterruptedException {}
    public void choosePlanet(int planet){}
    public void keepGoing() throws InterruptedException {}
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) throws InterruptedException {}
    public void loseCargo(IntegerPair pair, int position) throws InterruptedException {}
    public void ActivateCard(){}

    /// todo la getDefault riesce in qualche modo a dare un risultato diverso da default
    public int getDefaultPunishment(){
        int r =this.DefaultPunishment;
        return r;
    }
    public void setDefaultPunishment(int p){DefaultPunishment = p;}

    public ConcurrentCardListener getConcurrentCardListener() {
        return concurrentCardListener;
    }

    public boolean isFinished() {
        return finished;
    }

    public void sendTypeLog(){

    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setRandomCardEffectListeners(String id, RandomCardEffectListener randomCardEffectListener) {
        System.out.println("####Setto listener carta "+id+" "+randomCardEffectListener);
        getRandomCardEffectListeners().put(id, randomCardEffectListener);
    }


    /// usa questo per mandare notifiche al client lezgo
    public void sendRandomEffect(String playerid, LogEvent randomCardEffectEvent) {
        //System.out.println("invio a "+playerid+" "+randomCardEffectEvent.message());
        if(getRandomCardEffectListeners().get(playerid) != null) {
            getRandomCardEffectListeners().get(playerid).Effect(randomCardEffectEvent);
        }
        else{
            System.out.println("the value of the card listener was Null if its not a test this is an issue ");
        }
    }

    public HashMap<String, RandomCardEffectListener> getRandomCardEffectListeners() {
        return RandomCardEffectListeners;
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