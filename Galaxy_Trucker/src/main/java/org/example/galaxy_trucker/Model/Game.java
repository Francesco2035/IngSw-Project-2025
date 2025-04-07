package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private String GameID;
    private ArrayList<Player> Players;
    private GameBoard GameBoard;
    private CardStacks CardDeck;
    private TileSets TileDecks;
    private int  lv;
    private State CurrentState;
    private GAGen gag;




    public Game(int GameLevel, String id) throws IOException{
        GameID = id;
        gag = new GAGen();
        Players = new ArrayList<>();
        CardDeck = new CardStacks(gag, GameLevel);
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv, CardDeck);
    }


    public void NewPlayer(Player newborn)throws IllegalArgumentException, IndexOutOfBoundsException{
        if(Players.size() >= 4)
            throw new IndexOutOfBoundsException("Game is full");

        for(Player p : Players){
            if(p.equals(newborn)){
                throw new IllegalArgumentException("Username already exists");
            }
        }

        GameBoard.addPlayer(newborn);
        Players.add(newborn);
    }

    public void RemovePlayer(Player DeadMan){
        Players.remove(DeadMan);
    }

    public String getID(){return GameID;}

    public GameBoard getGameBoard(){return GameBoard;}
}

