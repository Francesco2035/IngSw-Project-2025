package org.example.galaxy_trucker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class specialStorageCompartment extends Component{



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
    public int setAbility(Goods good, boolean select) {
        if(select){
            this.goods.add(good);
            this.orderGoods();
            return goods.indexOf(good);
        }
        else{
            this.goods.remove(good);
            this.orderGoods();
            return 0;
        }
    }

    @Override
    public void initType() {
        if (type.equals("single")) setMaxNumGoods(1);
        else if (type.equals("double")) setMaxNumGoods(2);
    }
}
