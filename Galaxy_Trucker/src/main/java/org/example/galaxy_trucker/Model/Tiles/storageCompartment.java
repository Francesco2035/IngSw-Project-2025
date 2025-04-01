package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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

    @Override
    public void initType() {
        if (type==2) setMaxNumGoods(2);
        else if (type==3) setMaxNumGoods(3);
    }


    @Override
    public void rotate(Boolean direction) {}

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


    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getStorages().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
    }

}
