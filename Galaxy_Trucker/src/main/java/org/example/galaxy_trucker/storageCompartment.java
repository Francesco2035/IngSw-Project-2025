package org.example.galaxy_trucker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Enum.*;

public class storageCompartment extends Component{


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
        Collections.sort(goods);
    }


    @Override
    public ArrayList<Goods> getAbility(Goods good){
        if (good == null){
            return goods;
        }
        else{
            if (goods.contains(good)){
                return goods;
            }
            else {
                return null;
            }
        }

    }


    @Override
    public int unSetAbility(Goods good) {
        this.goods.remove(good);
        this.orderGoods();
        return 0;
    }


    @Override
    public int setAbility(@NotNull Goods good){
        if (good.ordinal()==3) throw new IllegalArgumentException("non-specialStorageCompartment");
        this.goods.add(good);
        this.orderGoods();
        return goods.indexOf(good);
    }


    @Override
    public void initType() {
        if (type.equals("double")) setMaxNumGoods(2);
        else if (type.equals("triple")) setMaxNumGoods(3);
    }
}
