package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

public class Slavers extends Card{
    private int requirement;
    private int reward;
    private int Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    public Slavers(int level, int time, GameBoard board, int Reward, int Requirement, int Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
    }

    //json required
    public Slavers(){}
    public int getPunishment() {return Punishment;}
    public void setPunishment(int punishment) {Punishment = punishment;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}


}
