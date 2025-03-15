package org.example.galaxy_trucker;

import java.util.ArrayList;

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






    @Override
    public void initType() {
        if (type.equals("double")) setMaxNumGoods(2);
        else if (type.equals("triple")) setMaxNumGoods(3);
    }
}
