package org.example.galaxy_trucker;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

//GESTISCI GLI ATTACCHI COME UN ARRAY LIST DI INTEGER E NON INTEGER PAIR

// direzioni int sinistra 0 sopra 1...
//0 piccolo 1 grande
public class Meteorites extends Card {
    private ArrayList<IntegerPair> Attacks;
    @JsonProperty ("attacks")// prima è la direzione, secondo il tipo di attacco
    private ArrayList<Integer> attacks;

    public Meteorites(int level, int time, GameBoard board,ArrayList<Integer> attacks) {
        super(level, 0, board);

        this.attacks = attacks;



    }
//per adesso hit non fa nulla ma o semplicemente chiamerà posizione + direzione + tipo per sparare
    //o gestisce la cosa e poi nel caso di hit chiama solo la posizione coplita se accade ed è indifesa
    @Override
    public void CardEffect() {
        int MeteoritesOrder = 0;
        int MeteoritesAttackNumber = 0;
        int MeteoritesLine;
        int Movement;
        boolean MeteoritesFlag = false;
        GameBoard MeteoritesBoard = super.getBoard();
        int[][] MeteoritesValidPlanche;
        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();
        PlayerBoard CurrentPlanche;

        while (Attacks.size() > MeteoritesAttackNumber) { //scorre i meteoriti e attacca i player 1 a 1
            MeteoritesLine = MeteoritesPlayerList.get(0).RollDice(); // tira numero
            while (MeteoritesPlayerList.size() > MeteoritesOrder) { // Scorre i player
                CurrentPlanche=MeteoritesPlayerList.get(MeteoritesOrder).getMyPlance(); //prendo plancia
                MeteoritesValidPlanche=CurrentPlanche.getValidPlayerBoard();//prende matrice validita
                if (attacks.get(MeteoritesAttackNumber)==0) { //sinistra
                    Movement=0;
                    while(Movement<10 && MeteoritesFlag == false){
                        if(MeteoritesValidPlanche[MeteoritesLine][Movement]>0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                            if(attacks.get(MeteoritesAttackNumber+1)==0) {
                                if(tiles[Movement][MeteoritesLine].getConnectors().get(0)!=Connector.NONE) {//manca uso scudi perché non va
                                    CurrentPlanche.destroy(Movement, MeteoritesLine);
                                }

                            }
                            else if (attacks.get(MeteoritesAttackNumber+1)==1) {


                                ArrayList<IntegerPair> Cannons = CurrentPlanche.getPlasmaDrills();

                                int i = 0;
                                int x;
                                int y;
                                boolean exploded = false;
                                while (i < Cannons.size() && !exploded) {
                                    x = Cannons.get(i).getFirst();
                                    y = Cannons.get(i).getSecond();
                                    if(tiles[x][y].getConnectors().get(0)==Connector.CANNON && y==MeteoritesLine) {// anche qui manca scudo
                                        //chiamo exlode meteorite
                                        exploded = true;
                                    }
                                    else {CurrentPlanche.destroy(x, y);}

                                    i++;
                                }
                            }
                            MeteoritesFlag = true;
                        }

                        Movement++;
                    }
                }
                if (Attacks.get(MeteoritesAttackNumber).getFirst()==1) {//sopra
                    Movement=0;
                    while(Movement<10 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                            if(attacks.get(MeteoritesAttackNumber+1)==0) {
                                if(tiles[Movement][MeteoritesLine].getConnectors().get(1)!=Connector.NONE) {//manca uso scudi perché non va
                                    CurrentPlanche.destroy(Movement, MeteoritesLine);
                                }

                            }
                            else if (attacks.get(MeteoritesAttackNumber+1)==1) {


                                ArrayList<IntegerPair> Cannons = CurrentPlanche.getPlasmaDrills();

                                int i = 0;
                                int x;
                                int y;
                                boolean exploded = false;
                                while (i < Cannons.size() && !exploded) {
                                    x = Cannons.get(i).getFirst();
                                    y = Cannons.get(i).getSecond();
                                    if(tiles[x][y].getConnectors().get(1)==Connector.CANNON && y==MeteoritesLine) {
                                        //chiamo exlode meteorite
                                        exploded = true;
                                    }
                                    else {CurrentPlanche.destroy(x, y);}

                                    i++;
                                }
                            }
                            MeteoritesFlag = true;
                        }

                        Movement++;
                    }
                }
                if (Attacks.get(MeteoritesAttackNumber).getFirst()==2) {// destra
                    Movement=9;
                    while(Movement>=0 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                            if(attacks.get(MeteoritesAttackNumber+1)==0) {
                                if(tiles[Movement][MeteoritesLine].getConnectors().get(2)!=Connector.NONE) {//manca uso scudi perché non va
                                    CurrentPlanche.destroy(Movement, MeteoritesLine);
                                }

                            }
                            else if (attacks.get(MeteoritesAttackNumber+1)==1) {


                                ArrayList<IntegerPair> Cannons = CurrentPlanche.getPlasmaDrills();

                                int i = 0;
                                int x;
                                int y;
                                boolean exploded = false;
                                while (i < Cannons.size() && !exploded) {
                                    x = Cannons.get(i).getFirst();
                                    y = Cannons.get(i).getSecond();
                                    if(tiles[x][y].getConnectors().get(2)==Connector.CANNON && y==MeteoritesLine) {
                                        //chiamo exlode meteorite
                                        exploded = true;
                                    }
                                    else {CurrentPlanche.destroy(x, y);}

                                    i++;
                                }
                            }
                            MeteoritesFlag = true;
                        }
                        Movement--;
                    }

                } else { //sotto
                    Movement=9;
                    while(Movement>=0 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                            if(attacks.get(MeteoritesAttackNumber+1)==0) {
                                if(tiles[Movement][MeteoritesLine].getConnectors().get(3)!=Connector.NONE) {//manca uso scudi perché non va
                                    CurrentPlanche.destroy(Movement, MeteoritesLine);
                                }

                            }
                            else if (attacks.get(MeteoritesAttackNumber+1)==1) {


                                ArrayList<IntegerPair> Cannons = CurrentPlanche.getPlasmaDrills();

                                int i = 0;
                                int x;
                                int y;
                                boolean exploded = false;
                                while (i < Cannons.size() && !exploded) {
                                    x = Cannons.get(i).getFirst();
                                    y = Cannons.get(i).getSecond();
                                    if(tiles[x][y].getConnectors().get(3)==Connector.CANNON && y==MeteoritesLine) {
                                        //chiamo explode meteorite
                                        exploded = true;
                                    }
                                    else {CurrentPlanche.destroy(x, y);}

                                    i++;
                                }
                            }
                            MeteoritesFlag = true;
                        }

                        Movement--;
                    }

                }

                MeteoritesOrder++;
            }
            MeteoritesAttackNumber+=2;

        }
    }


    //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return attacks;}
    public void setAttacks(ArrayList<Integer> attacks) {attacks = attacks;}
}