package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;

import java.util.ArrayList;

// list of the cargo contained in a planet and the bool signaling if a player chose it already
public class
Planet {
    private Player Occupied;
    @JsonProperty("Goods")
    ArrayList<org.example.galaxy_trucker.Model.Boards.Goods> Goods;

    Planet(ArrayList<Goods> Goods) {
        Goods = Goods;
        this.Occupied=null;
    }




    public ArrayList<Goods> getGoods() {
        return Goods;
    }

    public boolean isOccupied() {
        if(Occupied != null){
            return true;
        }
        else{
            return false;
        }
    }




    // json file


    public Planet() {}

    public void setGoods(ArrayList<Goods> goods) {
        Goods = goods;
    }

    public void setOccupied(Player occupied) {
        Occupied = occupied;
    }

    public Player getOccupied() {
        return Occupied;
    }


}
