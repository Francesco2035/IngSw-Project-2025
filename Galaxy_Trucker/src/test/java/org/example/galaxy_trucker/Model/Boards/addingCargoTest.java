package org.example.galaxy_trucker.Model.Boards;

import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.RED;
import org.example.galaxy_trucker.Model.Goods.YELLOW;
import org.example.galaxy_trucker.Model.PlayerStates.HandleCargo;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class addingCargoTest {


    static PlayerBoard playerBoard = new PlayerBoard(2);
    static PlayerBoard playerBoard2 = new PlayerBoard(2);
    static GAGen gag;

    static {
        try {
            gag = new GAGen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @BeforeAll
//    public static void setup(){


//    }


    @Test
    public void cargoTest(){

        playerBoard = TestSetupHelper.createInitializedBoard1();

        // normal in 7,9
        //special in 7,8
        ArrayList<Goods> reward= new ArrayList();
        reward.add(new RED());
        reward.add(new RED());
        reward.add(new YELLOW());
        reward.add( new YELLOW());
        reward.add(new BLUE());
        playerBoard.getRewards().clear();
        playerBoard.getRewards().addAll(reward);

        PlayerState state= new HandleCargo();

        Tile specialStorage= playerBoard.getTile(7,8);

        System.out.println(specialStorage.getComponent().getClass() + " space is " + specialStorage.getComponent().getType());
        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(1),playerBoard,7,8),state);
        Tile normalStorage= playerBoard.getTile(7,9);
        playerBoard.performAction(normalStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(1),playerBoard,7,9),state);


        /// remove
        GetGoodAction scarta = new GetGoodAction(0, playerBoard,7,8);
        playerBoard.performAction(specialStorage.getComponent(),scarta,state);

        /// aggiungo un blu
        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(2),playerBoard,7,8),state);

        /// switch con il vuoto
        GetGoodAction prendi= new GetGoodAction(0,playerBoard,7,8);

        playerBoard.performAction(specialStorage.getComponent(),prendi,state);

        Goods good1 = prendi.getGood();

        prendi = new GetGoodAction(1,playerBoard,7,9);

        try {
            playerBoard.performAction(normalStorage.getComponent(), prendi, state);
        } catch (Exception e){
            e.printStackTrace();
        }


        Goods good2 = prendi.getGood();

        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(good2,playerBoard,7,8),state);

        playerBoard.performAction(normalStorage.getComponent(),new AddGoodAction(good1,playerBoard,7,9),state);

        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(playerBoard.getFromRewards(1),playerBoard,7,8),state);

        /// switch blu da normal a special yellow da special a normal
        prendi = new GetGoodAction(0,playerBoard,7,8);

        playerBoard.performAction(specialStorage.getComponent(),prendi,state);

        good1 = prendi.getGood();

        prendi = new GetGoodAction(1,playerBoard,7,9);

        playerBoard.performAction(normalStorage.getComponent(),prendi,state);
        good2 =prendi.getGood();

        playerBoard.performAction(specialStorage.getComponent(),new AddGoodAction(good2,playerBoard,7,8),state);

        playerBoard.performAction(normalStorage.getComponent(),new AddGoodAction(good1,playerBoard,7,9),state);



    }
}
