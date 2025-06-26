package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

public class SpecialStorageCompartment extends Storage{

    private ArrayList<Goods> goods;
    PlayerBoard playerBoard;



    @Override
    public ArrayList<Goods> getGoods() {
        return goods;
    }
    @Override
    public int getValue(int i){
        return goods.get(i).getValue();
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }
//    private void orderGoods() {
//          this.goods.sort(Comparator.comparingInt(Goods::getValue));
//    }


    @Override
    public Goods removeGood(int position){
        if (position >= goods.size() || position < 0){
            return null;
            //throw new InvalidInput("Cannot remove a good because it is out of bounds");
        }
        Goods good = goods.remove(position);
        playerBoard.setTotalValue(-good.getValue());
        playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x,tile.y));
        if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
            playerBoard.getStoredGoods().remove(good.getValue());
        }
        tile.sendUpdates(goods,0, false, false, 0);
        return good;

    }


    @Override
    public void addGood(Goods good) {
        if(good == null){
            return;
        }
        if (goods.size() == type){
            throw new InvalidInput("SpecialStorageCompartment is full!");
        }
        goods.add(good);
        playerBoard.setTotalValue(good.getValue());
        if (playerBoard.getStoredGoods().containsKey(good.getValue())){
            System.out.println("Cargo add "+good.getValue()+ " in "+ tile.x+ " "+tile.y);
            playerBoard.getStoredGoods().get(good.getValue()).add(new IntegerPair(tile.x, tile.y));
        }
        else{
            ArrayList<IntegerPair> toAdd = new ArrayList<>();
            toAdd.add(new IntegerPair(tile.x, tile.y));
            System.out.println("Cargo add "+good.getValue()+ " in "+ tile.x+ " "+tile.y);
            playerBoard.getStoredGoods().put(good.getValue(), toAdd);

        }
        tile.sendUpdates(goods,0, false, false, 0);

    }


    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        this.playerBoard = playerBoard;
        playerBoard.getStorages().add(this);
        if (goods == null) {
            goods = new ArrayList<>();
        }
        else{
            ArrayList<IntegerPair> toAdd = new ArrayList<>();
            for (Goods good : goods){
                playerBoard.setTotalValue(good.getValue());
                if (playerBoard.getStoredGoods().containsKey(good.getValue())){
                    playerBoard.getStoredGoods().get(good.getValue()).add(new IntegerPair(tile.x, tile.y));
                }
                else{
                    toAdd.add(new IntegerPair(tile.x, tile.y));
                    playerBoard.getStoredGoods().put(good.getValue(), toAdd);
                    toAdd = new ArrayList<>();
                }
            }
        }
        tile.sendUpdates(goods,0, false, false, 0);

    }


    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
        for (Goods good : goods){
            playerBoard.setTotalValue(-good.getValue());
            playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x, tile.y));
            if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
                playerBoard.getStoredGoods().remove(good.getValue());
            }
        }
        tile.sendUpdates();

    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        SpecialStorageCompartment clone = new SpecialStorageCompartment();
        clone.goods = new ArrayList<>(goods);
        clone.type = this.type;
        for (Goods g : clone.getGoods()) {
            System.out.println("CLONED SPC "+g);
        }
        return clone;
    }

    @Override
    public void sendState(){
        tile.sendUpdates(goods,0, false, false, 0);
    }

}
