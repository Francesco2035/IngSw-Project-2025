package org.example.galaxy_trucker;

import java.lang.*;
import java.util.ArrayList;


public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Player> players;
    private TileSets tileSets;
    private Hourglass hourglass;
    private int GameLv;


    public GameBoard(TileSets list, int lv) {
        this.players = new ArrayList<>();
        GameLv = lv; //the GameBoard class learns the current game level from Game anc will extend it to players and other objects
        tileSets = list;
        hourglass = new Hourglass(GameLv);
    }

    public void addPlayer(String id){
        this.players.add(new Player (id, this));
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }
    public int getLevel(){return GameLv;}

}
