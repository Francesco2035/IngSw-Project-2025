package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.DOUBLE;
import org.example.galaxy_trucker.Model.Connectors.SINGLE;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.AddCrewState;
import org.example.galaxy_trucker.Model.Tiles.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewTestSetupHelper {

//    private final PlayerBoard playerBoard1 = new PlayerBoard(2);
//    private final PlayerBoard playerBoard2 = new PlayerBoard(2);
//    private final PlayerBoard playerBoard3 = new PlayerBoard(2);
//    private final PlayerBoard playerBoard4 = new PlayerBoard(2);
//    private final PlayerBoard playerBoard5 = new PlayerBoard(2);


    public PlayerBoard createInitializedBoard1() throws IOException {


        final PlayerBoard playerBoard1 = new PlayerBoard(2);

        GAGen gag1 = new GAGen();
        ArrayList<Tile> tiles = gag1.getTilesDeck();
        Tile t1 = tiles.get(133); //"SINGLE", "DOUBLE", "SINGLE", "NONE addons, br factos
        Tile t2 = tiles.get(102); //none,cannon, single, universal , plasmadrill    ruota sx due volte factos
        Tile t3 = tiles.get(128); //doublem cannon, none, unviersal. plasmadrill  factos
        Tile t4 = tiles.get(149); //double,k single, double, single shield none, double, double, universal ruota a sx factos
        Tile specialStorage = tiles.get(57);
        Tile normalStorage  =tiles.get(24);
        Tile t8 = tiles.get(56);  // universal, universal, double, double. sewerpips factos
        Tile t9 = tiles.get(32); //SINGLE", "NONE", "NONE", "UNIVERSAL housingjfoaihj factos
        Tile t10 = tiles.get(33); //SINGLE", "SINGLE", "DOUBLE", "SINGLE moudsajdahoyusingunit ruota dx factos
        Tile t11 = tiles.get(73); //"None, "DOUBLE", "Universal", "MOTOR" un motore factos
        Tile t12 = tiles.get(145);
        Tile powerCenter = tiles.get(12);
        Tile powerCenter2 = tiles.get(5); //single, uni, none, none
        Tile plasmaDrill = tiles.get(130);
        Tile addonspurple = tiles.get(142); // uni, none,single, none
        Tile modular1 = tiles.get(39); // double , single double, none
        Tile sewerpipes = tiles.get(52); // uni, sigle ,uni , double
        Tile modular2 = tiles.get(47);
        Tile modular3 = tiles.get(48);
        Tile hotWaterHeater = tiles.get(92);
        Tile shield = tiles.get(151);
        Tile replacement_house= tiles.get(45);

        shield.RotateSx();

        addonspurple.RotateSx(); //none single, none,uni
        plasmaDrill.RotateDx();

        playerBoard1.insertTile(t1, 6,7, true);
        t2.RotateSx();
        t2.RotateSx();
        t8.RotateDx();
        playerBoard1.insertTile(t8, 7,6, false);
        playerBoard1.insertTile(t11, 6,5, false);
        playerBoard1.insertTile(t9, 5,7, false);
        t4.RotateSx();
        playerBoard1.insertTile(t4, 5,5, false);
        t10.RotateDx();
        playerBoard1.insertTile(t10, 4,5, false);

        playerBoard1.insertTile(t12, 6,8, false);

        specialStorage.RotateDx();
        specialStorage.RotateDx();
        playerBoard1.insertTile(specialStorage, 7,8, false);

        playerBoard1.insertTile(normalStorage, 7,9, false);
        playerBoard1.insertTile(powerCenter, 6,9, false);
        playerBoard1.insertTile(powerCenter2, 5,4, false);
        playerBoard1.insertTile(plasmaDrill,8,9, false);
        playerBoard1.insertTile(addonspurple,6,4, false);
        playerBoard1.insertTile(replacement_house,7,4, false);
        playerBoard1.insertTile(sewerpipes,7,3, false);
        playerBoard1.insertTile(hotWaterHeater,8,3, false);
        //  playerBoard.insertTile(modular1,5,6);
//        playerBoard.insertTile(modular2,8,4);
//        playerBoard.insertTile(modular3,8,5);
//        playerBoard.insertTile(shield,6,3);
        MainCockpitComp mp = new MainCockpitComp();
        Tile mc = new Tile(mp, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        playerBoard1.insertTile(mc,6,6, false);
        return playerBoard1;

    }


    @Test
    public void boardChecks() throws IOException {
        PlayerBoard playerBoard = createInitializedBoard1();
        assertTrue(playerBoard.checkValidity());
    }

    public PlayerBoard createInitializedBoard2() throws IOException {
        // crea la 39 e 48 ad hoc perché le usa gia la uno e ovviamente crea problemi popolarle entrambe

        final PlayerBoard playerBoard2 = new PlayerBoard(2);

        GAGen gag2 = new GAGen();
        ArrayList<Tile> tiles = gag2.getTilesDeck();
        Tile t1 = tiles.get(37); //House:  single,double,single,double
        Tile t2 = tiles.get(35); //House: singe,double,none,none
        Tile t3 = tiles.get(40);//house: double,double,single,double
        Tile t4 = tiles.get(99); //Cannon: none,cannon,none,double
        Tile t5 = tiles.get(117);//Cannon: double, cannon,single,uni
        Tile t6 = tiles.get(137);//BrownAddon: uni,single,none,none
        Tile t7 = tiles.get(150);//Shield: uni,none,none,single
        Tile t8 = tiles.get(10);//battery uni,uni,none,none
        Tile t9 = tiles.get(25);//storage normale dim 2 uni uni none none
        Tile t10 = tiles.get(93);//DoubleEngine double uni none engine
        Tile t11 = tiles.get(79);//Engine double none single engine


        t1.RotateSx();
        t2.RotateDx();
        t3.RotateSx();
        t3.RotateSx();
        t5.RotateDx();
        t6.RotateDx();
        t9.RotateDx();
        t9.RotateDx();

        playerBoard2.insertTile(t1,5,6, false);
        playerBoard2.insertTile(t7,6,7, false);
        playerBoard2.insertTile(t2,7,7, false);
        playerBoard2.insertTile(t5,7,8, false); // cannon looking right tipo 1
        playerBoard2.insertTile(t6,6,5, false);
        playerBoard2.insertTile(t3,5,5, false);
        playerBoard2.insertTile(t4,4,5, false); // cannon looking up tipo1
        playerBoard2.insertTile(t8,8,8, false);
        playerBoard2.insertTile(t9,5,4, false);
        playerBoard2.insertTile(t10,6,4, false);
        playerBoard2.insertTile(t11,8,7, false);
        MainCockpitComp mk = new MainCockpitComp();
        Tile mkk = new Tile(mk, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        playerBoard2.insertTile(mkk,6,6, false);
        return playerBoard2;



    }

    public PlayerBoard createInitializedBoard3(){
        //test housing unit

        final PlayerBoard playerBoard3 = new PlayerBoard(2);

        Tile modular1 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular2 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular3 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular4 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular5 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular6 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular7 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile modular8 = new Tile(new ModularHousingUnit(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile sewerPipes = new Tile(new SewerPipes(),UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        AlienAddons alienAddonsBrown = new AlienAddons();
        alienAddonsBrown.setType(0);
        Tile brown1 = new Tile(alienAddonsBrown,UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        Tile brown2 = new Tile(alienAddonsBrown,UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        AlienAddons alienAddonsPurple = new AlienAddons();
        alienAddonsPurple.setType(1);
        Tile purple = new Tile(alienAddonsPurple,UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
        playerBoard3.insertTile(modular1, 7, 8, false);
        playerBoard3.insertTile(modular2, 6, 7, false);
        playerBoard3.insertTile(modular3, 5, 7, false);
        playerBoard3.insertTile(modular4, 4, 7, false);
        playerBoard3.insertTile(modular5, 5, 6, false);
        playerBoard3.insertTile(modular6, 7, 5, false);
        playerBoard3.insertTile(modular7, 6, 4, false);
        playerBoard3.insertTile(modular8, 5, 4, false);
        playerBoard3.insertTile(purple, 7, 7, false);
        playerBoard3.insertTile(brown1, 6, 5, false);
        playerBoard3.insertTile(brown2, 8, 5, false);
        playerBoard3.insertTile(sewerPipes,6,8, false);
        playerBoard3.insertTile(sewerPipes,7,6, false);
        playerBoard3.insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE), 6, 6, false);


        return playerBoard3;
    }

    public PlayerBoard createInitializedBoard4(){

        final PlayerBoard playerBoard4 = new PlayerBoard(2);

        SINGLE SS = SINGLE.INSTANCE;
        DOUBLE DD = DOUBLE.INSTANCE;
        Tile single = new Tile(new SewerPipes(),SS,SS,SS,SS);
        Tile dd = new Tile(new SewerPipes(),DD,DD,DD,DD);
        playerBoard4.insertTile(single,6,7, false);
        playerBoard4.insertTile(dd,7,7, false);
        playerBoard4.insertTile(dd,7,6, false);

        return playerBoard4;
    }

    public PlayerBoard createInitializedBoard5() throws IOException {
        //invalid

        final PlayerBoard playerBoard5 = new PlayerBoard(2);
        //posizione non visitata
        //cannone adiacente a una tile
        //razzo adiacente a una tile
        //razzo ruotato
        //connessiojne illegae
        //connessione none none
        //connessione single none
        GAGen gag5 = new GAGen();
        ArrayList<Tile> tiles = gag5.getTilesDeck();
        Tile tile1 = tiles.get(20); //UNIVERSAL, NONE, NONE, NONE
        Tile tile2 = tiles.get(105);//CANNONE: NONE, CANNON, DOUBLE, UNIVERSAL
        Tile tile3 = tiles.get(70);//MOTORE: NONE, SINGLE, NONE, ENGINE
        Tile tile4 = tiles.get(54);//SEWERPIPES: UNIVERSAL, DOUBLE, UNIVERSAL, NONE
        Tile tile5 = tiles.get(94);//MOTORE: DOUBLE, UNIVERSAL, NONE, ENGINE
        Tile tile6 = tiles.get(57);//SEWERPIPES: UNIVERSAL,UNIVERSAL, DOUBLE, DOUBLE
        Tile tile7 = tiles.get(64);//SPCOMP: SINGLE, NONE, NONE, NONE
        Tile tile8 = tiles.get(21);
        Tile tile9 = tiles.get(21);
        Tile tile10 = tiles.get(21);
        tile5.RotateSx(); //engine a dx

        playerBoard5.insertTile(new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE),6,6, false);
        playerBoard5.insertTile(tile1,8,8, false); //non connesso
        playerBoard5.insertTile(tile2,6,5, false); //cannone connesso ma illegale
        playerBoard5.insertTile(tile4,5,5, false);
        playerBoard5.insertTile(tile5,5,6, false);
        playerBoard5.insertTile(tile6,7,6, false);
        playerBoard5.insertTile(tile7,7,7, false);

        return playerBoard5;
    }

    public void HumansSetter1(PlayerBoard playerBoard){

        for(HousingUnit housingUnit : playerBoard.getHousingUnits()){
            System.out.println("added 2 humans in  "+housingUnit.getX()+" "+housingUnit.getY());
            playerBoard.performAction(housingUnit,new AddCrewAction(2,false,false, playerBoard),new AddCrewState());
        }
//        playerBoard.getHousingUnits().clear();
    }


    public void HumansSetter2(PlayerBoard playerBoard){
        ArrayList<HousingUnit> HousingCoords = new ArrayList<>(playerBoard.getHousingUnits());
        for(HousingUnit housingUnit : HousingCoords){
            if(housingUnit.getX()==5 && housingUnit.getY()==5){
                playerBoard.performAction(housingUnit,new AddCrewAction(0,false,true, playerBoard),new AddCrewState());
            }
            else{
                playerBoard.performAction(housingUnit,new AddCrewAction(2,false,false, playerBoard),new AddCrewState());
            }
        }

    }


    public GameController GameControllerSetup() throws IOException {
        Game game = new Game(2, "poggi");
        GamesHandler gh = new GamesHandler();
        GameController gc = new GameController(game.getGameID(), game, gh,2, 4);
        Player p1 = new Player();
        p1.setId("player1");
        Player p2 = new Player();
        p2.setId("player2");
        VirtualView v1 = new VirtualView(p1.GetID(), "poggi", null, null);

        VirtualView v2 = new VirtualView(p1.GetID(), "poggi", null, null);

        gc.NewPlayer(p1, v1, UUID.randomUUID().toString());
        gc.NewPlayer(p2, v2, UUID.randomUUID().toString());
        return gc;

    }


}