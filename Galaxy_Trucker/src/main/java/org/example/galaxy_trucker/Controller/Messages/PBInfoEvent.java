package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PBInfoEvent implements Event {

    private int damage;
    private int credits;
    private int exposedConnectors;
    private int[] shield;
    private int numHumans = 0;
    private int EnginePower = 0;
    private double PlasmaDrillsPower = 0;
    private int Energy = 0;
    private boolean purpleAlien;
    private boolean brownAlien;




    public PBInfoEvent(@JsonProperty("damage") int damage, @JsonProperty("credits") int credits, @JsonProperty("exposedConnectors") int exposedConnectors,
                       @JsonProperty("shield") int[]shield, @JsonProperty("numHumans") int numHumans, @JsonProperty("EnginePower") int EnginePower,
                       @JsonProperty("PlasmaDrillPower") double plasmaDrillsPower, @JsonProperty("Energy") int energy,
                       @JsonProperty("purpleAlien") boolean purpleAlien, @JsonProperty("brownAlien") boolean brownAlien) {

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
}
