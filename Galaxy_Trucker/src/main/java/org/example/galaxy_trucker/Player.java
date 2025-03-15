package org.example.galaxy_trucker;
import java.util.Random;

public class Player {

    private GameBoard CommonBoard;
    private PlayerPlance myPlance;
    private String ID;
    private boolean ready;
    private int credits;
    private Tile CurrentTile;   //the tile that Player has in his hand


    public Player(String id, GameBoard board) {
        CommonBoard = board;
        myPlance = new PlayerPlance(board.getLevel());
        ID = id;
        credits = 0;
        ready = false;
        CurrentTile = null;
    }

    public void consumeEnergyFrom(int x, int y){
        myPlance.getEnergyTiles().stream()
                                 .filter(tile -> tile.getCoords().getFirst() == x && tile.getCoords().getSecond() == y)
                                 .findFirst()
                                 .ifPresent(tile -> tile.getComponent().setAbility());//riduce di 1 le batterie a x, y se non sono già a zero
        }

    public void fireCannon(){}
    public void startEngine(){}



    public int RollDice() {
        Random r = new Random();
        int d1 = r.nextInt(6) + 1;
        int d2 = r.nextInt(6) + 1;
        return d1+d2;
    }


    public void PickNewTile(){      //get a new random tile
        CurrentTile = CommonBoard.getTilesSets().getNewTile();
    }

    public void PickNewTile(int index){     //select a new tile from the uncovered set
        CurrentTile = CommonBoard.getTilesSets().getNewTile(index);
    }

    public void DiscardTile(){
        CommonBoard.getTilesSets().AddUncoveredTile(CurrentTile);
        CurrentTile = null;
    }

    public void PlaceInBuffer(){

    }

    public void PlaceTile(int x, int y){
        this.myPlance.insertTile(CurrentTile, x, y);
        CurrentTile = null;
    }

    public void RightRotate() {CurrentTile.RotateDx();}
    public void LeftRotate() {CurrentTile.RotateSx();}


    public void IcreaseCredits(int num){
        credits += num;
    }

    public void SetReady(){
        this.ready = true;
    }



    public String GetID() {return this.ID;}
    public int GetCredits() {return this.credits;}
    public boolean GetReady() {return this.ready;}
    public PlayerPlance getMyPlance() {return myPlance;}

}
