package org.example.galaxy_trucker;

import java.util.ArrayList;

// list of the cargo contained in a planet and the bool signaling if a player chose it already
public class Planet {
    private boolean Occupied;

    ArrayList<Goods> Goods;

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
}
