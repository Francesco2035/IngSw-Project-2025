package org.example.galaxy_trucker.Model.Cards;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Controller.Messages.TileSets.RandomCardEffectEvent;
import org.example.galaxy_trucker.Exceptions.WrongPlanetExeption;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosingPlanet;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import java.util.ArrayList;

public class SolarSystem extends Card {
    @JsonProperty ("Planets")
    private ArrayList<Planet> planets;
    private  Player currentPlayer;
    private int order;
    private int done;
    private  String message;

    ArrayList<Player> losers;



    /// caso base è non scendere sui pianeti
    public SolarSystem(int lv, int time, GameBoard board, ArrayList<Planet> planets) {
        super(lv, time,board);
        this.planets = planets;
        this.order = 0;
        this.currentPlayer = new Player();
        this.done = 0;
    }

    @Override
    public void CardEffect(){
        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        System.out.println("playerlist size " + PlayerList.size());
        for(Player p : PlayerList){
            System.out.println(p.GetID());
            p.setState(new Waiting());
        }
        this.message=" ";
        this.updateSates();
    }


    @Override
    public void updateSates(){
        if (!this.isFinished()){
            GameBoard Board=this.getBoard();
            ArrayList<Player> PlayerList = Board.getPlayers();
            System.out.println("playerlist size (update states) " + PlayerList.size());
            if(this.order<PlayerList.size()){
                if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
                currentPlayer = PlayerList.get(this.order);
                PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

                this.currentPlayer.setState(new ChoosingPlanet());
                this.sendRandomEffect(currentPlayer.GetID(),new RandomCardEffectEvent(message));
                System.out.println(this.currentPlayer.GetID() + " : "+ this.currentPlayer.getPlayerState());
                //this.currentPlayer.setInputHandler(new ChoosingPlanet(this));

                this.order++;
            }
            else{
                for(Player p : PlayerList){
                    p.setState(new Waiting());
                }
                ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
                concurrentCardListener.onConcurrentCard(true);
                for(Planet p: this.planets){
                    if(p.isOccupied()){
                        this.getBoard().movePlayer(p.getOccupied().GetID(), -this.getTime());

                        p.getOccupied().setState(new HandleCargo());
                        p.getOccupied().getmyPlayerBoard().setRewards(p.getGoods());
                    }
                }
            }
            }
    }
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.done==PlayerList.size()-1) {

            for (int i = 0; i < PlayerList.size(); i++) {
                PlayerList.get(i).setState(new BaseState());

            }

            losers.remove(getBoard().checkDoubleLap());/// così non ho doppioni :3
            losers.addAll(getBoard().checkDoubleLap());

            for(Player p: getBoard().getPlayers()){
                if(p.getmyPlayerBoard().getNumHumans()==0){
                    losers.remove(p);
                    losers.add(p);
                }
            }

            for(Player p: losers){
                getBoard().abandonRace(p);
            }

            System.out.println("card finished");
            this.setFinished(true);
        }
        else{
            done++;
        }
    }


    @Override
    public void choosePlanet(int planet){
        if(planet>-1) {
            if (planet>=planets.size()) {
                this.currentPlayer.setState(new ChoosingPlanet());
                throw new WrongPlanetExeption("this planet doesn't exist");
            }
            if (this.planets.get(planet).isOccupied()) {
                this.currentPlayer.setState(new ChoosingPlanet());
                throw new WrongPlanetExeption("this planet is already ocupied");

                //this.currentPlayer.setInputHandler(new ChoosingPlanet(this));
            } else {
                this.planets.get(planet).setOccupied(this.currentPlayer);
                message=message+currentPlayer.GetID()+"has chosen planet number "+planet+"\n";
                this.updateSates();
            }
        }
        else{
            this.finishCard();
            //dovrebbe non dare problemi ne se l'ultimo rifiuta il pianeta ne se tutti rifiutano
            // se  l'ultimo rifiuta dovrebbe chiamare update come se avesse accettato cioe scorre l lista di chi ha accettato e li manda in handle cargo
            // se tutti dicono di no tecnicamente fa prima finish card e poi fa anche update state che però non dovrebbe fare nulla
            this.updateSates();
        }
    }

    @Override
    public void keepGoing(){
        this.finishCard();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json required
    public SolarSystem() {}
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

