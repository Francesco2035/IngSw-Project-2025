


package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Smugglers extends Card{
    private int requirement;
    private Planet reward;
    private int Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    Smugglers(int level, int time, GameBoard board, Planet Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
    }
    public void CardEffect(){
        int Order=0;
        int AttackNumber=0;
        boolean Flag=true;
        boolean Attacked=false;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerPlance CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        int PlayerPower;


        while(Len>Order && Flag){
            PlayerPower=PlayerList.get(Order).getPower();
            if(PlayerPower<requirement) {
                PlayerList.get(Order).loseCargo(Punishment);
            }// fine caso sconfitta
            else if (PlayerPower>requirement){
                Flag=false;
                //if(PLayerlist.get(Order).yes()){   //chiedo se vuole prende le ricompense
                //PlayerList.get(Order).HandleCargo(Reward);
                //PlayerList.get(Order).movePlayer(-this.Time);
                //}
            }//fine caso vittoria
            Order++;

        }
    }

}
