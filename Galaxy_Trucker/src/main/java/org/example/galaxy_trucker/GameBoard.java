package org.example.galaxy_trucker;

import java.lang.*;
import java.util.ArrayList;


public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Player> players;



    public GameBoard() {
        this.players = new ArrayList<>();

    }

    public void addPlayer(String id){
        GameBoard board = new GameBoard();
        this.players.add(new Player (id, 2, board));
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }


}
