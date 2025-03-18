package org.example.galaxy_trucker;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private ArrayList<Player> PlayerList;
    private GameBoard GameBoard;
    private CardStacks CardDeck;
    private TileSets TileDecks;
    private int  lv;
    private State CurrentState;
    private GAGen gag;



    public Game(int GameLevel) throws IOException {
        gag = new GAGen();
        PlayerList = new ArrayList<>();
        CardDeck = new CardStacks();
        TileDecks = new TileSets(gag);
        lv = GameLevel;
        GameBoard = new GameBoard(TileDecks, lv);
    }


    private void NewPlayer(String ID){
        Player newborn = new Player(ID, GameBoard);
        GameBoard.addPlayer(newborn);
        PlayerList.add(newborn);
    }

    private void RemovePlayer(Player DeadMan){
        PlayerList.remove(DeadMan);
    }

    public void CreateCardDeck(){
        // ???
    }

    public void CreateTileDeck(){
        // ???
    }

    public void PickNewCard(){
        //CardDeck.PickCard(???)
    }




}

