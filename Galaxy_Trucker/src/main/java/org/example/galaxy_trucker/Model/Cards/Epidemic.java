package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class Epidemic extends Card {
    private ArrayList<IntegerPair> infected;
    private ArrayList<IntegerPair> houses;
    Player currentPlayer;



    public Epidemic(int level, int time, GameBoard board) {
        super(level, time, board);
        this.infected = new ArrayList<>();


    }

    public void CardEffect(){


        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<HousingUnit> HousingCoords= new ArrayList<>();

        Tile[][] tiles;
        for(int i=0;i<Len;i++){
            this.infected.clear();
            this.currentPlayer = PlayerList.get(i);
            CurrentPlanche=this.currentPlayer.getmyPlayerBoard();
            tiles = CurrentPlanche.getPlayerBoard();
            HousingCoords.addAll(CurrentPlanche.getHousingUnits());


            ArrayList<IntegerPair> visited = new ArrayList<>();
            System.out.println("starting infected");
            for (int j = 0; j < HousingCoords.size(); j++) {
                findInfected(HousingCoords.get(j),CurrentPlanche);
                //findPaths(HousingCoords.get(j).getFirst(), HousingCoords.get(j).getSecond(), visited, valid ,playerTiles);

            }
            System.out.println("size of infected is: "+infected.size());
            for(int k=0;k<infected.size();k++){


                CurrentPlanche.performAction(tiles[this.infected.get(k).getFirst()][this.infected.get(k).getSecond()].getComponent(),new KillCrewAction(CurrentPlanche), PlayerStates.AcceptKilling);

            }


        }
    }

    public void findInfected(HousingUnit house,PlayerBoard playerBoard) {

        int x = house.getX();
        int y = house.getY();
        Tile [][] tiles=playerBoard.getPlayerBoard();
        int[][]valid=playerBoard.getValidPlayerBoard();
        PlayerBoard currBoard= this.currentPlayer.getmyPlayerBoard();
        ArrayList<IntegerPair> PopulatedHousecoords = new ArrayList<>();
        for(HousingUnit H:currBoard.getHousingUnits() ){
            if(H.isBrownAlien()||H.isPurpleAlien()||H.getNumHumans()>0) {
                PopulatedHousecoords.add(new IntegerPair(H.getX(), H.getY()));
            }
        }



        System.out.println("looking at: "+x+" "+y);
        int w;
        int z;
        IntegerPair temp;
        if(house.getNumHumans()>0||house.isBrownAlien()||house.isPurpleAlien()){
            w=x;
            z=y-1; //left
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(new IntegerPair(house.getX(),house.getY()));
                    }
                }
            }
            w=x-1;
            z=y;//up
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(new IntegerPair(house.getX(),house.getY()));
                    }
                }
            }
            w=x;
            z=y+1; //right
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(new IntegerPair(house.getX(),house.getY()));
                    }
                }
            }
            w=x+1;
            z=y; //down
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(new IntegerPair(house.getX(),house.getY()));
                    }
                }
            }

        }
        System.out.println("end of infected");

    }

//
//    public void findPaths(int r, int c, ArrayList<IntegerPair> visited, int [][] valid, Tile[][] tiles) {
//
//        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || valid[r][c] == -1) {
//            return;
//        }
//        visited.add(new IntegerPair(r, c));
//        System.out.println(r + " " + c);
//
//        if (valid[r][c-1] == 1 && (tiles[r][c-1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c-1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c-1].getComponent().isBrownAlien()||tiles[r][c-1].getComponent().isPurpleAlien()||tiles[r][c-1].getComponent().getAbility()>0)) {
//            System.out.println( "should infect "+r + " " +(c-1));
//            findPaths(r, c - 1, visited,valid,tiles);
//        }
//
//        if (valid[r-1][c] == 1 && (tiles[r-1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r-1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r-1][c].getComponent().isBrownAlien()||tiles[r-1][c].getComponent().isPurpleAlien()||tiles[r-1][c].getComponent().getAbility()>0)){
//            System.out.println( "should infect "+(r-1) + " " +(c));
//            findPaths(r - 1, c , visited,valid,tiles);
//        }
//
//        if (valid[r][c+1] == 1 && (tiles[r][c+1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c+1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c+1].getComponent().isBrownAlien()||tiles[r][c+1].getComponent().isPurpleAlien()||tiles[r][c+1].getComponent().getAbility()>0)){
//            System.out.println( "should infect "+r + " " +(c+1));
//            findPaths(r, c + 1, visited,valid,tiles);
//        }
//
//        if (valid[r+1][c] == 1 && (tiles[r+1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r+1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r+1][c].getComponent().isBrownAlien()||tiles[r+1][c].getComponent().isPurpleAlien()||tiles[r+1][c].getComponent().getAbility()>0)){
//            System.out.println( "should infect "+(r+1) + " " +(c));
//            findPaths(r +1, c , visited,valid,tiles);
//        }
//
//   }


    public Epidemic(){}






}




