package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class Pirates extends Card{
    private int requirement;
    private int reward;

    @JsonProperty("punishment")
    private ArrayList<Integer> Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    public Pirates(int level, int time, GameBoard board, int Reward, int Requirement, ArrayList<Integer> Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
      this.Punishment = Punsihment;
    }







    //json reuired
    public Pirates(){}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public ArrayList<Integer> getPunisment() {return Punishment;}
    public void setPunisment(ArrayList<Integer> punisment) {Punishment = punisment;}
}
