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
    @JsonProperty("number")
    private int number;


    public DebugShip(String gameId, String playerId, int lv, String title, String token, int number) {
        super(gameId, playerId, lv, title, token,-1);
        this.gameId = gameId;
        this.playerId = playerId;
        this.title = title;
        this.number = number;
    }

    public DebugShip() {

    }


    @Override
    public void execute(Player player) throws IOException {

        PlayerBoard debugShip = player.getmyPlayerBoard();
        GAGen gag = new GAGen();

        ArrayList<Tile> tiles = gag.getTilesDeck();
//        Tile t1 = tiles.get(133); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
//        Tile t2 = tiles.get(102); //none,cannon, single, universal , plasmadrill    ruota sx due volte factos
//        Tile t3 = tiles.get(128); //doublem cannon, none, unviersal. plasmadrill  factos
//        Tile t4 = tiles.get(149); //double,k single, double, single shield none, double, double, universal ruota a sx factos
//        Tile specialStorage = tiles.get(57);
//        Tile normalStorage  =tiles.get(24);
//        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
//        Tile t9 = tiles.get(32); //SINGLE", "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
//        Tile t10 = tiles.get(33); //SINGLE", "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
//        Tile t11 = tiles.get(73); //SINGLE", "DOUBLE", "NONE", "MOTOR" un motore factos
//        Tile t12 = tiles.get(145);
//        Tile powerCenter = tiles.get(12);
//        Tile powerCenter2 = tiles.get(5);
//        Tile plasmaDrill = tiles.get(130);
//        Tile addonspurple = tiles.get(142);
//        Tile modular1 = tiles.get(39);
//        Tile sewerpipes = tiles.get(52);
//        Tile modular2 = tiles.get(47);
//        Tile modular3 = tiles.get(48);
//        Tile hotWaterHeater = tiles.get(92);
//        Tile shield = tiles.get(151);
//
//        shield.RotateSx();
//
//        addonspurple.RotateSx();
//        plasmaDrill.RotateDx();
//
//        debugShip.setListener(player.getmyPlayerBoard().getListener());
//        debugShip.setRewardsListener(player.getmyPlayerBoard().getRewardsListener());
//
//        t2.RotateSx();
//        t2.RotateSx();
//        t8.RotateDx();
//        t4.RotateSx();
//        t10.RotateDx();
//        specialStorage.RotateDx();
//        specialStorage.RotateDx();
//
//
//        debugShip.insertTile(t1, 6,7, false);
//        debugShip.insertTile(t8, 7,6, false);
//        debugShip.insertTile(t11, 6,5, false);
//        debugShip.insertTile(t9, 5,7, false);
//        debugShip.insertTile(t4, 5,5, false);
//        debugShip.insertTile(t10, 4,5, false);
//        debugShip.insertTile(t12, 6,8, false);
//        debugShip.insertTile(specialStorage, 7,8, false);
//        debugShip.insertTile(normalStorage, 7,9, false);
//        debugShip.insertTile(powerCenter, 6,9, false);
//        debugShip.insertTile(powerCenter2, 5,4, false);
//        debugShip.insertTile(plasmaDrill,8,9, false);
//        debugShip.insertTile(addonspurple,6,4, false);
//        debugShip.insertTile(modular1,7,4, false);
//        debugShip.insertTile(sewerpipes,7,3, false);
//        debugShip.insertTile(hotWaterHeater,8,3, false);
//        debugShip.insertTile(player.getmyPlayerBoard().getTile(6,6),6,6, false);


        //da cam
        switch (number){  /// fai id-1
            case 0:{
                Tile tile = tiles.get(37); ///mod housing uinit
                debugShip.insertTile(tile,6,5,false);

                tile = tiles.get(86);//engine
                debugShip.insertTile(tile,7,6,false);

                tile = tiles.get(60); //specialstorage
                debugShip.insertTile(tile,7,5,false);

                tile = tiles.get(121);//double drill
                debugShip.insertTile(tile,5,6,false);

                tile = tiles.get(137);//brownalien
                tile.RotateDx();
                debugShip.insertTile(tile,6,4,false);

                tile = tiles.get(149); //shield
                debugShip.insertTile(tile,6,7,false);

                tile = tiles.get(144);//shield
                tile.RotateSx();
                debugShip.insertTile(tile,5,7,false);

                tile = tiles.get(11); //triplepowercenter
                debugShip.insertTile(tile,7,7,false);

                tile = tiles.get(15);//  powercenter
                tile.RotateDx();
                tile.RotateDx();
                debugShip.insertTile(tile,5,5,false);

                tile = tiles.get(104); //plasmadrill
                debugShip.insertTile(tile,4,5,false);

                tile = tiles.get(118); //plasmadrill
                tile.RotateDx();
                tile.RotateDx();
                debugShip.insertTile(tile,8,7,false);

                tile = tiles.get(62);//special storage
                tile.RotateDx();
                tile.RotateDx();
                debugShip.insertTile(tile,7,4,false);

                tile = tiles.get(33); //modular housing
                tile.RotateDx();
                tile.RotateDx();
                debugShip.insertTile(tile,6,8,false);

                tile = tiles.get(142);//purplealien
                tile.RotateSx();
                debugShip.insertTile(tile,7,8,false);

                tile = tiles.get(78); // engine
                debugShip.insertTile(tile,8,4,false);

                tile = tiles.get(94); // doubleengine
                debugShip.insertTile(tile,8,5,false);

                tile = tiles.get(54); // sewerpies
                debugShip.insertTile(tile,8,3,false);

                tile = tiles.get(120); // plasmadrill
                tile.RotateSx();
                debugShip.insertTile(tile,7,3,false);

                tile = tiles.get(29); //triple storage
                debugShip.insertTile(tile,6,3,false);

                tile = tiles.get(125); // doubleplasmadrill
                debugShip.insertTile(tile,5,4,false);


                tile = tiles.get(45); //housing
                debugShip.insertTile(tile,5,8,false);

                tile = tiles.get(17); /// storage
                tile.RotateSx();
                debugShip.insertTile(tile,8,8,false);

                tile = tiles.get(147); //shield
                tile.RotateDx();
                debugShip.insertTile(tile,8,9,false);

                tile = tiles.get(97);
                debugShip.insertTile(tile,4,7,false);

                tile = tiles.get(146);//shield
                debugShip.insertTile(tile,6,9,false);

                break;
            }
            case 1:{
                Tile tile = tiles.get(91);//engine
                debugShip.insertTile(tile,7,6,false);

                tile = tiles.get(3); // double battery
                debugShip.insertTile(tile,6,7,false);


                tile = tiles.get(48);//housing
                debugShip.insertTile(tile,7,7,false);

                tile = tiles.get(54); //sewer
                debugShip.insertTile(tile,6,5,false);

                tile = tiles.get(5); //Powercenter
                debugShip.insertTile(tile,7,5,false);

                tile = tiles.get(40); // housingunits
                debugShip.insertTile(tile,7,4,false);

                tile = tiles.get(133);
                debugShip.insertTile(tile,8,4,false);


                /// Todo Rotazione Tile  shield buggata
                tile = tiles.get(145); //shield
                tile.RotateDx();
                debugShip.insertTile(tile,8,5,false);


                tile = tiles.get(10); // battery comp
                tile.RotateDx();
                debugShip.insertTile(tile,8,3,false);

                tile = tiles.get(75);//engine
                debugShip.insertTile(tile,8,7,false);

                tile = tiles.get(63); //special storage
                tile.RotateSx();
                debugShip.insertTile(tile,5,6,false);

                tile = tiles.get(19); //storage
                tile.RotateDx();
                debugShip.insertTile(tile,5,7,false);

                tile = tiles.get(131); // double plasma
                debugShip.insertTile(tile,4,7,false);

                tile = tiles.get(38); //housing
                tile.RotateDx();
                debugShip.insertTile(tile,5,5,false);

                tile = tiles.get(119); // plasmadrill
                debugShip.insertTile(tile,4,5,false);

                tile = tiles.get(47); //house
                tile.RotateSx();
                debugShip.insertTile(tile,7,8,false);

                tile = tiles.get(14); // triple power
                tile.RotateSx();
                debugShip.insertTile(tile,8,8,false);

                tile = tiles.get(87); //double hot water
                debugShip.insertTile(tile,8,9,false);

                tile = tiles.get(126);//double plasma
                tile.RotateDx();
                debugShip.insertTile(tile,7,9,false);

                tile = tiles.get(18); //storage
                tile.RotateDx();
                debugShip.insertTile(tile,6,9,false);

                tile = tiles.get(49);//sewerpipes
                debugShip.insertTile(tile,6,8,false);

                tile = tiles.get(150); //shield
                debugShip.insertTile(tile,5,8,false);

                tile = tiles.get(106); //plasmadrill
                debugShip.insertTile(tile,5,4,false);

                tile = tiles.get(0); //powercenter
                debugShip.insertTile(tile,6,4,false);

                tile = tiles.get(127); //double plasma
                tile.RotateSx();
                debugShip.insertTile(tile,6,3,false);

                tile = tiles.get(36);
                tile.RotateDx();
                debugShip.insertTile(tile,7,3,false);


                break;
            }

        }

    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
