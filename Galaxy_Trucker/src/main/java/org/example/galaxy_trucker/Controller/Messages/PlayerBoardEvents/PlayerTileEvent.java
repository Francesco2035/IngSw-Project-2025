package org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

public class PlayerTileEvent extends TileEvent implements Event  {

    private String playerName;
    private int id;
    private int humans;
    private int x;
    private int y;
    private boolean purpleAlien;
    private boolean brownAlien;
    private int batteries;
    private ArrayList<Goods> cargo;
    private int rotation;
    private ArrayList<Connectors> connectors;

    @JsonCreator
    public PlayerTileEvent(
            @JsonProperty("player") String playerName,
            @JsonProperty("id") int id,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("cargo") ArrayList<Goods> cargo,
            @JsonProperty("humans") int humans,
            @JsonProperty("purpleAlien") boolean purpleAlien,
            @JsonProperty("brownAlien") boolean brownAlien,
            @JsonProperty("batteries") int batteries,
            @JsonProperty("rotation") int rotation,
            @JsonProperty("connectors") ArrayList<Connectors> connectors
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.cargo = cargo;
        this.humans = humans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.batteries = batteries;
        this.rotation = rotation;
        this.connectors = connectors;
        this.playerName = playerName;
    }

    @Override
    public String message() {
        return "";
    }

    public int getId() {
        return id;
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

    public int getRotation() {
        return rotation;
    }

    public ArrayList<Connectors> getConnectors() {
        return connectors;
    }

    public String getPlayerName(){
        return playerName;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public PlayerTileEvent() {}
}
