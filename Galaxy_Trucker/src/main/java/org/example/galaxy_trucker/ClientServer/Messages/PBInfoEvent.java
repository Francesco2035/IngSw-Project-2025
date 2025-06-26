package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The PBInfoEvent class represents an event containing various information
 * related to a player's board in a game. It implements the Event interface
 * and can be processed by an EventVisitor. This class is designed to
 * encapsulate the player's status and details such as credits, total value,
 * damage, energy, and other associated information.
 *
 * This class is a part of a polymorphic event system utilizing Jackson
 * annotations for serialization and deserialization.
 */
public class PBInfoEvent implements Event {

    /**
     * The credits field represents the number of credits associated with
     * the PBInfoEvent instance. Credits can be used to track in-game currency
     * or points collected by a player or an entity.
     */
    private int credits;
    /**
     * Represents the total value associated with a specific event.
     *
     * The `totValue` field is used to store an integer value that represents
     * the aggregated total or calculated value associated with the corresponding
     * event in the system. Its specific meaning and calculation are determined
     * by the context in which the event is used.
     */
    private int totValue;
    /**
     * Represents the energy level associated with a player's board in the game.
     *
     * This variable stores the current energy level, which could be used to power various
     * components or actions within the game. The energy value is initialized to zero
     * and may be modified throughout the game based on specific events, actions, or conditions.
     */
    private int energy = 0;
    /**
     * Represents the number of exposed connectors on a player's board or game element.
     *
     * This variable is used to track the count of connectors that are currently exposed,
     * potentially influencing gameplay decisions or outcomes related to connectivity
     * between various game components.
     */
    private int exposedConnectors;
    /**
     * Represents the damage attribute of the PBInfoEvent class.
     *
     * This variable holds the damage value associated with an event or entity.
     * It is utilized to track and manage the impact or harm caused under various
     * conditions within the system.
     */
    private int damage;
    /**
     * Represents the number of humans involved or present in the current context.
     * This variable is used to track the human count relevant to the PBInfoEvent.
     *
     * By default, its value is initialized to 0.
     */
    private int numHumans = 0;
    /**
     * Represents the engine power associated with an event in the system.
     *
     * This variable holds the power level of the engine, expressed as an integer.
     * It serves as a key attribute for calculations or representations related
     * to the engine's functionality or state. The value is initialized to 0 by
     * default and can be modified as per the requirements of the associated event.
     */
    private int enginePower = 0;
    /**
     * Represents the power level of the plasma drills in a player's spaceship.
     *
     * This variable stores the double precision value indicating the energy or power
     * output of the plasma drills, which may impact actions, capabilities, or performance
     * during gameplay. The value is initialized to 0 by default.
     */
    private double plasmaDrillsPower = 0;
    /**
     * Indicates whether a purple alien is present or associated with the current event.
     *
     * This variable is a boolean flag that represents the presence or involvement
     * of a purple alien in the context of the PBInfoEvent. It is used within the
     * system to track the status or condition related to purple aliens.
     */
    private boolean purpleAlien;
    /**
     * Represents the presence or absence of a brown alien in the event.
     *
     * This boolean variable indicates whether a brown alien is involved
     * in the current context of the event. It can be used to determine
     * specific scenarios or outcomes associated with the presence of this entity.
     *
     * A value of {@code true} signifies the involvement of a brown alien,
     * while {@code false} indicates otherwise.
     */
    private boolean brownAlien;
    /**
     * Represents the shield values for a spaceship in the game.
     *
     * This array holds information regarding shields, which protect the spaceship from damage
     * during specific events or interactions. Each element in the array corresponds to
     * a specific shield level or segment.
     *
     * The shield values can be used to determine the spaceship's defensive capabilities
     * and status within the game's mechanics.
     */
    private int[] shield;

    /**
     * Default constructor for the {@code PBInfoEvent} class.
     *
     * This no-argument constructor initializes an instance of the {@code PBInfoEvent} class,
     * which represents a specific type of event in the system. This class serves as part of
     * the event-handling mechanism, facilitating communication and interaction within the
     * application architecture.
     *
     * The {@code PBInfoEvent} is designed to encapsulate information related to the state
     * of a player board and implements the {@code Event} interface to allow for
     * visitor-based processing. This constructor may be used for creating instances of
     * {@code PBInfoEvent} before the actual properties are populated or when no specific
     * initialization is required at the time of object creation.
     */
    public PBInfoEvent() {

    }

    /**
     * Constructs a new PBInfoEvent instance with detailed information about a player's board state.
     *
     * @param damage the amount of damage sustained
     * @param credits the number of credits available
     * @param exposedConnectors the number of exposed connectors
     * @param shield the shield values as an array of integers
     * @param numHumans the number of humans present
     * @param enginePower the power of the engine
     * @param plasmaDrillsPower the power of the plasma drills
     * @param energy the amount of energy available
     * @param purpleAlien whether a purple alien is present (true if present, false otherwise)
     * @param brownAlien whether a brown alien is present (true if present, false otherwise)
     * @param totValue the total value associated with this event
     */
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

    /**
     * Accepts a visitor to process this PBInfoEvent.
     *
     * @param visitor the EventVisitor instance that will handle this PBInfoEvent
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to provide a descriptive string related to the event
     * represented by this class. For this specific implementation, it returns
     * an empty string indicating no specific message is associated.
     *
     * @return a string representing the message of the event; returns an empty string.
     */
    @JsonIgnore
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the current credits value.
     *
     * @return the number of credits as an integer.
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Retrieves the total value associated with this event.
     *
     * @return the total value as an integer.
     */
    public int getTotValue() {
        return totValue;
    }

    /**
     * Retrieves the energy value associated with this event.
     *
     * @return the energy value as an integer.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Retrieves the number of exposed connectors associated with this instance.
     *
     * @return the number of exposed connectors as an integer.
     */
    public int getExposedConnectors() {
        return exposedConnectors;
    }

    /**
     * Retrieves the damage value associated with this event.
     *
     * @return the damage value as an integer.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Retrieves the number of human entities associated with this instance.
     *
     * @return the number of humans as an integer.
     */
    public int getNumHumans() {
        return numHumans;
    }

    /**
     * Retrieves the engine power value.
     *
     * @return the engine power as an integer.
     */
    public int getEnginePower() {
        return enginePower;
    }

    /**
     * Retrieves the power level of the plasma drills.
     *
     * @return a double value representing the plasma drills' power.
     */
    public double getPlasmaDrillsPower() {
        return plasmaDrillsPower;
    }

    /**
     * Determines whether the associated entity is a purple alien.
     *
     * @return {@code true} if the entity is a purple alien; {@code false} otherwise.
     */
    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    /**
     * Determines whether the brown alien status is true.
     *
     * @return true if the brown alien is present, false otherwise.
     */
    public boolean isBrownAlien() {
        return brownAlien;
    }

    /**
     * Retrieves the shield values associated with this instance.
     *
     * @return an array of integers representing the shield values.
     */
    public int[] getShield() {
        return shield;
    }
}
