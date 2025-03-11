package org.example.galaxy_trucker;

public class Player {

    private PlayerPlance myPlance;
    private String ID;
    private int credits;


    public Player(String id, int lv) {
        myPlance = new PlayerPlance(lv);
        ID = id;
        credits = 0;
    }

    public void consumeEnergyFrom(int x, int y){
        myPlance.getEnergyTiles().stream()
                .filter(tile -> tile.getCoords().getKey() == x && tile.getCoords().getValue() == y)
                .findFirst()
                .filter(tile -> tile.getComponent().getAbility()!=-1)
                .ifPresent(tile -> tile.getComponent().setAbility()) //riduce di 1 le batterie a x, y se non sono già a zero
        ;}

    public void fireCannon(){}
    public void startEngine(){}

    public PlayerPlance getMyPlance() {
        return myPlance;
    }

    public void IcreaseCredits(int num){
        credits += num;
    }

}
