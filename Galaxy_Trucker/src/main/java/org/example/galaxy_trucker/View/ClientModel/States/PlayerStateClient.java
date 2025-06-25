package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;
import org.example.galaxy_trucker.View.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The PlayerStateClient class is an abstract representation of a player's state within the game.
 * It supports polymorphic behavior by allowing multiple concrete implementations corresponding to
 * specific game states. These states define the behavior and actions available to the player during
 * various phases of the game.
 *
 * The class is designed to serialize and deserialize concrete subclasses using the Jackson library,
 * leveraging annotations to manage JSON type information and handle various game state types.
 *
 * Each subclass of PlayerStateClient is expected to implement the abstract methods and may override
 * other methods, providing state-specific behavior.
 *
 * Features:
 * - Configured for polymorphic JSON serialization and deserialization with `JsonTypeInfo` and `JsonSubTypes`.
 * - Abstract methods to retrieve available commands for a specific game state.
 * - Methods to interface with different output mechanisms (`Out` and `GuiOut`) to render game state.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AcceptClient.class, name = "AcceptClient"),
        @JsonSubTypes.Type(value = AddCrewClient.class, name = "AddCrewClient"),
        @JsonSubTypes.Type(value = BaseStateClient.class, name = "BaseStateClient"),
        @JsonSubTypes.Type(value = BuildingClient.class, name = "BuildingClient"),
        @JsonSubTypes.Type(value = CheckValidityClient.class, name = "Validity"),
        @JsonSubTypes.Type(value = ChoosePositionClient.class, name = "Position"),
        @JsonSubTypes.Type(value = ChoosingPlanetClient.class, name = "Planet"),
        @JsonSubTypes.Type(value = ConsumingEnergyClient.class, name = "Energy"),
        @JsonSubTypes.Type(value = DefendingFromLargeClient.class, name = "Large"),
        @JsonSubTypes.Type(value = DefendingFromSmallClient.class, name = "Small"),
        @JsonSubTypes.Type(value = GiveAttackClient.class, name = "Attack"),
        @JsonSubTypes.Type(value = GiveSpeedClient.class, name = "Speed"),
        @JsonSubTypes.Type(value = HandleCargoClient.class, name = "Client"),
        @JsonSubTypes.Type(value = HandleDestructionClient.class, name = "Destruction"),
        @JsonSubTypes.Type(value = HandleTheftClient.class, name = "Theft"),
        @JsonSubTypes.Type(value = KillingClient.class, name = "Kill"),
        @JsonSubTypes.Type(value = WaitingClient.class, name = "Waiting"),
        @JsonSubTypes.Type(value = ReadCardClient.class, name  = "ReadCard")
})

public abstract class PlayerStateClient implements Serializable {

    /**
     * Constructs a new instance of the PlayerStateClient class.
     *
     * This constructor initializes the base class for representing
     * a player's abstract state in the game. Concrete implementations
     * are expected to provide specific behaviors and functionality
     * for various game states.
     */
    public PlayerStateClient(){

    }

    /**
     * Displays the current game state using the provided output mechanism.
     * This method is intended to render game information in a format defined
     * by the given `Out` implementation.
     *
     * @param out The output mechanism used to display the game state,
     *            which provides methods to handle and format output data.
     */
    public void showGame(Out out) {
    }

    /**
     * Displays the current game state using a graphical user interface.
     *
     * This method is responsible for rendering the game state on the provided `GuiOut`
     * object, which acts as a bridge to display various scenes and interact with the
     * JavaFX GUI framework.
     *
     * @param out the GuiOut object responsible for managing and rendering graphical scenes.
     */
    public void showGame(GuiOut out) {
    }

    /**
     * Retrieves the list of available commands for the current state of the player.
     * This method is abstract and must be implemented by subclasses of PlayerStateClient
     * to define specific commands relevant to the respective state.
     *
     * @return an ArrayList of Strings representing the available commands. Each string
     *         corresponds to a valid command that can be performed in the given state.
     *         The content and number of commands depend on the specific subclass implementation.
     */
    @JsonIgnore
    public abstract ArrayList<String> getCommands();
}
