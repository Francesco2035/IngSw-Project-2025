package org.example.galaxy_trucker;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerPlance myPlance;
    private String ID;
    private boolean ready;
    private int credits;


    public Player(String id, GameBoard board) {

        CommonBoard = board;
        myPlance = new PlayerPlance(board.getLevel());
        ID = id;
        credits = 0;
        ready = false;
    }

    public void consumeEnergyFrom(int x, int y){
        myPlance.getEnergyTiles().stream()
                .filter(tile -> tile.getCoords().getKey() == x && tile.getCoords().getValue() == y)
                .findFirst()
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
        int d1 = r.nextInt(6) + 1;
        int d2 = r.nextInt(6) + 1;
        return d1+d2;
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
