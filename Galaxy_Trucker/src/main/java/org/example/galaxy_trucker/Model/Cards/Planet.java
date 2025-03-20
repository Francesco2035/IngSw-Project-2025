package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Goods;

import java.util.ArrayList;

// list of the cargo contained in a planet and the bool signaling if a player chose it already
public class
Planet {
    private boolean Occupied;
    @JsonProperty("Goods")
    ArrayList<org.example.galaxy_trucker.Model.Boards.Goods> Goods;

    Planet(ArrayList<Goods> Goods) {
        Goods = Goods;
        this.Occupied=false;
    }




    public ArrayList<Goods> getGoods() {
        return Goods;
    }

    public boolean isOccupied() {
        return Occupied;
    }




    // json file


    public Planet() {}

    public void setGoods(ArrayList<Goods> goods) {
        Goods = goods;
    }

    public void setOccupied(boolean occupied) {
        Occupied = occupied;
    }


}
