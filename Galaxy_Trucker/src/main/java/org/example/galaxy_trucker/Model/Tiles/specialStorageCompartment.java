package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;
import java.util.Comparator;

public class specialStorageCompartment extends Component {



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



    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {}

    @Override
    public ArrayList<Integer> getAbility(int integer) {
        return null;
    }

    @Override
    public int setAbility() {
        return 0;
    }

    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        return 0;
    }

    @Override
    public void setAbility(boolean direzione) {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }


}
