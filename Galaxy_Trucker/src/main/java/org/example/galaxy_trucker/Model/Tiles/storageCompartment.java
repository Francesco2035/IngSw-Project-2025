package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class StorageCompartment extends Storage{


    public ArrayList<Goods> getGoods(Goods good) {
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

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

}



//
//    @Override
//    public int getAbility(){
//        return maxNumGoods;
//    }
//
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
//
//    }
//
//
//    @Override
//    public int setAbility(Goods good, boolean select) {
//        if(select){
//            if (good.getValue() == 3) throw new IllegalArgumentException("non-specialStorageCompartment");
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


//
//    @Override
//    public ArrayList<Integer> getAbility(int integer) {
//        return null;
//    }
//
//    @Override
//    public int setAbility() {
//        return 0;
//    }
//
//    @Override
//    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
//        return 0;
//    }
//
//    @Override
//    public void setAbility(boolean direzione) {}
//
//    @Override
//    public boolean controlValidity(PlayerBoard pb, int x, int y) {
//        return true;
//    }
//
//    @Override
//    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {}

