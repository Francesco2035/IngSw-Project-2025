package org.example.galaxy_trucker;

import java.lang.*;
import java.util.ArrayList;


public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Player> players;



    public GameBoard() {
        this.players = new ArrayList<>();

    }

    public void addPlayer(){
        this.players.add(new Player(PlayerPlance, String ));
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }


}
