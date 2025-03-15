package org.example.galaxy_trucker;

import java.util.ArrayList;

public class Pirates extends Card{
    private int requirement;
    private int reward;
    private ArrayList<IntegerPair> Punishment;
            // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    Pirates(int level, int time, GameBoard board, int Reward, int Requirement, ArrayList<IntegerPair> Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
    }
    @Override
    public void CardEffect(){
        int Order=0;
        int AttackNumber=0;
        boolean Flag=true;
        boolean Attacked=false;
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        PlayerPlance CurrentPlanche;
        int Len= PlayerList.size();
        int[][] ValidPlanche;
        int Movement;
        int Lines [] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int Line; // gli attacchi son fissi per tutti i player quindi tiro già la sequenza di dadi
        for(int i=0;i<Punishment.size();i++){
            Lines[i]=PlayerList.get(0).RollDice();
        }

        int PlayerPower;


        while(Len>Order && Flag){ // ciclo attacchi player a player
            PlayerPower=PlayerList.get(Order).getPower();
            if(PlayerPower<requirement){ // se il player è meno potente dei pirati lo attacco
                //metodo simile a metorites cambia solo che lo fa solo su un player e poi la gestione degli attacchi è vagamente diversa

                CurrentPlanche=PlayerList.get(Order).getMyPlance();
                ValidPlanche=CurrentPlanche.getValidPlance(); //prendo la planche da controllare
                AttackNumber=0;

                while(Punishment.size()>AttackNumber) { // faccio tutti gli atttacchi
                    Line=Lines[AttackNumber]; // prendo la linea da attaccà

                    if (Punishment.get(AttackNumber).getFirst() == 0) { //sinistra
                        Movement = 0;

                        while (Movement < 10 && Attacked == false) {
                            if (ValidPlanche[Line][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                                //Meteorites.Hit(MeteoritesLine,MeteoritesMovement)
                                Attacked = true;
                            }

                            Movement++;
                        }
                    }
                    if (Punishment.get(AttackNumber).getFirst() == 1) {//sopra
                        Movement = 0;
                        while (Movement < 10 && Attacked == false) {
                            if (ValidPlanche[Movement][Line] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                                //Meteorites.Hit(MeteoritesMovement,MeteoritesLine)
                                Attacked  = true;
                            }

                            Movement++;
                        }
                    }
                    if (Punishment.get(AttackNumber).getFirst() == 2) {// destra
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
            } //fine caso potenza minore

            else if (PlayerPower > requirement) {
                Flag = false;
                //if(PLayerlist.get(Order).yes()){   //chiedo se vuole prende le ricompense
                //PlayerList.get(Order).IncreaseCredits(Reward);
                //PlayerList.get(Order).movePlayer(-this.Time);
                //}
            }//fine caso vittoria

            //mi basta non fare nulla in caso di pareggio

            Order++;
        }
    }

}
