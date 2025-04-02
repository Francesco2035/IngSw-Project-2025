package org.example.galaxy_trucker.Model.Cards;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.InputHandlers.ChoosingPlanet;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.Random;

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
            p.setState(PlayerStates.Waiting);
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

            this.currentPlayer.setState(PlayerStates.ChoosingPlanet);
            this.currentPlayer.setInputHandler(new ChoosingPlanet(this));

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
    public void choosePlanet(int planet, boolean accepted){
        if(accepted) {
            if (this.planets.get(planet).isOccupied()) {
                //mando un avviso e richiedo
                this.currentPlayer.setState(PlayerStates.ChoosingPlanet);
                this.currentPlayer.setInputHandler(new ChoosingPlanet(this));
            } else {
                this.planets.get(planet).setOccupied(this.currentPlayer);
                this.updateSates();
            }
        }
        else{
            this.finishCard();
        }
    }




    //json required
    public SolarSystem() {}
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

