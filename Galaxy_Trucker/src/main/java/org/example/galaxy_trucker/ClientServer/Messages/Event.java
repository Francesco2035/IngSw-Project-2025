package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;

import java.io.Serializable;

/**
 * The Event interface represents a generic type of event within the system.
 * It serves as the base contract for all event-specific implementations, allowing different types of
 * events to be handled uniformly. This interface extends the Serializable interface, enabling events
 * to be serialized for networking or storage purposes.
 *
 * An Event is processed using the visitor pattern, where each specific implementation
 * of the Event interface allows a defined EventVisitor to process its behavior.
 *
 * Events may also include a descriptive message about their state or outcome,
 * provided by the message method. This string message can be utilized in logging
 * or user-facing feedback.
 *
 * The event framework relies on JSON annotations to handle polymorphic serialization
 * and deserialization, ensuring that different event types can be correctly processed
 * based on their specified type.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardEvent.class, name = "CardEvent"),
        @JsonSubTypes.Type(value = TileEvent.class, name = "TileEvent"),
        @JsonSubTypes.Type(value = VoidEvent.class, name = "VoidEvent"),
        @JsonSubTypes.Type(value = HandEvent.class, name = "HandEvent"),
        @JsonSubTypes.Type(value = CoveredTileSetEvent.class, name = "CoveredTileSetEvent"),
        @JsonSubTypes.Type(value = UncoverdTileSetEvent.class, name = "UncoveredTileSetEvent"),
        @JsonSubTypes.Type(value = DeckEvent.class, name = "DeckEvent"),
        @JsonSubTypes.Type(value = LobbyEvent.class, name = "LobbyEvent"),
        @JsonSubTypes.Type(value = GameLobbyEvent.class, name = "GameLobbyEvent"),
        @JsonSubTypes.Type(value = PhaseEvent.class, name = "PhaseEvent"),
        @JsonSubTypes.Type(value = RewardsEvent.class, name = "RewardsEvent"),
        @JsonSubTypes.Type(value = GameBoardEvent.class, name = "GameBoardEvent"),
        @JsonSubTypes.Type(value = ExceptionEvent.class, name = "ExceptionEvent"),
        @JsonSubTypes.Type(value = ConnectionRefusedEvent.class, name = "ConnectionRefusedEvent"),
        @JsonSubTypes.Type(value = PBInfoEvent.class, name = "PBInfoEvent"),
        @JsonSubTypes.Type(value = QuitEvent.class, name = "QuitEvent"),
        @JsonSubTypes.Type(value = HourglassEvent.class, name = "HourglassEvent"),
        @JsonSubTypes.Type(value = FinishGameEvent.class, name = "FinishGameEvent"),
        @JsonSubTypes.Type(value = LogEvent.class, name = "LogEvent"),
        @JsonSubTypes.Type(value = PlayerTileEvent.class, name = "PlayerTileEvent"),
        @JsonSubTypes.Type(value = ReconnectedEvent.class, name = "ReconnectedEvent"),
        @JsonSubTypes.Type(value = TokenEvent.class, name = "TokenEvent"),
        @JsonSubTypes.Type(value = ScoreboardEvent.class, name = "ScoreboardEvent")

})

public interface Event extends Serializable {


    /**
     * Accepts a visitor to perform an operation on this event.
     *
     * @param visitor the EventVisitor instance performing the operation
     */
    void accept(EventVisitor visitor);


    /**
     * Retrieves a descriptive message associated with the event.
     *
     * This method provides a message that represents the event's purpose
     * or status. The returned message can differ depending on the specific
     * implementation of the event.
     *
     * @return a string representing the event-specific message, which may
     *         vary across different event types.
     */
    String message();
}
