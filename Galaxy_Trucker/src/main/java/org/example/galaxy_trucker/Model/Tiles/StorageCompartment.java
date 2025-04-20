package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class StorageCompartment extends Storage{

    private ArrayList<Goods> goods;


    private void orderGoods() {
        this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }




    @Override
    public Goods removeGood(int i){
        if (i > goods.size()){
            throw new IndexOutOfBoundsException("Cannot remove a good because it is out of bounds");
        }
        tile.sendUpdates(goods,0, false, false, 0, "StorageCompartment");
        return goods.remove(i);



    }

    @Override
    public void addGood(Goods good) {
        if (goods.size() == type){
            throw new InvalidInput("StorageCompartment is full!");
        }
        if (good.getValue() == 4){
            throw new InvalidInput("StorageCompartment cannot contain special Goods");
        }
        goods.add(good);
        tile.sendUpdates(goods,0, false, false, 0, "StorageCompartment");

    }


    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        playerBoard.getStorages().add(this);
        goods = new ArrayList<>();
        tile.sendUpdates(goods,0, false, false, 0, "StorageCompartment");

    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
        tile.sendUpdates(new RemoveTileEvent());

    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        StorageCompartment clone = new StorageCompartment();
        clone.goods = new ArrayList<>(goods);
        clone.type = this.type;
        return clone;
    }

}
