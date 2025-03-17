package org.example.galaxy_trucker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        storage1.setAbility(Goods.BLUE, true);
        storage1.setAbility(Goods.GREEN, true);
        Assertions.assertEquals(Goods.GREEN, storage1.getGoods().get(0));

        PlayerBoard testboard= new PlayerBoard(2);
        //testboard.insertTile();
    }
}