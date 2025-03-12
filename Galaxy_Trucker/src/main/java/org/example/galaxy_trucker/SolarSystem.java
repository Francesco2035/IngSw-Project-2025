package org.example.galaxy_trucker;
import java.util.Random;

import java.util.ArrayList;

public class SolarSystem extends Card {
    private ArrayList<Planet>  SolarSystemPlanets;

    public SolarSystem(int lv, int time, GameBoard board, ArrayList<Planet> planets) {
        super(lv, time,board);
        SolarSystemPlanets = planets;

    }
    public void CardEffect(){
        int SolarSystemOrder=0;
        boolean SolarSystemBool=true;
        GameBoard SolarSystemBoard=this.getBoard();
        ArrayList<Player> SolarSystemPlayerList = SolarSystemBoard.getPlayers();
        PlayerPlance SolarSystemCurrentPlanche;
        Random SolarSystemR= new Random();

        while(SolarSystemPlayerList.size()>SolarSystemOrder){
            //Ask.Descent O qualche cosa che chieda il pianeta di discesa al player

            //per adesso metto un numero random
            SolarSystemR.nextInt(SolarSystemPlanets.size());

            SolarSystemOrder++;
        }
    }
}
