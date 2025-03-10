package org.example.galaxy_trucker;

public class Card {
    private int Level;
    private int Time;
    private GameBoard Board;

    public Card(int level, int time, GameBoard board) {
        this.Level = level;
        this.Time = time;
        this.Board = board;
    }
    public GameBoard getBoard() {
        return Board;
    }
}
