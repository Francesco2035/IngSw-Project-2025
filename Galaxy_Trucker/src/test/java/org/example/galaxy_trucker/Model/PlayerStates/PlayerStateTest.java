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
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    Game game;
    GameBoard Gboard;
    GameController gc;
    Player p1;
    VirtualView vv;
    FlightController c1;


    @Test
    void allows() throws IOException, InterruptedException {

        game = new Game(2, "g1");
        gc = new GameController(game.getGameID(), game, new GamesHandler(), game.getLv(), 4);

        p1 = new Player();
        p1.setId("passos");
        game.NewPlayer(p1);
        p1.setBoards(game.getGameBoard());
        vv = new VirtualView(p1.GetID(), game.getGameID(), new RMIClient(new Client()), null);
        vv.setDisconnected(true);
//        assertTrue(p1.getmyPlayerBoard().checkValidity());
        Gboard = game.getGameBoard();
        c1 = new FlightController(p1, game.getGameID(), gc, false);
        gc.getControllerMap().put(p1.GetID(), c1);

        p1.setPhaseListener(vv);
        p1.setReadyListener(gc);
        p1.getmyPlayerBoard().setListener(vv);
        p1.setHandListener(vv);
        p1.getCommonBoard().getTilesSets().setListeners(vv);
        p1.setCardListner(vv);
        gc.getVirtualViewMap().put(p1.GetID(), vv);

        p1.setMyPlance(TestSetupHelper.createInitializedBoard1());
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
        ce.createDefaultCommand(game.getGameID(), p1).execute(p1);
        ce.toClientState();
    }
}