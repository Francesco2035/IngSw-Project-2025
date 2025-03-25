package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private ArrayList<Player> PlayerList;
    private org.example.galaxy_trucker.Model.Boards.GameBoard GameBoard;
    private CardStacks CardDeck;
    private TileSets TileDecks;
    private int  lv;
    private State CurrentState;
    private GAGen gag;




    public Game(int GameLevel) throws IOException {
        gag = new GAGen();
        PlayerList = new ArrayList<>();
        CardDeck = new CardStacks(gag, GameLevel);
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv, CardDeck);
    }


    private void NewPlayer(String ID){
        Player newborn = new Player(ID, GameBoard);
        GameBoard.addPlayer(newborn);
        PlayerList.add(newborn);
    }

    private void RemovePlayer(Player DeadMan){
        PlayerList.remove(DeadMan);
    }



}

