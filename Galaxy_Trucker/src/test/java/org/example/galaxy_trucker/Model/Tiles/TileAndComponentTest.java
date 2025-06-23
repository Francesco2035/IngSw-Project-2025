package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.KillCommand;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.RED;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TileAndComponentTest {



    @Test
    public void ComponentTest() throws IOException, InterruptedException {



        Game game = new Game(2, "g1");
        GameController gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);
        NewTestSetupHelper helper = new NewTestSetupHelper();

        Player p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        VirtualView vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
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
        assertTrue(p1.getmyPlayerBoard().checkValidity());



        //component

        Component c = new SpaceVoid();
        try{
            c.accept(new GetPlasmaDrillPower(1), new GiveSpeed());
        } catch (Exception e){
            assertEquals("Invalid input for the specific action", e.getMessage());
        }



        //hotwaterheater

        //type 2
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(95), 6, 7, false);
        HotWaterHeater hotWaterHeater = (HotWaterHeater) game.getGag().getTilesDeck().get(95).getComponent();
        hotWaterHeater.getEnginePower();
        hotWaterHeater.rotate(true);
        hotWaterHeater.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        hotWaterHeater.clone(p1.getmyPlayerBoard());
        hotWaterHeater.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        hotWaterHeater.accept(new GetEnginePower(1), new GiveSpeed());
        assertEquals(2, hotWaterHeater.getType());



        //type 1
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(85), 6, 7, false);
        HotWaterHeater hotWaterHeater2 = (HotWaterHeater) game.getGag().getTilesDeck().get(85).getComponent();
        hotWaterHeater2.getEnginePower();
        hotWaterHeater2.rotate(true);
        hotWaterHeater2.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        hotWaterHeater2.clone(p1.getmyPlayerBoard());
        hotWaterHeater2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        hotWaterHeater2.accept(new GetEnginePower(1), new GiveSpeed());
        assertEquals(1, hotWaterHeater2.getType());





        //alienaddon1

        //type 0
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(132), 6, 7, false);
        AlienAddons alienaddon = (AlienAddons) game.getGag().getTilesDeck().get(132).getComponent();
        alienaddon.rotate(true);
        alienaddon.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        alienaddon.clone(p1.getmyPlayerBoard());
        alienaddon.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(0, alienaddon.getType());



        //type 1
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(139), 6, 7, false);
        AlienAddons alienaddon1 = (AlienAddons) game.getGag().getTilesDeck().get(139).getComponent();
        alienaddon1.rotate(true);
        alienaddon1.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        alienaddon1.clone(p1.getmyPlayerBoard());
        alienaddon1.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(1, alienaddon1.getType());




        //housingunits

        //type 0
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(35), 6, 7, false);
        ModularHousingUnit mhu = (ModularHousingUnit) game.getGag().getTilesDeck().get(35).getComponent();
        mhu.notifyUnit(true, new ModularHousingUnit());
        mhu.remove(p1.getmyPlayerBoard());
        mhu.getX();
        mhu.getY();
        mhu.setX(mhu.getX() + 1);
        mhu.setY(mhu.getY() + 1);
        mhu.isPopulated();
        mhu.checkNearbyUnits(p1.getmyPlayerBoard());
        helper.HumansSetter1(p1.getmyPlayerBoard()); //TODO: aggiungere i casi degli alieni, tramite un nuovo metodo sull'helper
        p1.getmyPlayerBoard().getHousingUnits().getFirst().checkNearbyUnits(p1.getmyPlayerBoard());



        //maincockpit
        MainCockpitComp mc = new MainCockpitComp();
        mc.setNumHumans(2);
        assertEquals(2, mc.getNumHumans());
        mc.setBrownAlien(false);
        mc.setPurpleAlien(false);
        try {
            mc.setBrownAlien(true);
        } catch (Exception e) {
            assertEquals("MainCockpit can't have aliens", e.getMessage());
        }
        try {
            mc.setPurpleAlien(true);
        } catch (Exception e) {
            assertEquals("MainCockpit can't have aliens", e.getMessage());
        }
        mc.isNearBrownAddon();
        mc.isNearPurpleAddon();
        mc.isBrownAlien();
        mc.isPurpleAlien();

        try {
            mc.setNearBrownAddon(true);
        } catch (Exception e) {
            assertEquals("can't have aliens", e.getMessage());
        }
        try {
            mc.setNearPurpleAddon(true);
        } catch (Exception e) {
            assertEquals("can't have aliens", e.getMessage());
        }
        mc.notifyUnit(true, new ModularHousingUnit());
        mc.clone(p1.getmyPlayerBoard());


        ArrayList<IntegerPair> alp = new ArrayList<>();
        p1.setState(new Killing());
        game.getGag().getCardsDeck().getFirst().setBoard(game.getGameBoard()); // per qualche motivo se chiamo questi metodi su carta 32 già usata crasha e perdo
        game.getGag().getCardsDeck().getFirst().setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().getFirst().updateStates();
        game.getGag().getCardsDeck().getFirst().CardEffect();
        p1.setCard(game.getGag().getCardsDeck().getFirst());
        alp.add(new IntegerPair(6,6));
        alp.add(new IntegerPair(4,5));
        alp.add(new IntegerPair(7,4));
        KillCommand kc = new KillCommand(alp, game.getGameID(), p1.GetID(), game.getLv(), "KILL", null);
        kc.execute(p1);
        KillCommand kc2 = new KillCommand(alp, game.getGameID(), p1.GetID(), game.getLv(), "KILL", null);
        kc2.execute(p1);
        KillCommand kc3 = new KillCommand(alp, game.getGameID(), p1.GetID(), game.getLv(), "KILL", null);
        try {
            kc3.execute(p1);
        } catch (Exception e) {
            assertEquals("no more humans to kill", e.getMessage());
        }





        //plasmadrill


        //type 1
        game.getGag().getTilesDeck().get(110).RotateDx();
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(110), 6, 7, false);
        PlasmaDrill plasmaDrill = (PlasmaDrill) game.getGag().getTilesDeck().get(110).getComponent();
        plasmaDrill.rotate(true);
        plasmaDrill.rotate(false);
        plasmaDrill.getCannonPower();
        p1.getmyPlayerBoard().removeTile(6, 7);
        plasmaDrill.clone(p1.getmyPlayerBoard());
        plasmaDrill.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        plasmaDrill.accept(new GetPlasmaDrillPower(1), new GiveAttack());
        assertEquals(1, plasmaDrill.getType());

        game.getGag().getTilesDeck().get(110).RotateSx();
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(110), 6, 7, false);
        PlasmaDrill plasmaDrill_1 = (PlasmaDrill) game.getGag().getTilesDeck().get(110).getComponent();
        plasmaDrill_1.rotate(true);
        plasmaDrill_1.rotate(false);
        plasmaDrill_1.getCannonPower();
        p1.getmyPlayerBoard().removeTile(6, 7);
        plasmaDrill_1.clone(p1.getmyPlayerBoard());
        plasmaDrill_1.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        plasmaDrill_1.accept(new GetPlasmaDrillPower(1), new GiveAttack());
        assertEquals(1, plasmaDrill_1.getType());


        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(125), 6, 7, false);
        PlasmaDrill plasmaDrill2 = (PlasmaDrill) game.getGag().getTilesDeck().get(125).getComponent();
        plasmaDrill2.rotate(true);
        plasmaDrill2.rotate(false);
        plasmaDrill2.getCannonPower();
        p1.getmyPlayerBoard().removeTile(6, 7);
        plasmaDrill2.clone(p1.getmyPlayerBoard());
        plasmaDrill2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        plasmaDrill2.accept(new GetPlasmaDrillPower(1), new GiveAttack());
        assertEquals(2, plasmaDrill2.getType());


        game.getGag().getTilesDeck().get(125).RotateDx();
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(125), 6, 7, false);
        PlasmaDrill plasmaDrill_2 = (PlasmaDrill) game.getGag().getTilesDeck().get(125).getComponent();
        plasmaDrill_2.rotate(true);
        plasmaDrill_2.rotate(false);
        plasmaDrill_2.getCannonPower();
        p1.getmyPlayerBoard().removeTile(6, 7);
        plasmaDrill_2.clone(p1.getmyPlayerBoard());
        plasmaDrill_2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        plasmaDrill_2.accept(new GetPlasmaDrillPower(1), new GiveAttack());
        assertEquals(2, plasmaDrill_2.getType());





        //powercenter

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(10), 6, 7, false);
        PowerCenter powercenter = (PowerCenter) game.getGag().getTilesDeck().get(10).getComponent();
        powercenter.rotate(true);
        powercenter.rotate(false);
        powercenter.setType(0);
        powercenter.getX();
        powercenter.getY();
        p1.getmyPlayerBoard().removeTile(6, 7);
        powercenter.clone(p1.getmyPlayerBoard());
        powercenter.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        try {
            powercenter.accept(new UseEnergyAction(p1.getmyPlayerBoard()), new ConsumingEnergy());
        } catch (Exception e) {
            assertEquals("cannot exceed 0 energy", e.getMessage());
        }



        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(10), 6, 7, false);
        PowerCenter powercenter2 = (PowerCenter) game.getGag().getTilesDeck().get(10).getComponent();
        powercenter2.rotate(true);
        powercenter2.rotate(false);
        powercenter.setType(3);
        powercenter.getX();
        powercenter.getY();
        p1.getmyPlayerBoard().removeTile(6, 7);
        powercenter2.clone(p1.getmyPlayerBoard());
        powercenter2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        powercenter2.accept(new UseEnergyAction(p1.getmyPlayerBoard()), new ConsumingEnergy());



        //sewerpipes


        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(50), 6, 7, false);
        SewerPipes sewerPipes = (SewerPipes) game.getGag().getTilesDeck().get(50).getComponent();
        sewerPipes.rotate(true);
        sewerPipes.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        sewerPipes.clone(p1.getmyPlayerBoard());
        sewerPipes.controlValidity(p1.getmyPlayerBoard(), 0, 0);



        //shieldGenerator

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(145), 6, 7, false);
        ShieldGenerator shieldGenerator = (ShieldGenerator) game.getGag().getTilesDeck().get(145).getComponent();
        shieldGenerator.rotate(true);
        shieldGenerator.rotate(false);
        p1.getmyPlayerBoard().removeTile(6, 7);
        shieldGenerator.clone(p1.getmyPlayerBoard());
        shieldGenerator.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(12, shieldGenerator.getType());



        //spacevoid

        SpaceVoid sv = new SpaceVoid();
        try{
            sv.rotate(false);
        } catch (Exception e){
            assertEquals("you can't rotate spaceVoid tile", e.getMessage());
        }
        sv.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        sv.insert(p1.getmyPlayerBoard(), 0,0);
        sv.remove(p1.getmyPlayerBoard());
        sv.clone(p1.getmyPlayerBoard());




        //modularhousingunit TODO: aumentare ancora la percentuale

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(36), 6, 7, false);
        ModularHousingUnit mhu2 = (ModularHousingUnit) game.getGag().getTilesDeck().get(36).getComponent();
        mhu2.notifyUnit(true, new ModularHousingUnit());
        mhu2.setNumHumans(2);
        assertEquals(2,mhu2.getNumHumans());
        mhu2.setNumHumans(0);
        assertEquals(0,mhu2.getNumHumans());
        mhu2.setPurpleAlien(true);
        assertTrue(mhu2.isPurpleAlien());
        mhu2.setPurpleAlien(false);
        assertFalse(mhu2.isPurpleAlien());
        mhu2.setBrownAlien(true);
        assertTrue(mhu2.isBrownAlien());
        mhu2.setBrownAlien(false);
        assertFalse(mhu2.isBrownAlien());
        mhu2.setNearBrownAddon(true);
        assertTrue(mhu2.isNearBrownAddon());
        mhu2.setNearBrownAddon(false);
        assertFalse(mhu2.isNearBrownAddon());
        mhu2.setNearPurpleAddon(true);
        assertTrue(mhu2.isNearPurpleAddon());
        mhu2.setNearPurpleAddon(false);
        assertFalse(mhu2.isNearPurpleAddon());


        for(HousingUnit housingUnit : p1.getmyPlayerBoard().getHousingUnits()){
            housingUnit.controlValidity(p1.getmyPlayerBoard(), housingUnit.getX(), housingUnit.getY());
        }
        helper.HumansSetter1(p1.getmyPlayerBoard());
        mhu2.remove(p1.getmyPlayerBoard());
        mhu2.isPopulated();
        mhu2.checkNearbyUnits(p1.getmyPlayerBoard());
        p1.getmyPlayerBoard().getHousingUnits().getFirst().checkNearbyUnits(p1.getmyPlayerBoard());

        for(HousingUnit housingUnit : p1.getmyPlayerBoard().getHousingUnits()){
            housingUnit.kill();
        }
        mhu2.clone(p1.getmyPlayerBoard());







        //storage

        //type 2

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(18), 6, 7, false);
        Storage sc = (StorageCompartment) game.getGag().getTilesDeck().get(18).getComponent();
        sc.rotate(true);
        sc.rotate(false);
        sc.clone(p1.getmyPlayerBoard());
//        ArrayList<Goods> goodsList = new ArrayList<>();
//        goodsList.add(new BLUE());
//        goodsList.add(new YELLOW());
//        goodsList.add(new RED());
//        p1.getmyPlayerBoard().setRewards(goodsList);

        try {
            ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new RED());
        } catch (Exception e){
            assertEquals("StorageCompartment cannot contain special Goods", e.getMessage());
        }
        ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        try {
            ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        } catch (Exception e){
            assertEquals("StorageCompartment is full!", e.getMessage());
        }
        p1.getmyPlayerBoard().removeTile(6, 7);
        sc.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(2, sc.getType());



        //type 3

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(28), 6, 7, false);
        Storage sc2 = (StorageCompartment) game.getGag().getTilesDeck().get(28).getComponent();
        sc2.rotate(true);
        sc2.rotate(false);
        try {
            ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new RED());
        } catch (Exception e){
            assertEquals("StorageCompartment cannot contain special Goods", e.getMessage());
        }
        ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        try {
            ((StorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        } catch (Exception e){
            assertEquals("StorageCompartment is full!", e.getMessage());
        }
        p1.getmyPlayerBoard().removeTile(6, 7);
        sc2.clone(p1.getmyPlayerBoard());
        sc2.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(3, sc2.getType());







        //special storage


        //type 1
        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(60), 6, 7, false);
        SpecialStorageCompartment sc3 = (SpecialStorageCompartment) game.getGag().getTilesDeck().get(60).getComponent();
        sc3.rotate(true);
        sc3.rotate(false);
        ((SpecialStorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new RED());
        try {
            ((SpecialStorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        } catch (Exception e){
            assertEquals("SpecialStorageCompartment is full!", e.getMessage());
        }
        sc3.getValue(0);
        ArrayList<Goods> goodsList = new ArrayList<>();
        goodsList.add(new RED());
        sc3.setGoods(goodsList);
        sc3.removeGood(-1);
        sc3.removeGood(10);
        p1.getmyPlayerBoard().removeTile(6, 7);
        sc3.clone(p1.getmyPlayerBoard());
        sc3.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(1, sc3.getType());



        //type 2

        p1.getmyPlayerBoard().insertTile(game.getGag().getTilesDeck().get(65), 6, 7, false);
        SpecialStorageCompartment sc4 = (SpecialStorageCompartment) game.getGag().getTilesDeck().get(65).getComponent();
        sc4.rotate(true);
        sc4.rotate(false);
        ((SpecialStorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new RED());
        ((SpecialStorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new RED());
        try {
            ((SpecialStorageCompartment) p1.getmyPlayerBoard().getPlayerBoard()[6][7].getComponent()).addGood(new BLUE());
        } catch (Exception e){
            assertEquals("SpecialStorageCompartment is full!", e.getMessage());
        }
        sc4.getValue(0);
        ArrayList<Goods> goodsList2 = new ArrayList<>();
        goodsList.add(new RED());
        sc4.setGoods(goodsList2);
        sc4.removeGood(-1);
        sc4.removeGood(10);
        p1.getmyPlayerBoard().removeTile(6, 7);
        sc4.clone(p1.getmyPlayerBoard());
        sc4.controlValidity(p1.getmyPlayerBoard(), 0, 0);
        assertEquals(2, sc4.getType());



        //storage

        Storage str = new Storage() {
            @Override
            public Component clone(PlayerBoard playerBoard) {
                return null;
            }
        };
        str.insert(p1.getmyPlayerBoard(), 6, 7);
        str.remove(p1.getmyPlayerBoard());
        try {
            str.getValue(0);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        str.removeGood(-1);
        str.addGood(new BLUE());
        str.getGoods();
        str.sendState();



        //tilesets TODO: mancherebbero le linee coperte da synchronized

        TileSets ts = new TileSets(new GAGen());
        ts.getNewTile(-1);
        ts.removeListeners(vv);
        ts.setListeners(vv);











    }


}