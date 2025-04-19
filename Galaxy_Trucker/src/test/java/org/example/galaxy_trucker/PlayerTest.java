package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void PickCardTest() throws IOException {
        Player p1 = new Player();
        p1.setId("pgp");
        Player p2 = new Player();
        p2.setId("fgr");

        Game g = new Game(2,"testGame");

        g.NewPlayer(p1);
        g.NewPlayer(p2);


        p1.SetReady(true);
        p2.SetReady(true);

        p1.PickNewTile(-1);
        p2.PickNewTile(-1);

        p1.DiscardTile();
        p2.DiscardTile();
        assertNull(p1.getCurrentTile());
        assertNull(p1.getCurrentTile());


        p1.PickNewTile(0);
        p2.PickNewTile(4);
        p2.PickNewTile(0);
        assertNull(p2.getCurrentTile());
        p1.DiscardTile();

        p1.PickNewTile(-1);
        Tile t = p1.getCurrentTile();
        p1.DiscardTile();
        p2.PickNewTile(2);
        assertEquals(t, p2.getCurrentTile());

    }


}