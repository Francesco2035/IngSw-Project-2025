package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PBInfoEvent implements Event {

    private int credits;
    private int totValue;
    private int Energy = 0;
    private int exposedConnectors;
    private int damage;
    private int numHumans = 0;
    private int EnginePower = 0;
    private double PlasmaDrillsPower = 0;
    private boolean purpleAlien;
    private boolean brownAlien;
    private int[] shield;





    public PBInfoEvent(@JsonProperty("damage") int damage, @JsonProperty("credits") int credits, @JsonProperty("exposedConnectors") int exposedConnectors,
                       @JsonProperty("shield") int[]shield, @JsonProperty("numHumans") int numHumans, @JsonProperty("EnginePower") int EnginePower,
                       @JsonProperty("PlasmaDrillPower") double plasmaDrillsPower, @JsonProperty("Energy") int energy,
                       @JsonProperty("purpleAlien") boolean purpleAlien, @JsonProperty("brownAlien") boolean brownAlien,
                       @JsonProperty("totValue") int totValue) {

        this.totValue = totValue;
        this.damage = damage;
        this.credits = credits;
        this.exposedConnectors = exposedConnectors;
        this.shield = shield;
        this.numHumans = numHumans;
        this.EnginePower = EnginePower;
        this.brownAlien = brownAlien;
        this.purpleAlien = purpleAlien;
        this.Energy = energy;
        this.PlasmaDrillsPower = plasmaDrillsPower;



    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

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
        return Energy;
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
        return EnginePower;
    }

    public double getPlasmaDrillsPower() {
        return PlasmaDrillsPower;
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
