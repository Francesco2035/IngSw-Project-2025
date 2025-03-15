package org.example.galaxy_trucker;
//import javafx.util.Pair;
import java.util.ArrayList;



// direzioni int sinistra 0 sopra 1...
//0 piccolo 1 grande
public class Meteorites extends Card {
    private ArrayList<IntegerPair> Attacks; // prima è la direzione, secondo il tipo di attacco

    public Meteorites(int level, int time, GameBoard board, ArrayList<IntegerPair> Attacks) {
        super(level, 0, board);
        this.Attacks = Attacks;


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
        PlayerPlance MeteoritesCurrentPlanche;

        while (Attacks.size() > MeteoritesAttackNumber) { //scorre i meteoriti e attacca i player 1 a 1
            MeteoritesLine = MeteoritesPlayerList.get(0).RollDice(); // tira numero
            while (MeteoritesPlayerList.size() > MeteoritesOrder) { // Scorre i player
                MeteoritesCurrentPlanche=MeteoritesPlayerList.get(MeteoritesOrder).getMyPlance(); //prendo plancia
                MeteoritesValidPlanche=MeteoritesCurrentPlanche.getValidPlance();//prende matrice validita
                if (Attacks.get(MeteoritesAttackNumber).getFirst()==0) { //sinistra
                    Movement=0;
                    while(Movement<10 && MeteoritesFlag == false){
                        if(MeteoritesValidPlanche[MeteoritesLine][Movement]>0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
                            MeteoritesFlag = true;
                        }

                        Movement++;
                    }
                }
                if (Attacks.get(MeteoritesAttackNumber).getFirst()==1) {//sopra
                    Movement=0;
                    while(Movement<10 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
                            MeteoritesFlag = true;
                        }

                        Movement++;
                    }
                }
                if (Attacks.get(MeteoritesAttackNumber).getFirst()==2) {// destra
                    Movement=9;
                    while(Movement>=0 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
                            MeteoritesFlag = true;
                        }
                        Movement--;
                    }

                } else { //sotto
                    Movement=9;
                    while(Movement>=0 && MeteoritesFlag == false) {
                        if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
                            MeteoritesFlag = true;
                        }

                        Movement--;
                    }

                }

                MeteoritesOrder++;
            }
            MeteoritesAttackNumber++;

        }
    }
}