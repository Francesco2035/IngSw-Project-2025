package org.example.galaxy_trucker.Controller.Messages;

public class VoidEvent implements Event {

    int x;
    int y;
    public VoidEvent(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String message() {
        return "VirtualView settata";
    }
}
