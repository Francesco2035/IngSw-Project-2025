package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class StorageCompartment extends Storage{


    private int maxNumGoods;
    private ArrayList<Goods> goods;



    public int getMaxNumGoods() {
        return maxNumGoods;
    }

    public void setMaxNumGoods(int maxNumGoods) {
        this.maxNumGoods = maxNumGoods;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    private void orderGoods() {
        this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }


    @Override
    public void rotate(Boolean direction) {}



}
