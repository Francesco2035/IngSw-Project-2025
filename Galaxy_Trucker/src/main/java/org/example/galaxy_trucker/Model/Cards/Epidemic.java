package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Controller.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.Killing;
import org.example.galaxy_trucker.Model.PlayerStates.ReadCardState;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class Epidemic extends Card {
    private ArrayList<HousingUnit> infected;

    private Player currentPlayer;
    private ArrayList<Player> losers;


    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Epidemic"));
        }
    }


    public Epidemic(int level, int time, GameBoard board) {
        super(level, time, board);

    }

    public void CardEffect() throws InterruptedException {
        losers = new ArrayList<>();
        if(infected==null) {
            infected = new ArrayList<>();
        }

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new ReadCardState());
        }
        Thread.sleep(5000);
        PlayerBoard CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        ArrayList<HousingUnit> HousingCoords= new ArrayList<>();
        infected = new ArrayList<>();

        Tile[][] tiles;
        for(int i=0;i<Len;i++){
            this.infected.clear();
            this.currentPlayer = PlayerList.get(i);
            this.currentPlayer.setState(new Waiting());
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
                System.out.println(infected.get(k).getX()+" "+infected.get(k).getY());
            }
            for(int k=0;k<infected.size();k++){

                System.out.println("killing in:"+infected.get(k).getX()+" "+infected.get(k).getY());
                CurrentPlanche.performAction(tiles[this.infected.get(k).getX()][this.infected.get(k).getY()].getComponent(),new KillCrewAction(CurrentPlanche), new Killing());

            }


        }
        this.finishCard();
    }

    @Override
    public void finishCard() {
        checkLosers();
        this.setFinished(true);
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
                        this.infected.add(house);
                    }
                }
            }
            w=x-1;
            z=y;//up
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }
            w=x;
            z=y+1; //right
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }
            w=x+1;
            z=y; //down
            if((0<=w&&w<10)&&(0<=z&&z<10)&&valid[w][z]==1) {
                if (PopulatedHousecoords.contains(new IntegerPair(w,z)) ) {
                    if (!this.infected.contains(house)) {
                        System.out.println("added from left");
                        this.infected.add(house);
                    }
                }
            }

        }
        System.out.println("end of infected");

    }



    public Epidemic(){}






}




