package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Goods;

import java.util.ArrayList;
import java.util.Collections;

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
    public int getAbility(){
        return maxNumGoods;
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
            if (good.ordinal() == 3) throw new IllegalArgumentException("non-specialStorageCompartment");
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
        if (type.equals("double")) setMaxNumGoods(2);
        else if (type.equals("triple")) setMaxNumGoods(3);
    }
}
