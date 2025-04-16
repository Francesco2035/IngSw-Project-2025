package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

// Aggiungi JsonTypeInfo per discriminare le sottoclassi
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "commandType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginCommand.class, name = "LoginCommand"),
        @JsonSubTypes.Type(value = ReadyCommand.class, name = "ReadyCommand"),
        @JsonSubTypes.Type(value = InsertTileCommand.class, name = "InsertTileCommand")
})
public class Command implements Serializable {

    @JsonProperty("gameId")
    public String gameId;
    @JsonProperty("playerId")
    public String playerId;
    @JsonProperty("lv")
    public int lv;
    @JsonProperty("title")
    public String title;

    public Command() {
        System.out.println("Command default called");
    }

    public Command(String gameId, String playerId, int lv, String title) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
        this.title = title;
    }

    public void execute(Player player) {
        // Esegui azioni per il comando
    }

    public String getTitle() {
        return title;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public int getLv() {
        return lv;
    }

    public boolean allowedIn(PlayerState state) {
        return state.allows(this);
    }
}
