package org.example.galaxy_trucker;

import javafx.util.Pair;

import java.lang.*;
import java.util.ArrayList;
import java.util.Comparator;


public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Pair<Player, Integer>> players;
    private Player[] positions;
    private int nPositions;
    private int[] startPos;
    private TileSets tileSets;
    private Hourglass hourglass;
    private int GameLv;
    private int PlayersOnBoard;




    public GameBoard(TileSets list, int lv) {}
//        this.players = new ArrayList<>();
//        GameLv = lv; //the GameBoard class learns the current game level from Game anc will extend it to players and other objects
//        tileSets = list;
//        startPos = new int[4];
//        PlayersOnBoard = 0;
//
//
//        if(lv == 2) {
//            nPositions = 24;
//            startPos[0] = 6;
//            startPos[1] = 3;
//
//        }
//        else {
//            nPositions = 18;
//            startPos[0] = 4;
//            startPos[1] = 2;
//        }
//
//        startPos[2] = 1;
//        startPos[3] = 0;
//
//        positions = new Player[nPositions];
//        hourglass = new Hourglass(GameLv);
//    }
//
//
//    /**
//     * adds a new player to the game
//     * @param ID of the new player
//     */
//    public void addPlayer(String ID){
//        Player NewPlayer = new Player(ID, this);
//        int NewPlayerPosition = 0;
//        Pair<Player, Integer> NewPair = new Pair<>(NewPlayer, NewPlayerPosition);
//        this.players.add(NewPair);
//
//    }
//
//
//    /**
//     * sets the starting position of a player on the common board:
//     * the position players[0] corresponds to the starting position of the 1st player
//     * the leader (1st player) will be in the first position of the arraylist
//     * @param ID of the player
//     */
//    public void SetStartingPosition(String ID){
//
//        Pair<Player, Integer> cur = players.stream()
//                                           .filter(p -> ID
//                                           .equals( p.getKey().GetID()) )
//                                           .findFirst().orElseThrow();
//
//        SetNewPosition(cur, startPos[PlayersOnBoard]);
//
//        PlayersOnBoard++;
//    }
//
//
//    /**
//     * moves the player forward or backwards on the board of a selected number of steps
//     *
//     * @param ID of the player to move
//     * @param nSteps to run (negative if the player is moving backwards)
//     * @throws IllegalArgumentException if the number of steps is 0
//     */
//    public void movePlayer(String ID, int nSteps) throws IllegalArgumentException{
//
//        Pair<Player, Integer> cur = players.stream()
//                                           .filter(p -> ID.equals( p.getKey().GetID() ) )
//                                           .findFirst()
//                                           .orElseThrow();
//
//        int NewPos = cur.getValue();
//
//        if(nSteps == 0) throw new IllegalArgumentException("Number of steps cannot be 0: must move forward or backwards");
//
//        else if(nSteps > 0)
//            while(nSteps > 0){
//                NewPos++;
//                if(positions[NewPos] == null) nSteps--;
//                else if(cur.getKey().equals(players.getLast().getKey())){
//                    //ELIMINAZIONE GIOCATORE DOPPIATO DA GESTIRE
//                    //TEMPORANEAMENTE SI SOLLEVA UNA ECCEZIONE
//                    throw new RuntimeException("GIOCATORE DOPPIATO");
//                }
//            }
//        else while(nSteps < 0){
//            NewPos--;
//            if(positions[NewPos] == null) nSteps++;
//            else if(cur.getKey().equals(players.getFirst().getKey())){
//                //ELIMINAZIONE GIOCATORE DOPPIATO DA GESTIRE
//                //TEMPORANEAMENTE SI SOLLEVA UNA ECCEZIONE
//                throw new RuntimeException("GIOCATORE DOPPIATO");
//            }
//        }
//
//        SetNewPosition(cur, NewPos);
//    }
//
//
//    /**
//     * support method: brings a player on a specified position on the board and reorders the leadboard
//     * @param cur pair of: player to move and relative score (number of steps taken so far)
//     * @param newPosition to move the player on
//     */
//    private void SetNewPosition(Pair<Player, Integer> cur, int newPosition){
//
//        int CurIndex = players.indexOf(cur);
//        Pair<Player, Integer> NewPair = new Pair<>(cur.getKey(), newPosition);
//
//        positions[cur.getValue()] = null;
//        positions[newPosition % nPositions] = NewPair.getKey();
//
//        players.remove(CurIndex);
//        players.add(CurIndex, NewPair);
//
//
//        players.sort(Comparator.comparing(Pair::getValue));
//        //revers the order of the arraylist to have the leader in the 1st position (index = 0)
//        ArrayList<Pair<Player, Integer>> OrderedPlayers = new ArrayList<>();
//        for(int i= players.size()-1; i>=0; i--){
//            OrderedPlayers.add(players.get(i));
//        }
//        players = OrderedPlayers;
//    }
//
//
//    public ArrayList<Player> getPlayers(){
//        ArrayList<Player> PlayersCopy = new ArrayList<>();
//
//        for (Pair<Player, Integer> player : players) {
//            PlayersCopy.add(player.getKey());
//        }
//
//        return PlayersCopy;
//    }


    public int getLevel(){return GameLv;}
    public Player[] getPositions(){return this.positions;}
    public TileSets getTilesSets(){return tileSets;}

}