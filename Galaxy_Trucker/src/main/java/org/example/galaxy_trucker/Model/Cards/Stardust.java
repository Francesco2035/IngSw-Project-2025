package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class Stardust extends Card {
    public Stardust(int level, GameBoard board){
        super(level, 0 ,board);
    }
    @Override
    public void CardEffect () {

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Order=PlayerList.size();
        int StarpowderMovement=0;
        while(Order>=0){
            CurrentPlanche =PlayerList.get(Order).getMyPlance();
            StarpowderMovement=-CurrentPlanche.getExposedConnectors();
            Board.movePlayer(PlayerList.get(Order).GetID(), -this.getTime());
            Order--;
        }

        return;
    }

    //json required
    public Stardust(){}

}


