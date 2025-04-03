package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class SpecialStorageCompartment extends Storage{




    private ArrayList<Goods> goods;



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





    @Override
    public Goods removeGood(int position){
        if (position > goods.size()){
            throw new IndexOutOfBoundsException("Cannot remove a good because it is out of bounds");
        }
        return goods.remove(position);

    }

    @Override
    public void addGood(Goods good) {
        if (goods.size() == type){
            throw new InvalidInput("StorageCompartment is full!");
        }
        goods.add(good);
    }

}
