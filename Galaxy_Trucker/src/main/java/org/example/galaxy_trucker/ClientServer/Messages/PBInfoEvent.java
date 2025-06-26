package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PBInfoEvent implements Event {

    private int credits;
    private int totValue;
    private int energy = 0;
    private int exposedConnectors;
    private int damage;
    private int numHumans = 0;
    private int enginePower = 0;
    private double plasmaDrillsPower = 0;
    private boolean purpleAlien;
    private boolean brownAlien;
    private int[] shield;

    public PBInfoEvent() {

    }

    @JsonCreator
    public PBInfoEvent(@JsonProperty("damage") int damage, @JsonProperty("credits") int credits, @JsonProperty("exposedConnectors") int exposedConnectors,
                       @JsonProperty("shield") int[]shield, @JsonProperty("numHumans") int numHumans, @JsonProperty("enginePower") int enginePower,
                       @JsonProperty("plasmaDrillPower") double plasmaDrillsPower, @JsonProperty("energy") int energy,
                       @JsonProperty("purpleAlien") boolean purpleAlien, @JsonProperty("brownAlien") boolean brownAlien,
                       @JsonProperty("totValue") int totValue) {

        this.totValue = totValue;
        this.damage = damage;
        this.credits = credits;
        this.exposedConnectors = exposedConnectors;
        this.shield = shield;
        this.numHumans = numHumans;
        this.enginePower = enginePower;
        this.brownAlien = brownAlien;
        this.purpleAlien = purpleAlien;
        this.energy = energy;
        this.plasmaDrillsPower = plasmaDrillsPower;



    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Override
    public String message() {
        return "";
    }

    public int getCredits() {
        return credits;
    }

    public int getTotValue() {
        return totValue;
    }

    public int getEnergy() {
        return energy;
    }

    public int getExposedConnectors() {
        return exposedConnectors;
    }

    public int getDamage() {
        return damage;
    }

    public int getNumHumans() {
        return numHumans;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public double getPlasmaDrillsPower() {
        return plasmaDrillsPower;
    }

    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    public boolean isBrownAlien() {
        return brownAlien;
    }

    public int[] getShield() {
        return shield;
    }
}
