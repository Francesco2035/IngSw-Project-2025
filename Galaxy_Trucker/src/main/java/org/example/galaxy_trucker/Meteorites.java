package org.example.galaxy_trucker;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

//RINOMINA AD ATTACK E CONSIDERA ANCHE I PROIETTILI A STO PUNTO

// direzioni int sinistra 0 sopra 1...
public class Meteorites extends Card {
    @JsonProperty("attacks")
    private ArrayList<Integer> Attacks; // prima è la direzione, secondo il tipo di attacco

    public Meteorites(int level, int time, GameBoard board, ArrayList<Integer> Attacks) {
        super(level, 0, board);
        this.Attacks = Attacks;



    }

//    @Override
//    public void CardEffect() {
//        int MeteoritesOrder = 0;
//        int MeteoritesAttackNumber = 0;
//        int MeteoritesLine;
//        int MeteoritesMovement;
//        boolean MeteoritesFlag = false;
//        GameBoard MeteoritesBoard = super.getBoard();
//        int[][] MeteoritesValidPlanche;
//        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();
//        PlayerPlance MeteoritesCurrentPlanche;
//
//        while (Attacks.size() > MeteoritesAttackNumber) { //scorre i meteoriti e attacca i player 1 a 1
//            MeteoritesLine = MeteoritesPlayerList.get(0).RollDice(); // tira numero
//            while (MeteoritesPlayerList.size() > MeteoritesOrder) { // Scorre i player
//                MeteoritesCurrentPlanche = MeteoritesPlayerList.get(MeteoritesOrder).getMyPlance(); //prendo plancia
//                MeteoritesValidPlanche = MeteoritesCurrentPlanche.getValidPlance();//prende matrice validita
//                if (Attacks.get(MeteoritesAttackNumber).getFirst() == 0) { //sinistra
//                    MeteoritesMovement = 0;
//                    while (MeteoritesMovement < 10 && MeteoritesFlag == false) {
//                        if (MeteoritesValidPlanche[MeteoritesLine][MeteoritesMovement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
//                            MeteoritesFlag = true;
//                        }
//
//                        MeteoritesOrder++;
//                    }
//                }
//                if (Attacks.get(MeteoritesAttackNumber).getFirst() == 1) {//sopra
//                    MeteoritesMovement = 0;
//                    while (MeteoritesMovement < 10 && MeteoritesFlag == false) {
//                        if (MeteoritesValidPlanche[MeteoritesMovement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
//                            MeteoritesFlag = true;
//                        }
//
//                        MeteoritesOrder++;
//                    }
//                }
//                if (Attacks.get(MeteoritesAttackNumber).getFirst() == 2) {// destra
//                    MeteoritesMovement = 9;
//                    while (MeteoritesMovement >= 0 && MeteoritesFlag == false) {
//                        if (MeteoritesValidPlanche[MeteoritesLine][MeteoritesMovement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
//                            MeteoritesFlag = true;
//                        }
//                    }
//                    MeteoritesOrder--;
//                } else { //sotto
//                    MeteoritesMovement = 9;
//                    while (MeteoritesMovement >= 0 && MeteoritesFlag == false) {
//                        if (MeteoritesValidPlanche[MeteoritesMovement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
//                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
//                            MeteoritesFlag = true;
//                        }
//
//                        MeteoritesOrder--;
//                    }
//
//                }
//
//                MeteoritesOrder++;
//            }
//            MeteoritesAttackNumber++;
//
//        }
//    }
//


    //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return Attacks;}
    public void setAttacks(ArrayList<Integer> attacks) {Attacks = attacks;}
}