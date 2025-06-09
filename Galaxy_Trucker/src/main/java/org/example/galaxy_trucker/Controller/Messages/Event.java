package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.awt.desktop.QuitEvent;
import java.io.Serializable;
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
        @JsonSubTypes.Type(value = GameBoardEvent.class, name = "GameBoardEvent" ),
        @JsonSubTypes.Type(value = GameBoardEvent.class, name ="RandomCardEffectEvent"),
        @JsonSubTypes.Type(value = GameBoardEvent.class, name = "GameBoardEvent" ),
        @JsonSubTypes.Type(value = ExceptionEvent.class, name = "ExceptionEvent" ),
        @JsonSubTypes.Type(value = ConnectionRefusedEvent.class, name = "ConnectionRefusedEvent" ),
        @JsonSubTypes.Type(value = PBInfoEvent.class, name = "PBInfoEvent"),
        @JsonSubTypes.Type(value = QuitEvent.class, name = "QuitEvent"),
        @JsonSubTypes.Type(value = HourglassEvent.class, name = "HourglassEvent")

})

public interface Event extends Serializable {


    public void accept(EventVisitor visitor);



    public String message();
}
