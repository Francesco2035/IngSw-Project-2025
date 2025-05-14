package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CheckValidityTest {

    @Test
    public void checkValidity() throws IOException {
        Player newPlayer = new Player();
        newPlayer.setId("poggi");
        newPlayer.setState(new CheckValidity());
        newPlayer.setMyPlance(TestSetupHelper.createInitializedBoard5());
        newPlayer.getPlayerState().createDefaultCommand("dragone", newPlayer).execute(newPlayer);
    }

}