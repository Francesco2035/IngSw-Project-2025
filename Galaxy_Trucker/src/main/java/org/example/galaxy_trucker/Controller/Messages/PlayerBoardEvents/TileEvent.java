package org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents;

import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

public class TileEvent implements Event {

    int id;
    int humans;
    int x;
    int y;
    boolean purpleAlien;
    boolean brownAlien;
    int batteries;
    ArrayList<Goods> cargo;
    int rotation;
    ArrayList<Connectors> connectors;


    public TileEvent(int id,int x,int y, ArrayList<Goods> cargo, int humans,boolean purpleAlien, boolean brownAlien, int batteries, int rotation, ArrayList<Connectors> connectors) {
        this.x = x;
        this.y = y;
        this.cargo = cargo;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.batteries = batteries;
        this.id = id;
        this.rotation = rotation;
        this.humans = humans;
        this.connectors = connectors;
    }
    @Override
    public String message() {
        return "";
    }

    public int getHumans() {
        return humans;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    public boolean isBrownAlien() {
        return brownAlien;
    }

    public int getBatteries() {
        return batteries;
    }

    public ArrayList<Goods> getCargo() {
        return cargo;
    }

    public int getId() {
        return id;
    }
    public int getRotation() {
        return rotation;
    }

    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

}
