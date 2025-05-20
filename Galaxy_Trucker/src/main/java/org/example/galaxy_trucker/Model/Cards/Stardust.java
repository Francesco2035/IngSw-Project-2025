package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

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
        this.finishCard();


        return;
    }

    @Override
    public void finishCard() {
        System.out.println("card finished");
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
            PlayerList.get(i).SetReady(true);
        }
        this.setFinished(true);
    }

    //json required
    public Stardust(){}

}


