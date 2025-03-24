package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class Epidemic extends Card {


    public Epidemic(int level, int time, GameBoard board) {
        super(level, time, board);
    }

    public void CardEffect(){


        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<IntegerPair> HousingUnits;
        ArrayList<IntegerPair> InfectedUnits;
        Tile[][] playerTiles;
        for(int i=0;i<Len;i++){
            CurrentPlanche=PlayerList.get(i).getMyPlance();
            int[][] valid = CurrentPlanche.getValidPlayerBoard();
            playerTiles = CurrentPlanche.getPlayerBoard();
            HousingUnits=CurrentPlanche.gethousingUnits();
            ArrayList<IntegerPair> visited = new ArrayList<>();
            for (int j = 0; j < HousingUnits.size(); j++) {
                findPaths(HousingUnits.get(j).getFirst(), HousingUnits.get(j).getSecond(), visited, valid ,playerTiles);
            }
            for(IntegerPair housingunits :visited){
                playerTiles[housingunits.getFirst()][housingunits.getSecond()].getComponent().setAbility(1,false,false);
            }


        }
    }

    public void findPaths(int r, int c, ArrayList<IntegerPair> visited, int [][] valid, Tile[][] tiles) {

        if (visited.contains(new IntegerPair(r, c))||r < 0 || c < 0 || r > 9 || c > 9 || valid[r][c] == -1) {
            return;
        }
        visited.add(new IntegerPair(r, c));
        System.out.println(r + " " + c);

        if (valid[r][c-1] == 1 && (tiles[r][c-1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c-1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c-1].getComponent().isBrownAlien()||tiles[r][c-1].getComponent().isPurpleAlien()||tiles[r][c-1].getComponent().getAbility()>0)) {
            findPaths(r, c - 1, visited,valid,tiles);
        }

        if (valid[r-1][c] == 1 && (tiles[r-1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r-1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r-1][c].getComponent().isBrownAlien()||tiles[r-1][c].getComponent().isPurpleAlien()||tiles[r-1][c].getComponent().getAbility()>0)){
            findPaths(r - 1, c , visited,valid,tiles);
        }

        if (valid[r][c+1] == 1 && (tiles[r][c+1].getComponent().getType().equals("modularHousingUnit")||tiles[r][c+1].getComponent().getType().equals("MainCockpit"))&&(tiles[r][c+1].getComponent().isBrownAlien()||tiles[r][c+1].getComponent().isPurpleAlien()||tiles[r][c+1].getComponent().getAbility()>0)){
            findPaths(r, c + 1, visited,valid,tiles);
        }

        if (valid[r+1][c] == 1 && (tiles[r+1][c].getComponent().getType().equals("modularHousingUnit")||tiles[r+1][c].getComponent().getType().equals("MainCockpit"))&&(tiles[r+1][c].getComponent().isBrownAlien()||tiles[r+1][c].getComponent().isPurpleAlien()||tiles[r+1][c].getComponent().getAbility()>0)){
            findPaths(r +1, c , visited,valid,tiles);
        }

    }

    public Epidemic(){}






}




