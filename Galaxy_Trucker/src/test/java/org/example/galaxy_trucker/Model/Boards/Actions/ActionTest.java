package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.Killing;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionTest {
    
    
    @Test
    public void testActions() throws IOException {


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
        assertTrue(p1.getmyPlayerBoard().checkValidity());


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        AddCrewAction aca = new AddCrewAction(2, false, false, p1.getmyPlayerBoard());
        aca.visit(((HousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[6][6].getComponent()), new AddCrewState());
        try {
            aca.visit(((HousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[6][6].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }

        AddCrewAction aca2 = new AddCrewAction(0, false, true, p1.getmyPlayerBoard());
//        ((HousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setBrownAlien(true);
        p1.getmyPlayerBoard().setBrownAlien(true);
        try {
            aca2.visit(((HousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new AddCrewState());
        } catch (Exception e){
            assertEquals("An alien of the same type is already present in the board", e.getMessage());
        }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        AddGoodAction aga = new AddGoodAction(new BLUE(), p1.getmyPlayerBoard(), 0, 0);
        try {
            aga.visit(((Storage) p1.getmyPlayerBoard().getPlayerBoard()[7][8].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        ComponentAction ca = new ComponentAction() {
            @Override
            public void visit(HotWaterHeater hotWaterHeater, PlayerState state) {
                super.visit(hotWaterHeater, state);
            }

            @Override
            public void visit(Storage storage, PlayerState state) {
                super.visit(storage, state);
            }

            @Override
            public void visit(HousingUnit housing, PlayerState state) {
                super.visit(housing, state);
            }

            @Override
            public void visit(PlasmaDrill plasmaDrill, PlayerState state) {
                super.visit(plasmaDrill, state);
            }

            @Override
            public void visit(PowerCenter powerCenter, PlayerState state) {
                super.visit(powerCenter, state);
            }
        };

        try {
            ca.visit(((HotWaterHeater) null), new Killing());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }
        try {
            ca.visit(((Storage) null), new Killing());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }
        try {
            ca.visit(((HousingUnit) null), new Killing());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }
        try {
            ca.visit(((PowerCenter) null), new Killing());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }
        try {
            ca.visit(((PlasmaDrill) null), new Killing());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        GetEnginePower gep = new GetEnginePower(1);
        try {
            gep.visit(((HotWaterHeater) p1.getmyPlayerBoard().getPlayerBoard()[8][3].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        GetGoodAction gga = new GetGoodAction(9, p1.getmyPlayerBoard(), 0, 0);
        try {
            gga.visit(((Storage) p1.getmyPlayerBoard().getPlayerBoard()[7][8].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        GetPlasmaDrillPower gpdp = new GetPlasmaDrillPower(1);
        try {
            gpdp.visit(((PlasmaDrill) p1.getmyPlayerBoard().getPlayerBoard()[8][9].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        KillCrewAction kca = new KillCrewAction(p1.getmyPlayerBoard());
        ((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setNumHumans(2);
        try {
            kca.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new BaseState());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }
        kca.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new Killing());


        KillCrewAction kca2 = new KillCrewAction(p1.getmyPlayerBoard());
        ((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setPurpleAlien(true);
        try {
            kca2.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }


        KillCrewAction kca3 = new KillCrewAction(p1.getmyPlayerBoard());
        ((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setPurpleAlien(true);
        try {
            kca3.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }


        KillCrewAction kca4 = new KillCrewAction(p1.getmyPlayerBoard());
        ((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setBrownAlien(true);
        try {
            kca4.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }


        KillCrewAction kca5 = new KillCrewAction(p1.getmyPlayerBoard());
        ((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()).setBrownAlien(true);
        try {
            kca5.visit(((ModularHousingUnit) p1.getmyPlayerBoard().getPlayerBoard()[5][7].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        UseEnergyAction uae = new UseEnergyAction(p1.getmyPlayerBoard());
        try {
            uae.visit(((PowerCenter) p1.getmyPlayerBoard().getPlayerBoard()[6][9].getComponent()), new Killing());
        } catch (Exception e){
            assertEquals("You are not allowed to perform this action in this state", e.getMessage());
        }























    }
}