package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

// Aggiungi JsonTypeInfo per discriminare le sottoclassi
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "commandType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginCommand.class, name = "LoginCommand"),
        @JsonSubTypes.Type(value = ReadyCommand.class, name = "ReadyCommand"),
        @JsonSubTypes.Type(value = BuildingCommand.class, name = "BuildingCommand"),
        @JsonSubTypes.Type(value = FinishBuildingCommand.class, name = "FinishBuildingCommand"),
        @JsonSubTypes.Type(value = DebugShip.class, name = "DebugShip"),
        @JsonSubTypes.Type(value = AddCrewCommand.class, name = "AddCrewCommand"),

        @JsonSubTypes.Type(value = RemoveTileCommand.class, name = "RemoveTileCommand"),

        @JsonSubTypes.Type(value = ReconnectCommand.class, names = "ReconnectCommand")

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
    @JsonProperty("token")
    public String token;

    public Command() {
        System.out.println("Command default called");
    }

    public Command(String gameId, String playerId, int lv, String title, String token) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
        this.title = title;
        this.token = token;
    }

    public void execute(Player player) throws IOException {}

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

    @JsonIgnore
    public ClientInterface getClient() {
        return null;
    }

    public String getToken() {
        return token;
    }
}
