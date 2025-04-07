package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private String GameID;
    private HashMap<String,Player> Players;
    private ArrayList<Player> PlayerList;
    private GameBoard GameBoard;
    private CardStacks CardDeck;
    private TileSets TileDecks;
    private int  lv;
    private State CurrentState;
    private GAGen gag;


    public Game(int GameLevel, String id) throws IOException{
        GameID = id;
        gag = new GAGen();
        PlayerList = new ArrayList<>();
        Players = new HashMap<>();
        CardDeck = new CardStacks(gag, GameLevel);
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv, CardDeck);
    }


    public void NewPlayer(Player newborn)throws IllegalArgumentException, IndexOutOfBoundsException{
        if(Players.size() >= 4)
            throw new IndexOutOfBoundsException("Game is full");

        if (Players.containsKey(newborn.GetID())){
            throw new IllegalArgumentException("Player already exists");
        }
        newborn.setBoards(GameBoard);
        this.GameBoard.addPlayer(newborn);
        Players.put(newborn.GetID(), newborn);
        PlayerList.add(newborn);

    }

    public void RemovePlayer(String DeadMan){
        Players.remove(DeadMan);
    }

    public String getID(){return GameID;}

    public State getCurrentState(){
        return CurrentState;
    }

    public void setState(State newState){
        CurrentState = newState;
    }

    public HashMap<String,Player> getPlayers(){
        return Players;
    }


    public GAGen getGag() {
        return gag;
    }

    public GameBoard getGameBoard() {
        return GameBoard;
    }
}

