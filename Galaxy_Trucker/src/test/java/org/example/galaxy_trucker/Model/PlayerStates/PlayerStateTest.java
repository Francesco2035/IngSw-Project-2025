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
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.TestSetupHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    Game game;
    GameBoard Gboard;
    GameController gc;
    Player p1;
    VirtualView vv;
    FlightController c1;


    @Test
    void allows() throws IOException {

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


        //aggiungere housing units manualmente al player per eseguire la execute di addcrewstate

        AddCrewState adc = new AddCrewState();
        adc.allows(((AddCrewCommand) null));
        adc.createDefaultCommand(game.getGameID(), p1);
        ac.toClientState();

    }
}