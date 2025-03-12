package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Pirates extends Card{
    private int requirement;
    private int reward;
    private Meteorites Punishment;
            // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    Pirates(int level, int time, GameBoard board, int Reward, int Requirement, Meteorites Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
    }
    @Override
    public void CardEffect(){
        int Order=0;
        boolean Flag=true;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerPlance AbandonedShipCurrentPlanche;
        int Len= PlayerList.size();
        while(Len>Order && Flag){

            Order++;
        }
    }

}
