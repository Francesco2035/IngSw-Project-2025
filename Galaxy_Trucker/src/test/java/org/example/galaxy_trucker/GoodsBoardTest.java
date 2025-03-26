package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Tiles.specialStorageCompartment;
import org.example.galaxy_trucker.Model.Tiles.storageCompartment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoodsBoardTest {

    @Test
    void pullGoods() {
    }

    @Test
    void putGoods() {
    }

    @Test
    void pullFromBuffer() {
    }

    @Test
    void removeGood() {
        storageCompartment storage1 = new storageCompartment();
        specialStorageCompartment storage2 = new specialStorageCompartment();
        storage1.setMaxNumGoods(3);
        storage1.setAbility(new BLUE(), true);
        storage1.setAbility(new GREEN(), true);
        Assertions.assertEquals(Goods.GREEN, storage1.getGoods().get(0));

        PlayerBoard testboard= new PlayerBoard(2);
        //testboard.insertTile();
    }
}