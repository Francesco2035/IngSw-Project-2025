package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Goods.*;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.HandleCargoClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

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
        ArrayList<Goods> goods1 = new ArrayList<>();
        goods1.add(new RED());
        goods1.add(new BLUE());
        ArrayList<Connectors> con = new ArrayList<>();
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        for (String s: tui.formatCell(new TileEvent(58,0,0,goods1,0,false,false,0,0,con))){
            System.out.println(s);
        }

    }

    @Test
    public void otherspb(){
        ArrayList<Connectors> con = new ArrayList<>();
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        con.add(UNIVERSAL.INSTANCE);
        PlayerTileEvent playerTileEvent = new PlayerTileEvent("aaaa",45,6,7,null,0,false,false,0,0,con);
        tui.updateOthersPB(playerTileEvent);

        out.setOthersPB("aaaa",6,7,tui.formatCell(playerTileEvent));
        out.seeBoards();
    }

    @Test
    public void gb1(){
        tui.setGameboard(1);
        //out.initGameBoard(1);
        System.out.println(tui.getOut().printGameboard());
    }


}