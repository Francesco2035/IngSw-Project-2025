package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.InputHandlers.GiveSpeed;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

import java.util.ArrayList;

public class OpenSpace extends Card{
    private Player currentPlayer;
    private int order;

    public OpenSpace(int level, GameBoard board){

        super(level, 0 ,board);
        this.order = 0;
        this.currentPlayer = null;
    }

    @Override
    public void CardEffect(){

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(PlayerStates.Waiting);
        }
        this.updateSates();
    }
    @Override
    public void updateSates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size()){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();

                this.currentPlayer.setState(PlayerStates.GiveSpeed);
                this.currentPlayer.setInputHandler(new GiveSpeed(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(PlayerStates.BaseState);
        }
    }

    @Override
    public void continueCard(ArrayList<IntegerPair> engines) {
        int movement= currentPlayer.getMyPlance().getEnginePower(engines);
        this.getBoard().movePlayer(currentPlayer.GetID(),movement);
        this.currentPlayer.setState(PlayerStates.Waiting);
        this.updateSates();
    }



    //json required
    public OpenSpace() {}
}

