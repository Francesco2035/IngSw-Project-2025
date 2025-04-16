package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandleCargoTest {

    @Test
    void playerAction() {

        Player poggi = new Player();
        poggi.setMyPlance(TestSetupHelper.createInitializedBoard1());
        poggi.setState(new HandleCargo());
        PlayerBoard pb = poggi.getmyPlayerBoard();
        ArrayList<Goods> goods = new ArrayList<>();
        goods.add(new BLUE());
        goods.add(new GREEN());
        goods.add(new BLUE());
        goods.add(new BLUE());
        pb.setRewards(goods);

        String com1 = """
        {
          "title": "GetFromRewards",
          "position": 2
        }
        """;

        Command command = poggi.getPlayerState().PlayerAction(com1, poggi);
        command.execute();

        assertEquals(1, pb.getBufferGoods().size());

         com1 = """
        {
          "title": "PutInStorage",
          "x": 7,
          "y": 9,
          "position":0
        }
        """;

        command = poggi.getPlayerState().PlayerAction(com1, poggi);
        command.execute();

        assertEquals(0, pb.getBufferGoods().size());





    }
}