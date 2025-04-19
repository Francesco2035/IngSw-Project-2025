package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GiveAttackTest {

    @Test
    void playerAction() throws IOException {
        PlayerState state = new GiveAttack();
        String json = """
        {
          "title": "give_attack",
          "coordinates": [
            { "x": 1, "y": 2 },
            { "x": 3, "y": 4 },
            { "x": 5, "y": 6 }
          ]
        }
        """;
        Game game  = new Game(2,"gay");
        Player Poggi = new Player();
        Poggi.setId("poggi");
        game.NewPlayer(Poggi);

//        state.PlayerAction(json, game.getPlayers().get("poggi"));


    }
}