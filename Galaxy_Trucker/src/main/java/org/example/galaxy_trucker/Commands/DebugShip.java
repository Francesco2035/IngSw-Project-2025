package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class DebugShip extends Command implements Serializable {


    @JsonProperty("commandType")
    private final String commandType = "DebugShip";


    public DebugShip(String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.gameId = gameId;
        this.playerId = playerId;
        this.title = title;
    }

    public DebugShip() {

    }


    @Override
    public void execute(Player player) throws IOException {

        PlayerBoard debugShip = new PlayerBoard(lv);
        GAGen gag = new GAGen();

        ArrayList<Tile> tiles = gag.getTilesDeck();
        Tile t1 = tiles.get(133); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
        Tile t2 = tiles.get(102); //none,cannon, single, universal , plasmadrill    ruota sx due volte factos
        Tile t3 = tiles.get(128); //doublem cannon, none, unviersal. plasmadrill  factos
        Tile t4 = tiles.get(149); //double,k single, double, single shield none, double, double, universal ruota a sx factos
        Tile specialStorage = tiles.get(57);
        Tile normalStorage  =tiles.get(24);
        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
        Tile t9 = tiles.get(32); //SINGLE", "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
        Tile t10 = tiles.get(33); //SINGLE", "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
        Tile t11 = tiles.get(73); //SINGLE", "DOUBLE", "NONE", "MOTOR" un motore factos
        Tile t12 = tiles.get(145);
        Tile powerCenter = tiles.get(12);
        Tile powerCenter2 = tiles.get(5);
        Tile plasmaDrill = tiles.get(130);
        Tile addonspurple = tiles.get(142);
        Tile modular1 = tiles.get(39);
        Tile sewerpipes = tiles.get(52);
        Tile modular2 = tiles.get(47);
        Tile modular3 = tiles.get(48);
        Tile hotWaterHeater = tiles.get(92);
        Tile shield = tiles.get(151);

        shield.RotateSx();

        addonspurple.RotateSx();
        plasmaDrill.RotateDx();

        debugShip.setListener(player.getmyPlayerBoard().getListener());
        debugShip.setRewardsListener(player.getmyPlayerBoard().getRewardsListener());

        t2.RotateSx();
        t2.RotateSx();
        t8.RotateDx();
        t4.RotateSx();
        t10.RotateDx();
        specialStorage.RotateDx();
        specialStorage.RotateDx();


        debugShip.insertTile(t1, 6,7, false);
        debugShip.insertTile(t8, 7,6, false);
        debugShip.insertTile(t11, 6,5, false);
        debugShip.insertTile(t9, 5,7, false);
        debugShip.insertTile(t4, 5,5, false);
        debugShip.insertTile(t10, 4,5, false);
        debugShip.insertTile(t12, 6,8, false);
        debugShip.insertTile(specialStorage, 7,8, false);
        debugShip.insertTile(normalStorage, 7,9, false);
        debugShip.insertTile(powerCenter, 6,9, false);
        debugShip.insertTile(powerCenter2, 5,4, false);
        debugShip.insertTile(plasmaDrill,8,9, false);
        debugShip.insertTile(addonspurple,6,4, false);
        debugShip.insertTile(modular1,7,4, false);
        debugShip.insertTile(sewerpipes,7,3, false);
        debugShip.insertTile(hotWaterHeater,8,3, false);
        debugShip.insertTile(player.getmyPlayerBoard().getTile(6,6),6,6, false);
        player.setMyPlance(debugShip);

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
