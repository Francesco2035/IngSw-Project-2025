package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    static Game game;
    static GameController gc;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        gc = TestSetupHelper.GameControllerSetup();
    }

    @Test
    public void testgame(){
        gc.addCommand(new DebugShip("poggi", "player1", 2, "DebugShipCommand", null));
        System.out.println(gc);
    }

}