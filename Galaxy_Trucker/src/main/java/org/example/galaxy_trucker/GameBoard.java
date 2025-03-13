package org.example.galaxy_trucker;

import java.lang.*;
import java.util.ArrayList;


public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Player> players;
    private Player[] positions;
    private int nPositions;
    private int[] startPos;
    private TileSets tileSets;
    private Hourglass hourglass;
    private int GameLv;
    private int nPlayers;


    public GameBoard(TileSets list, int lv) {
        this.players = new ArrayList<>();
        GameLv = lv; //the GameBoard class learns the current game level from Game anc will extend it to players and other objects
        tileSets = list;
        startPos = new int[4];
        nPlayers = 0;

        if(lv == 2) {
            nPositions = 24;
            startPos[0] = 6;
            startPos[1] = 3;
            startPos[2] = 1;
            startPos[3] = 0;
        }
        else {
            nPositions = 18;
            startPos[0] = 4;
            startPos[1] = 2;
            startPos[2] = 1;
            startPos[3] = 0;
        }
        positions = new Player[nPositions];
        hourglass = new Hourglass(GameLv);
    }

    public void addPlayer(String id){
        this.players.add(new Player (id, this));
        nPlayers++;
    }

    public void SetStartingPositions(){
        //the position players[0] corresponds to the starting position of the 1st player
        // the leader (1st player) will be in the first position of the arraylist
        for(int i = 0; i < nPlayers; i++) {
            positions[startPos[i]] = players.get(i);

        }
    }

    public void movePlayer(String ID){

    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }
    public int getLevel(){return GameLv;}

}
