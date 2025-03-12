package org.example.galaxy_trucker;

// list of the cargo contained in a planet and the bool signaling if a player chose it already
public class Planet {
    private boolean Occupied;
    private int NumofRed;
    private int NumofYellow;
    private int NumofGreen;
    private int NumofBlue;
    Planet(int red, int yellow, int green, int blue ) {
        this.NumofRed = red;
        this.NumofYellow = yellow;
        this.NumofGreen = green;
        this.NumofBlue= blue;
        this.Occupied=false;
    }

    int getNumofRed() {
        return NumofRed;
    }
    int getNumofYellow() {
        return NumofYellow;
    }
    int getNumofGreen() {
        return NumofGreen;
    }
    int getNumofBlue() {
        return NumofBlue;
    }
    boolean isOccupied() {
        return Occupied;
    }
}
