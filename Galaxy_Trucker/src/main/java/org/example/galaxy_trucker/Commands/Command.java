package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

// Aggiungi JsonTypeInfo per discriminare le sottoclassi
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "commandType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginCommand.class, name = "LoginCommand"),
        @JsonSubTypes.Type(value = QuitCommand.class, name = "QuitCommand"),
        @JsonSubTypes.Type(value = ReadyCommand.class, name = "ReadyCommand"),
        @JsonSubTypes.Type(value = BuildingCommand.class, name = "BuildingCommand"),
        @JsonSubTypes.Type(value = FinishBuildingCommand.class, name = "FinishBuildingCommand"),
        @JsonSubTypes.Type(value = DebugShip.class, name = "DebugShip"),
        @JsonSubTypes.Type(value = AddCrewCommand.class, name = "AddCrewCommand"),

        @JsonSubTypes.Type(value = RemoveTileCommand.class, name = "RemoveTileCommand"),
        @JsonSubTypes.Type(value = LoginCommand.class, names = "LobbyCommand"),
        @JsonSubTypes.Type(value = HandleCargoCommand.class, names = "HandleCargoCommand"),
        @JsonSubTypes.Type(value = TheftCommand.class, names = "TheftCommand"),
        @JsonSubTypes.Type(value = SelectChunkCommand.class, name = "SelectChunkCommand"),

        @JsonSubTypes.Type(value = ReconnectCommand.class, names = "ReconnectCommand")
//TODO: AGGIUNGERE ALTRI COMANDI IN JSONSUBTYPES PER TCP E LE JSONPROPERTY NELLE SOTTOCLASSI: IMPORTANTISSIMO REGA'
})
public abstract class Command implements Serializable {

    @JsonProperty("gameId")
    public String gameId;
    @JsonProperty("playerId")
    public String playerId;
    @JsonProperty("lv")
    public int lv;
    @JsonProperty("maxPlayers")
    int maxPlayers;
    @JsonProperty("title")
    public String title;
    @JsonProperty("token")
    public String token;

    public Command() {
        System.out.println("Command default called");
    }

    public Command(String gameId, String playerId, int lv, String title, String token, int maxPlayers) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
        this.title = title;
        this.token = token;
        this.maxPlayers = maxPlayers;
    }

    public abstract void execute(Player player) throws IOException, InterruptedException;

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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean allowedIn(PlayerState state) {
        return state.allows(this);
    }

    @JsonIgnore
    public ClientInterface getClient() {
        return null;
    }

    public String getToken() {
        return token;
    }
}
