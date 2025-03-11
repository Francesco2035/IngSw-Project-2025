package org.example.galaxy_trucker;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerPlance myPlance;
    private String ID;
    private boolean ready;
    private int credits;


    public Player(String id, int lv, GameBoard board) {
        myPlance = new PlayerPlance(lv);
        CommonBoard = board;
        ID = id;
        credits = 0;
        ready = false;
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

    public void RightRotate(Tile t) {}
    public void LeftRotate(Tile t) {}

    public PlayerPlance getMyPlance() {
        return myPlance;
    }

    public int RollDice() {
        Random r = new Random();
        return r.nextInt(11) + 1;
    }

    public void IcreaseCredits(int num){
        credits += num;
    }

    public void SetReady() {
        this.ready = true;
    }


    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}

}
