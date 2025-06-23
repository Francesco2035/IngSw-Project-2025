package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.Player;

public class Player_IntegerPair {
    Player player;
    Integer score;

    Player_IntegerPair(Player player, Integer number) {
        this.player = player;
        this.score = number;
    }

    public Player getKey(){return player;}
    public Integer getValue(){return score;}
    public void setValue(Integer value){this.score=value;}


}
