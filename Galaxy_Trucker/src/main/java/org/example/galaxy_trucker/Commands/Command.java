package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.*;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Cards.CardEffect.DefendFromSmall;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a base abstract class for all game commands, providing a framework
 * for various command types within the game system. Each specific command type
 * extends this class to implement custom behavior.
 *
 * This class uses Jackson annotations to enable polymorphic serialization and deserialization
 * of different command types. It defines common properties and abstract behavior that all
 * command implementations must support.
 *
 * Command serialization utilizes the `commandType` property to distinguish between
 * subclasses during de/serialization.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "commandType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = AcceptCommand.class, name = "AcceptCommand"),
        @JsonSubTypes.Type(value = LoginCommand.class, name = "LoginCommand"),
        @JsonSubTypes.Type(value = ChoosingPlanetsCommand.class, name = "ChoosePlanet"),
        @JsonSubTypes.Type(value = ConsumeEnergyCommand.class, name = "ConsumeEnergy"),
        @JsonSubTypes.Type(value = QuitCommand.class, name = "QuitCommand"),
        @JsonSubTypes.Type(value = ReadyCommand.class, name = "ReadyCommand"),
        @JsonSubTypes.Type(value = BuildingCommand.class, name = "BuildingCommand"),
        @JsonSubTypes.Type(value = FinishBuildingCommand.class, name = "FinishBuildingCommand"),
        @JsonSubTypes.Type(value = DebugShip.class, name = "DebugShip"),
        @JsonSubTypes.Type(value = AddCrewCommand.class, name = "AddCrewCommand"),
        @JsonSubTypes.Type(value = RemoveTileCommand.class, name = "RemoveTileCommand"),
        @JsonSubTypes.Type(value = LobbyCommand.class, names = "LobbyCommand"),
        @JsonSubTypes.Type(value = HandleCargoCommand.class, names = "HandleCargoCommand"),
        @JsonSubTypes.Type(value = TheftCommand.class, names = "TheftCommand"),
        @JsonSubTypes.Type(value = Theft.class, names = "Theft"),
        @JsonSubTypes.Type(value = SelectChunkCommand.class, name = "SelectChunkCommand"),
        @JsonSubTypes.Type(value = ReconnectCommand.class, name = "ReconnectCommand"),
        @JsonSubTypes.Type(value = DefendFromLargeCommand.class, name = "DefendLarge"),
        @JsonSubTypes.Type(value = DefendFromSmallCommand.class, name = "DefendSmall"),
        @JsonSubTypes.Type(value = GiveSpeedCommand.class, name = "GiveSpeedCommand"),
        @JsonSubTypes.Type(value = GiveAttackCommand.class, name = "GiveAttackCommand"),
        @JsonSubTypes.Type(value = KillCommand.class, name = "KillCommand")
})
public abstract class Command implements Serializable {

    /**
     * Represents the unique identifier for a game associated with this command.
     * This field is used to distinguish and reference a specific game instance
     * in the context of the application. The `gameId` ensures that operations
     * or commands are executed within the correct game environment.
     */
    @JsonProperty("gameId")
    public String gameId;
    /**
     * Represents the unique identifier of the player associated with this command.
     * This ID is used to specify the player that the command is targeting or is executed by.
     *
     * The `playerId` serves as a crucial link between the command and the specific player,
     * enabling individualized actions or behaviors in the game context.
     *
     * It is serialized and deserialized using the `@JsonProperty` annotation to ensure smooth
     * data exchange in JSON-based communication.
     */
    @JsonProperty("playerId")
    public String playerId;
    /**
     * Represents the level (lv) associated with the command.
     * This value is used to denote the level at which the command operates
     * or is applicable within the game's logic.
     *
     * Annotated with @JsonProperty to allow serialization and deserialization
     * to and from JSON, ensuring the variable is correctly mapped during
     * data exchange processes.
     */
    @JsonProperty("lv")
    public int lv;
    /**
     * Represents the maximum number of players allowed in the context of a command.
     * This variable is used to define or retrieve the upper limit of participants
     * associated with the command or game instance.
     *
     * The value is typically set during initialization and is utilized for
     * validation or enforcement of player limits within a game scenario.
     */
    @JsonProperty("maxPlayers")
    int maxPlayers;
    /**
     * Represents the title or name of the command. This field is used to assign a descriptive
     * or identifiable name to the command for better understanding and reference in the game.
     * It is serialized and deserialized using Jackson annotations for seamless compatibility
     * with JSON-based data exchange.
     */
    @JsonProperty("title")
    public String title;
    /**
     * Represents the authentication or validation token associated with a command.
     * This field is used to ensure secure and authorized execution of commands
     * within the system.
     *
     * The token may be provided during the instantiation of a command and can be
     * retrieved for validation purposes or other logic requiring secure identification.
     *
     * This field is serialized and deserialized utilizing the Jackson annotation
     * for JSON compatibility.
     */
    @JsonProperty("token")
    public String token;

    /**
     * Default constructor for the Command class.
     * Initializes the Command instance with default values.
     */
    public Command() {
        System.out.println("Command default called");
    }
    /**
     * Constructs a Command object with specified parameters.
     *
     * @param gameId     the unique identifier of the game
     * @param playerId   the unique identifier of the player
     * @param lv         the level associated with the command
     * @param title      the title or name of the command
     * @param token      the token used for authentication or validation
     * @param maxPlayers the maximum number of players allowed
     */
    @JsonCreator
    public Command(String gameId, String playerId, int lv, String title, String token, int maxPlayers) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
        this.title = title;
        this.token = token;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Executes the command logic associated with the given player.
     *
     * @param player the player instance on which the command will operate
     * @throws IOException if an I/O error occurs during command execution
     * @throws InterruptedException if the execution is interrupted
     */
    public abstract void execute(Player player) throws IOException, InterruptedException;

    /**
     * Retrieves the title associated with the command.
     *
     * @return the title of the command as a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the player ID associated with this command.
     *
     * @return the player ID as a String.
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Retrieves the unique identifier of the game associated with this command.
     *
     * @return the gameId of the game as a String
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Retrieves the level of the command.
     *
     * @return the level (lv) associated with this command.
     */
    public int getLv() {
        return lv;
    }

    /**
     * Retrieves the maximum number of players allowed for this command.
     *
     * @return the maximum number of players allowed
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Determines whether the command is allowed in the given PlayerState.
     *
     * @param state the PlayerState in which the command's allowance will be checked
     * @return true if the command is allowed in the given state, false otherwise
     */
    public boolean allowedIn(PlayerState state) {
        return state.allows(this);
    }

    /**
     * Retrieves the client interface associated with this command.
     * This method is marked with @JsonIgnore to exclude it from serialization.
     *
     * @return the client interface if available, or null if none is set
     */
    @JsonIgnore
    public ClientInterface getClient() {
        return null;
    }

    /**
     * Retrieves the token associated with the command.
     *
     * @return the token as a String.
     */
    public String getToken() {
        return token;
    }
}
