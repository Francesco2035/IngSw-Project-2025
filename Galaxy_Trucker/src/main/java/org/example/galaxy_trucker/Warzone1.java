package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Warzone1 extends Card{
    private int Punishment1;
    private int Punishment2;
    private ArrayList<IntegerPair> Punishment3;
    // richiede umani, toglie tempo
    //richiede velocità, toglie umani
    //richete cannoni, ti spara
    public Warzone1(int level, int time, GameBoard board,int Punishment1, int Punishment2, ArrayList<IntegerPair> Punishment3) {
        super(level, time, board);
        this.Punishment1 = Punishment1;
        this.Punishment2 = Punishment2;
        this.Punishment3 = Punishment3;
    }

    @Override
    public void CardEffect() {
        int Order=0;
        int AttackNumber=0;
        boolean Attacked=false;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerPlance CurrentPlanche;
        int Len= PlayerList.size(); // quanti player ho
        int[][] ValidPlanche;
        int Movement;
        int Lines [] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int Line; // gli attacchi son fissi per tutti i player quindi tiro già la sequenza di dadi
        for(int i=0;i<Punishment3.size();i++){
            Lines[i]=PlayerList.get(0).RollDice();
        }

        int CurrentValue;
        int Minimum=1000000; // potrei semplicemente settare minimum = primo player
        // e poi fare solo fino a 3 controlli  ma mi va di farla sporca
        Player PlayerMin=PlayerList.get(0);
        //effetto1
        for(Order=0; Order<Len; Order++){
            CurrentValue=PlayerList.get(Order).getHumans();
            if(CurrentValue<Minimum){
                Minimum=CurrentValue;
                PlayerMin= PlayerList.get(Order);
            }

        }
        PlayerMin.movePlayer(-Punishment1);

        //effetto2
        Minimum=100000;
        for(Order=0; Order<Len; Order++){
            CurrentValue=PlayerList.get(Order).getMovement();
            if(CurrentValue<Minimum){
                Minimum=CurrentValue;
                PlayerMin= PlayerList.get(Order);
            }
            PlayerMin.killHumans(Punishment2);

        }
        //effetto3
        Minimum=100000;
        for(Order=0; Order<Len; Order++){
            CurrentValue=PlayerList.get(Order).getPower();
            if(CurrentValue<Minimum){
                Minimum=CurrentValue;
                PlayerMin= PlayerList.get(Order);
            }
            //metodo simile a metorites cambia solo che lo fa solo su un player e poi la gestione degli attacchi è vagamente diversa

            CurrentPlanche=PlayerList.get(Order).getMyPlance();
            ValidPlanche=CurrentPlanche.getValidPlance(); //prendo la planche da controllare
            AttackNumber=0;

            while(Punishment3.size()>AttackNumber) { // faccio tutti gli atttacchi
                Line=Lines[AttackNumber]; // prendo la linea da attaccà

                if (Punishment3.get(AttackNumber).getFirst() == 0) { //sinistra
                    Movement = 0;

                    while (Movement < 10 && Attacked == false) {
                        if (ValidPlanche[Line][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
                            Attacked = true;
                        }

                        Movement++;
                    }
                }
                if (Punishment3.get(AttackNumber).getFirst() == 1) {//sopra
                    Movement = 0;
                    while (Movement < 10 && Attacked == false) {
                        if (ValidPlanche[Movement][Line] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
                            Attacked  = true;
                        }

                        Movement++;
                    }
                }
                if (Punishment3.get(AttackNumber).getFirst() == 2) {// destra
                    Movement = 9;
                    while (Movement >= 0 && Attacked == false) {
                        if (ValidPlanche[Line][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
                            Attacked = true;
                        }
                    }
                    Movement--;
                } else { //sotto
                    Movement = 9;
                    while (Movement >= 0 && Attacked == false) {
                        if (ValidPlanche[Movement][Line] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                            //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
                            Attacked = true;
                        }

                        Movement--;
                    }

                }
                AttackNumber++;
            }

        }
    }
}
