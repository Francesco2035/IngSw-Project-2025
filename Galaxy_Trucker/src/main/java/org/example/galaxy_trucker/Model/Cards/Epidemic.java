package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.modularHousingUnit;

import java.util.ArrayList;

public class Epidemic extends Card {
    private ArrayList<IntegerPair> infected;


    public Epidemic(int level, int time, GameBoard board) {
        super(level, time, board);
        this.infected = new ArrayList<>();

    }

    public void CardEffect(){


        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<IntegerPair> HousingCoords;
        ArrayList<IntegerPair> InfectedUnits;
        Tile[][] playerTiles;
        for(int i=0;i<Len;i++){
            this.infected.clear();
            CurrentPlanche=PlayerList.get(i).getMyPlance();
            int[][] valid = CurrentPlanche.getValidPlayerBoard();
            playerTiles = CurrentPlanche.getPlayerBoard();
            HousingCoords=new ArrayList<>();
            if(CurrentPlanche.getClassifiedTiles().containsKey(modularHousingUnit.class)) {
                HousingCoords = CurrentPlanche.getClassifiedTiles().get(modularHousingUnit.class);
            }
            if(CurrentPlanche.getValidPlayerBoard()[6][6]==1) {
                HousingCoords.add(new IntegerPair(6,6));
            }

            ArrayList<IntegerPair> visited = new ArrayList<>();
            System.out.println("starting infected");
            for (int j = 0; j < HousingCoords.size(); j++) {
                findInfected(HousingCoords.get(j),CurrentPlanche);
                //findPaths(HousingCoords.get(j).getFirst(), HousingCoords.get(j).getSecond(), visited, valid ,playerTiles);

            }
            System.out.println("size of infected is: "+infected.size());
            for(int k=0;k<infected.size();k++){
                System.out.println("killing in: "+infected.get(k).getFirst()+" "+infected.get(k).getSecond());

                playerTiles[infected.get(k).getFirst()][infected.get(k).getSecond()].getComponent().setAbility(1,false,false);
            }


        }
    }

    public void findInfected(IntegerPair coords,PlayerBoard playerBoard) {
        int x = coords.getFirst();
        int y = coords.getSecond();
        Tile [][] tiles=playerBoard.getPlayerBoard();
        int[][]valid=playerBoard.getValidPlayerBoard();
        System.out.println("looking at: "+x+" "+y);
        int w;
        int z;
        IntegerPair temp;
        if(tiles[x][y].getComponent().getAbility()>0||tiles[x][y].getComponent().isBrownAlien()||tiles[x][y].getComponent().isPurpleAlien()){
            w=x;
            z=y-1; //left
            temp=new IntegerPair(w,z);

//            if(CurrentPlanche.getClassifiedTiles().containsKey(modularHousingUnit.class)) {
//                HousingCoords = CurrentPlanche.getClassifiedTiles().get(modularHousingUnit.class);
//            }
//            if(CurrentPlanche.getValidPlayerBoard()[6][6]==1) {
//                HousingCoords.add(new IntegerPair(6,6));
//            }

            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if ((tiles[w][z].getComponent().getType().equals("modularHousingUnit")||tiles[w][z].getComponent().getType().equals("MainCockpit")) && (tiles[w][z].getComponent().getAbility() > 0 || tiles[w][z].getComponent().isBrownAlien() || tiles[w][z].getComponent().isPurpleAlien())) {
                    if (!this.infected.contains(coords)) {
                        System.out.println("added from left");
                        this.infected.add(coords);
                    }
                }
            }
            w=x-1;
            z=y;//up
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if ((tiles[w][z].getComponent().getType().equals("modularHousingUnit")||tiles[w][z].getComponent().getType().equals("MainCockpit")) && (tiles[w][z].getComponent().getAbility() > 0 || tiles[w][z].getComponent().isBrownAlien() || tiles[w][z].getComponent().isPurpleAlien())) {
                    if (!this.infected.contains(coords)) {
                        System.out.println("added from up");
                        this.infected.add(coords);
                    }
                }
            }
            w=x;
            z=y+1; //right
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if ((tiles[w][z].getComponent().getType().equals("modularHousingUnit")||tiles[w][z].getComponent().getType().equals("MainCockpit")) && (tiles[w][z].getComponent().getAbility() > 0 || tiles[w][z].getComponent().isBrownAlien() || tiles[w][z].getComponent().isPurpleAlien())){
                    System.out.println(w+" "+z+" is a house");
                    if (!this.infected.contains(coords)) {
                        System.out.println("added from right");
                        this.infected.add(coords);
                    }
                }
            }
            w=x+1;
            z=y; //down
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if ((tiles[w][z].getComponent().getType().equals("modularHousingUnit")||tiles[w][z].getComponent().getType().equals("MainCockpit")) && (tiles[w][z].getComponent().getAbility() > 0 || tiles[w][z].getComponent().isBrownAlien() || tiles[w][z].getComponent().isPurpleAlien())) {
                    if (!this.infected.contains(coords)) {
                        System.out.println("added from down");
                        this.infected.add(coords);
                    }
                }
            }

        }
        System.out.println("end of infected");

    }


    public void findPaths(int r, int c, ArrayList<IntegerPair> visited, int [][] valid, Tile[][] tiles) {

        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || valid[r][c] == -1) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);

        if (valid[r][c-1] == 1 && (tiles[r][c-1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c-1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c-1].getComponent().isBrownAlien()||tiles[r][c-1].getComponent().isPurpleAlien()||tiles[r][c-1].getComponent().getAbility()>0)) {
            System.out.println( "should infect "+r + " " +(c-1));
            findPaths(r, c - 1, visited,valid,tiles);
        }

        if (valid[r-1][c] == 1 && (tiles[r-1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r-1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r-1][c].getComponent().isBrownAlien()||tiles[r-1][c].getComponent().isPurpleAlien()||tiles[r-1][c].getComponent().getAbility()>0)){
            System.out.println( "should infect "+(r-1) + " " +(c));
            findPaths(r - 1, c , visited,valid,tiles);
        }

        if (valid[r][c+1] == 1 && (tiles[r][c+1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c+1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c+1].getComponent().isBrownAlien()||tiles[r][c+1].getComponent().isPurpleAlien()||tiles[r][c+1].getComponent().getAbility()>0)){
            System.out.println( "should infect "+r + " " +(c+1));
            findPaths(r, c + 1, visited,valid,tiles);
        }

        if (valid[r+1][c] == 1 && (tiles[r+1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r+1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r+1][c].getComponent().isBrownAlien()||tiles[r+1][c].getComponent().isPurpleAlien()||tiles[r+1][c].getComponent().getAbility()>0)){
            System.out.println( "should infect "+(r+1) + " " +(c));
            findPaths(r +1, c , visited,valid,tiles);
        }

    }


    public Epidemic(){}






}




