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
        int Order=PlayerList.size()-1;
        int StarpowderMovement=0;
        while(Order>=0){
            CurrentPlanche =PlayerList.get(Order).getmyPlayerBoard();
            System.out.println("numof exposed connectors of: "+PlayerList.get(Order).GetID()+" is: "+PlayerList.get(Order).getmyPlayerBoard().getExposedConnectors());
            StarpowderMovement=-CurrentPlanche.getExposedConnectors();
            if(StarpowderMovement!=0) {
                Board.movePlayer(PlayerList.get(Order).GetID(), StarpowderMovement);
            }
            else {
                System.out.println("No exposed connectors found for "+PlayerList.get(Order).GetID());
            }
            Order--;
        }

        return;
    }

    //json required
    public Stardust(){}

}


