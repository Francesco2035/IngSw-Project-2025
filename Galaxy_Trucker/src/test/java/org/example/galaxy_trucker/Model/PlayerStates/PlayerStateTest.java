package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.Controller.FlightController;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.SolarSystem;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Goods.RED;
import org.example.galaxy_trucker.Model.Goods.YELLOW;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.NewTestSetupHelper;
import org.example.galaxy_trucker.View.TUI.InputReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {


    @Test
    void allows() throws IOException, InterruptedException {

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


        PlayerState ps = new PlayerState() {
            @Override
            public Command createDefaultCommand(String gameId, Player player) {
                return null;
            }

            @Override
            public PhaseEvent toClientState() {
                return null;
            }

        };

        ps.allows(((Command) null));

        ps.allows((AcceptCommand) null);
        ps.allows((AddCrewCommand) null);
        ps.allows((ChoosingPlanetsCommand) null);
        ps.allows((ConsumeEnergyCommand) null);
        ps.allows((DefendFromLargeCommand) null);
        ps.allows((DefendFromSmallCommand) null);
        ps.allows((FinishBuildingCommand) null);
        ps.allows((FinishBuildingCommand) null);
        ps.allows((GiveAttackCommand) null);
        ps.allows((GiveSpeedCommand) null);
        ps.allows((HandleCargoCommand) null);
        ps.allows((TheftCommand) null);
        ps.allows((BuildingCommand) null);
        ps.allows((KillCommand) null);
        ps.allows((LoginCommand) null);
        ps.allows((QuitCommand) null);
        ps.allows((ReadyCommand) null);
        ps.allows((RemoveTileCommand) null);
        ps.allows((SelectChunkCommand) null);
        ps.allows((AddCrewAction) null);
        ps.allows((AddGoodAction) null);
        ps.allows((GetEnginePower) null);
        ps.allows((GetGoodAction) null);
        ps.allows((GetPlasmaDrillPower) null);
        ps.allows((KillCrewAction) null);
        ps.allows((UseEnergyAction) null);
        ps.allows((DebugShip) null);




        Accepting ac = new Accepting();
        ac.allows(((AcceptCommand) null));
        ac.createDefaultCommand(game.getGameID(), p1);
        ac.toClientState();



        AddCrewState adc = new AddCrewState();
        adc.allows(((AddCrewCommand) null));
        p1.setState(adc);
        adc.createDefaultCommand(game.getGameID(), p1).execute(p1);
        adc.toClientState();



        BaseState bs = new BaseState();
        bs.allows(new LoginCommand());
        bs.allows(new ReadyCommand());
        bs.allows(new QuitCommand());
        bs.allows(new DebugShip());
        p1.setState(bs);
        bs.createDefaultCommand(game.getGameID(), p1).execute(p1);
        bs.toClientState();



        BuildingShip bgs = new BuildingShip();
        bgs.allows(new BuildingCommand());
        bgs.allows(new ReadyCommand());
        bgs.allows(new FinishBuildingCommand());
        p1.setState(bgs);
        bgs.createDefaultCommand(game.getGameID(), p1).execute(p1);
        bgs.toClientState();



        CheckValidity cv = new CheckValidity();
        cv.allows(new RemoveTileCommand());
        cv.allows(new ReadyCommand());
        p1.setState(cv);
        cv.createDefaultCommand(game.getGameID(), p1).execute(p1); /// da aggiungere potenzialmente una board invalida per testare il resto dell'execute
        cv.toClientState();



        ChoosePosition chp = new ChoosePosition();
        chp.allows(new FinishBuildingCommand());
        p1.setState(chp);
        chp.createDefaultCommand(game.getGameID(), p1).execute(p1);
        chp.toClientState();



        ChoosingPlanet cp = new ChoosingPlanet();
        cp.allows(new ChoosingPlanetsCommand());
        p1.setState(cp);
        game.getGag().getCardsDeck().get(22).setBoard(game.getGameBoard());
        game.getGag().getCardsDeck().get(22).setConcurrentCardListener(gc);
        p1.setCard(game.getGag().getCardsDeck().get(22));
        cp.createDefaultCommand(game.getGameID(), p1).execute(p1); // non funziona perchè bisogna settare anche currentcard
        cp.toClientState();



        ConsumingEnergy ce = new ConsumingEnergy();
        ce.allows(new ConsumeEnergyCommand());
        ce.allows(new UseEnergyAction(p1.getmyPlayerBoard()));
        p1.setState(ce);
        game.getGag().getCardsDeck().get(30).setBoard(game.getGameBoard());
        game.getGag().getCardsDeck().get(30).setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().get(30).updateSates();
        game.getGag().getCardsDeck().get(30).CardEffect(); // forse questo è sbagliato nelle carte. perchè losers non viene creato nel caso di default command
        p1.setCard(game.getGag().getCardsDeck().get(30));
        game.getGag().getCardsDeck().get(31).setBoard(game.getGameBoard());
        game.getGag().getCardsDeck().get(31).setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().get(31).updateSates();
        game.getGag().getCardsDeck().get(31).CardEffect(); // forse questo è sbagliato nelle carte. perchè losers non viene creato nel caso di default command
        p1.setCard(game.getGag().getCardsDeck().get(31));
        game.getGag().getCardsDeck().get(32).setBoard(game.getGameBoard());
        game.getGag().getCardsDeck().get(32).setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().get(32).updateSates();
        game.getGag().getCardsDeck().get(32).CardEffect(); // forse questo è sbagliato nelle carte. perchè losers non viene creato nel caso di default command
        p1.setCard(game.getGag().getCardsDeck().get(32));
        ce.createDefaultCommand(game.getGameID(), p1).execute(p1);
        ce.toClientState();



        DefendingFromLarge dfl = new DefendingFromLarge();
        p1.setState(dfl);
        dfl.allows(new DefendFromLargeCommand(new IntegerPair(4, 5), new IntegerPair(6, 7), game.getGameID(), p1.GetID(), game.getLv(), "DEFEND", null));
        dfl.allows(new UseEnergyAction(p1.getmyPlayerBoard()));
        dfl.createDefaultCommand(game.getGameID(), p1).execute(p1);
        dfl.toClientState();



        DefendingFromSmall dfs = new DefendingFromSmall();
        p1.setState(dfs);
        dfs.allows(new DefendFromSmallCommand(new IntegerPair(6, 7), game.getGameID(), p1.GetID(), game.getLv(), "DEFEND", null));
        dfs.createDefaultCommand(game.getGameID(), p1).execute(p1);
        dfs.toClientState();



        GiveAttack ga = new GiveAttack();
        ga.allows(new GetPlasmaDrillPower(2.0));
        ga.allows(new GetPlasmaDrillPower(1.0));
        p1.setState(ga);
        ArrayList<IntegerPair> ip1 = new ArrayList<>();
        ip1.add(new IntegerPair(4, 5));
        ip1.add(new IntegerPair(6, 7));
        ip1.add(new IntegerPair(7, 8));
        ga.allows(new GiveAttackCommand(ip1, game.getGameID(), p1.GetID(), game.getLv(), "GIVEATTACK", null));
        ga.createDefaultCommand(game.getGameID(), p1).execute(p1);
        ga.toClientState();



        GiveSpeed gs = new GiveSpeed();
        gs.allows(new GetEnginePower(1));
        p1.setState(gs);
        game.getGag().getCardsDeck().get(34).setBoard(game.getGameBoard()); // per qualche motivo se chiamo questi metodi su carta 32 già usata crasha e perdo
        game.getGag().getCardsDeck().get(34).setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().get(34).updateSates();
        game.getGag().getCardsDeck().get(34).CardEffect();
        p1.setCard(game.getGag().getCardsDeck().get(34));
        ArrayList<IntegerPair> ip2 = new ArrayList<>();
        ip2.add(new IntegerPair(4, 5));
        ip2.add(new IntegerPair(6, 7));
        ip2.add(new IntegerPair(7, 8));
        gs.allows(new GiveSpeedCommand(ip2, game.getGameID(), p1.GetID(), game.getLv(), "GIVESPEED", null));
        gs.createDefaultCommand(game.getGameID(), p1).execute(p1);
        gs.toClientState();



        HandleCargo hc = new HandleCargo();
        p1.setState(hc);
        hc.allows(new HandleCargoCommand(3, new IntegerPair(3, 4), 4, new IntegerPair(6, 7), game.getGameID(), p1.GetID(), game.getLv(), "HANDLECARGO", null));
        hc.allows(new AddGoodAction(new BLUE(), p1.getmyPlayerBoard(), 6, 9));
        hc.allows(new GetGoodAction(9, p1.getmyPlayerBoard(), 7, 8));
        hc.createDefaultCommand(game.getGameID(), p1).execute(p1);
        hc.toClientState();



        HandleDestruction hd = new HandleDestruction();
        p1.setState(hd);
        hd.allows(new SelectChunkCommand(new IntegerPair(6, 7), game.getGameID(), p1.GetID(), game.getLv(), "HANDLEDESTRUCTION", null));
        p1.getmyPlayerBoard().handleAttack(6, 7);
        hd.createDefaultCommand(game.getGameID(), p1).execute(p1);
        hd.toClientState();



        HandleTheft ht = new HandleTheft();
        ht.allows(new TheftCommand(5, new IntegerPair(6, 9), game.getGameID(), p1.GetID(), game.getLv(), "HANDLETHEFT", null));
        ht.allows(new GetGoodAction(6, p1.getmyPlayerBoard(), 7, 8));
        p1.setState(new HandleCargo());
        ArrayList<Goods> goodsList = new ArrayList<>();
        goodsList.add(new BLUE());
        goodsList.add(new YELLOW());
        goodsList.add(new RED());
        p1.getmyPlayerBoard().setRewards(goodsList);
        gc.getControllerMap().values().stream().findFirst().orElseThrow().action(new HandleCargoCommand(1, new IntegerPair(7, 9), 1, new IntegerPair(7, 9), game.getGameID(), p1.GetID(), game.getLv(), "GetFromRewards", null), gc);
        p1.setState(ht);
        ht.createDefaultCommand(game.getGameID(), p1).execute(p1);
        ht.toClientState();



        Killing k = new Killing();
        p1.setState(k);
        ArrayList<IntegerPair> ip3 = new ArrayList<>();
        ip3.add(new IntegerPair(4, 5));
        ip3.add(new IntegerPair(6, 7));
        ip3.add(new IntegerPair(7, 8));
        k.allows(new KillCommand(ip3, game.getGameID(), p1.GetID(), game.getLv(), "KILLING", null));
        k.allows(new KillCrewAction(p1.getmyPlayerBoard()));
        game.getGag().getCardsDeck().get(1).setBoard(game.getGameBoard()); // per qualche motivo se chiamo questi metodi su carta 32 già usata crasha e perdo
        game.getGag().getCardsDeck().get(1).setConcurrentCardListener(gc);
        game.getGag().getCardsDeck().get(1).updateSates();
        game.getGag().getCardsDeck().get(1).CardEffect();
        p1.setCard(game.getGag().getCardsDeck().get(1));
        k.createDefaultCommand(game.getGameID(), p1).execute(p1);
        k.toClientState();



        ReadCardState rcs = new ReadCardState();
        rcs.shouldAct(p1);
        rcs.createDefaultCommand(game.getGameID(), p1);
        rcs.toClientState();


        Waiting w = new Waiting();
        w.shouldAct(p1);;
        w.createDefaultCommand(game.getGameID(), p1);
        w.toClientState();

    }
}