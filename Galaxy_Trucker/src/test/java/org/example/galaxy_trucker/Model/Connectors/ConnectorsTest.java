package org.example.galaxy_trucker.Model.Connectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorsTest {


    @Test
    public void test() {


        CANNON cn = CANNON.INSTANCE;

        assertFalse(cn.checkLegal(null));
        assertFalse(cn.checkLegal(ENGINE.INSTANCE));

        assertFalse(cn.checkAdjacent(null));
        assertFalse(cn.checkAdjacent(ENGINE.INSTANCE));


        ENGINE en = ENGINE.INSTANCE;

        assertFalse(en.checkLegal(null));
        assertFalse(en.checkLegal(ENGINE.INSTANCE));

        assertFalse(en.checkAdjacent(null));
        assertFalse(en.checkAdjacent(ENGINE.INSTANCE));

    }

}