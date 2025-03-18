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
    @Override
    public void CardEffect(){
        int SolarSystemOrder=0;
        boolean SolarSystemBool=true;
        GameBoard SolarSystemBoard=this.getBoard();
        ArrayList<Player> SolarSystemPlayerList = SolarSystemBoard.getPlayers();

        Random SolarSystemR= new Random();

        while(SolarSystemPlayerList.size()>SolarSystemOrder){
            //Ask.Descent O qualche cosa che chieda il pianeta di discesa al player se vuol scendere

            //per adesso metto un numero random
            int yes =SolarSystemR.nextInt(2); // per ora salvo sia la volontà di scendere e il pianeta,
            // potrei chiamare un metodo che semplicemente chiama un pianeta nullo dove va chi non vuole o non può scendere
            //rimuovendo la necessità di salvare due parametri
            int NumPlanet=SolarSystemR.nextInt(planets.size());
            while(yes==1 && planets.get(NumPlanet).isOccupied()){
                //avviso il player di cambiare idea perché il pianeta è occupato
            }
            if(yes==1) {
               // SolarSystemPlayerList.get(SolarSystemOrder).movePlayer(this.getTime());
               SolarSystemPlayerList.get(SolarSystemOrder).handleCargo(planets.get(NumPlanet).getGoods());
            }
            SolarSystemOrder++;
        }
    }


    //json required
    public SolarSystem() {}
    public ArrayList<Planet> getSolarSystemPlanets() {return planets;}
    public void setSolarSystemPlanets(ArrayList<Planet> solarSystemPlanets) {this.planets = solarSystemPlanets;}

}

