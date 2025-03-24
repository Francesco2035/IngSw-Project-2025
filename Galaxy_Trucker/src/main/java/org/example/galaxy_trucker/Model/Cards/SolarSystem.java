package org.example.galaxy_trucker.Model.Cards;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Player;

import java.util.Random;

import java.util.ArrayList;

public class SolarSystem extends Card {
    @JsonProperty ("Planets")
    private ArrayList<Planet> planets;

    public SolarSystem(int lv, int time, GameBoard board, ArrayList<Planet> planets) {
        super(lv, time,board);
        planets = planets;

    }


    //json required
    public SolarSystem() {}
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

