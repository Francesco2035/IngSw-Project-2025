package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class OpenSpace extends Card{
    public OpenSpace(int level, GameBoard board){

        super(level, 0 ,board);
    }
    @Override
    public void CardEffect(){
        int OpenSpaceOrder=0;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard Planche;
        ArrayList<IntegerPair> ActiveEngines;
        int OpenSpaceMovement=0;
        while(OpenSpaceOrder<PlayerList.size()){ // moves the players in order
            Planche=PlayerList.get(OpenSpaceOrder).getMyPlance();
            ActiveEngines=PlayerList.get(OpenSpaceOrder).getEnginePower();

            OpenSpaceMovement=Planche.getEnginePower(ActiveEngines);
            if(OpenSpaceMovement==0){
                //KILL YOURSELF
            }
            Board.movePlayer(PlayerList.get(OpenSpaceOrder).GetID(),OpenSpaceMovement);
            //o simili a seconda di come decidiamo di fare il movement
            OpenSpaceOrder++;

        }

        return;
    }

    //json required
    public OpenSpace() {}
}

