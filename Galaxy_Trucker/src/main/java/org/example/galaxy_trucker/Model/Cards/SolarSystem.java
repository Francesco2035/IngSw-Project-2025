package org.example.galaxy_trucker.Model.Cards;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.WrongPlanetExeption;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.ChoosingPlanet;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import java.util.ArrayList;

public class SolarSystem extends Card {
    @JsonProperty ("Planets")
    private ArrayList<Planet> planets;
    private  Player currentPlayer;
    private int order;
    private int done;

    public SolarSystem(int lv, int time, GameBoard board, ArrayList<Planet> planets) {
        super(lv, time,board);
        this.planets = planets;
        this.order = 0;
        this.currentPlayer = null;
        this.done = 0;
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
        if(this.order<PlayerList.size()){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

            this.currentPlayer.setState(new ChoosingPlanet());
            //this.currentPlayer.setInputHandler(new ChoosingPlanet(this));

            this.order++;
        }
        else{
            for(Planet p: this.planets){
                if(p.isOccupied()){
                    this.getBoard().movePlayer(p.getOccupied().GetID(), this.getTime());
                    p.getOccupied().handleCargo(p.getGoods());
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




    //json required
    public SolarSystem() {}
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

