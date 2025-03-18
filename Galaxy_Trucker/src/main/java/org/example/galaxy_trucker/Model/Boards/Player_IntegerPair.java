package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.Player;

public class Player_IntegerPair {
    Player player;
    Integer number;

    Player_IntegerPair(Player player, Integer number) {
        this.player = player;
        this.number = number;
    }

    public Player getKey(){return player;}
    public Integer getValue(){return number;}

}
