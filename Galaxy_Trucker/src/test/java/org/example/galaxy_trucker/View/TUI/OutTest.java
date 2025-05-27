package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.Commands.InputReader;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Model.Goods.*;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.HandleCargoClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class OutTest {

    static Out out;
    static InputReader input;
    static PlayerClient client;
    static TUI tui;

    @BeforeAll
    public static void setUp() throws IOException {
        input = new InputReader(new LinkedBlockingQueue<>());
        client = new PlayerClient();
        tui = new TUI();
        out = new Out(input, client);
    }

    @Test
    public void rewards(){
        client.setPlayerState(new HandleCargoClient());
        ArrayList<Goods> goods = new ArrayList<>();
        goods.add(new BLUE());
        goods.add(new BLUE());
        goods.add(new BLUE());
        goods.add(new RED());
        goods.add(new RED());
        goods.add(new GREEN());
        goods.add(new GREEN());
        goods.add(new YELLOW());
        RewardsEvent event = new RewardsEvent(goods);
        out.setRewards(tui.formatRewards(event));
        System.out.println(tui.formatRewards(event));
    }

}