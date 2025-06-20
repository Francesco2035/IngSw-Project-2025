package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Debugship1Test {


    static DebugShip debugShip = new DebugShip();
    static Player player = new Player();


    @Test
    public void testDebugShip() throws NullPointerException, InvalidInput, IOException {
        debugShip.setNumber(1);

        Tile mainCock = new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);

//        player.getmyPlayerBoard().insertTile(mainCock,6,6,false);

        debugShip.execute(player);
        player.getmyPlayerBoard().insertTile(mainCock,6,6,false);

        assertTrue(player.getmyPlayerBoard().checkValidity());

    }


}
