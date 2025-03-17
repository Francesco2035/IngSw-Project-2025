package org.example.galaxy_trucker;

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
