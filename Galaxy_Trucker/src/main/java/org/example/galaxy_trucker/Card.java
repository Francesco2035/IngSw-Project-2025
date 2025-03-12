package org.example.galaxy_trucker;

public class Card {
    private int Level;
    private int Time;
    private GameBoard Board;
    // IMPORTANTE ESERCITATORE DICE DI ATTIVARE EFFETTI DELLE CARTE A POSTERIORI DELLE SCELTE DEI PLAYER
    // NON SO SE HA SENSO MA LUI DICE DI FARE COSI, QUINDI I METODI DI CARD EFFECT DOVREBBERO ESSERE
    //INTERAMENTE DEFINITI SENZA CHIEDERE ALTRI INPUT AI PLAYER, SEMPLICEMENTE MODIFICHERANNO IL MODELLO

        // puoi invece che salvarti come attributo la Gameboard  posso passaerla per risparmiare in memoria ma non è un big problema :)
    public Card(int level, int time, GameBoard board) {
        this.Level = level;
        this.Time = time;
        this.Board = board;
    }
    public GameBoard getBoard() {
        return Board;
    }

    public void CardEffect(){

    }
}
