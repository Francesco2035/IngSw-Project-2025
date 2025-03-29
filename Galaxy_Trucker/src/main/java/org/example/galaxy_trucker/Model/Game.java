package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private String GameID;
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
        CardDeck = new CardStacks(gag, GameLevel);
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv, CardDeck);
    }


    public void NewPlayer(String ID)throws IllegalArgumentException, IndexOutOfBoundsException{
        if(PlayerList.size() >= 4)
            throw new IndexOutOfBoundsException("Game is full");

        for(Player p : PlayerList){
            if(p.GetID().equals(ID)){
                throw new IllegalArgumentException("Player already exists");
            }
        }
        Player newborn = new Player(ID, GameBoard);
        GameBoard.addPlayer(newborn);
        PlayerList.add(newborn);
    }

    public void RemovePlayer(Player DeadMan){
        PlayerList.remove(DeadMan);
    }



}

