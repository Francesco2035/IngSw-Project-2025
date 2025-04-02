package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class SpecialStorageCompartment extends Storage{

//maxnumgoods==type


    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    private void orderGoods() {
          this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }


}










//    @Override
//    public int getAbility(){
//        return maxNumGoods;
//    }

//
//    @Override
//    public ArrayList<Goods> getAbility(Goods good){
//        if (good == null){
//            return goods;
//        }
//        else{
//            if (goods.contains(good)){
//                return goods;
//            }
//            else {
//                return null;
//            }
//        }
//    }
//
//
//
//    @Override
//    public int setAbility(Goods good, boolean select) {
//        if(select){
//            this.goods.add(good);
//            this.orderGoods();
//            return goods.indexOf(good);
//        }
//        else{
//            this.goods.remove(good);
//            this.orderGoods();
//            return 0;
//        }
//    }