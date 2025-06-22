package org.example.galaxy_trucker.Model;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.AbandonedShip;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    void testPlayer() throws IOException {


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
        assertTrue(p1.getmyPlayerBoard().checkValidity());
        GameBoard Gboard = game.getGameBoard();
        FlightController c1 = new FlightController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        p1.setMyPlance(helper.createInitializedBoard1());
        p1.getmyPlayerBoard().insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6 ,6, false);


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        p1.setFinishListener(gc);
        assertEquals(gc, p1.getFinishListener());

        p1.setReadyListener(gc);
        assertEquals(gc, p1.getReadyListener());

        System.out.println(p1.getCurrentTile());
        p1.setCurrentTile(null);
        try{
            p1.setCurrentTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE));
        }catch(Exception e){
            assertEquals("Your hand is full!", e.getMessage());
        }

        p1.setCard(new AbandonedShip(3, 1, 2, 5, game.getGameBoard()));
        p1.execute();

        try {
            p1.DiscardTile();
        } catch (Exception e){
            assertEquals("You can't discard a Tile, you don't have one!", e.getMessage());
        }

        game.getGag().getTilesDeck().get(19).setChosen();
        try {
            p1.PickNewTile(-2);
        } catch (Exception e){
            assertEquals("Valore non valido", e.getMessage());
        }

        p1.PickNewTile(-1);
        p1.getCurrentTile().setChosen();
        try {
            p1.SelectFromBuffer(0);
        } catch (Exception e){
            assertEquals("You can't select a Tile, you have already one!", e.getMessage());
        }

//        p1.SelectFromBuffer(-20);

        try {
            p1.DiscardTile();
        } catch (Exception e){
            assertEquals("You can't discard this Tile!", e.getMessage());
        }







    }
}