package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.*;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Cards.CardEffect.DefendFromSmall;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

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
        @JsonSubTypes.Type(value = SelectChunkCommand.class, name = "SelectChunkCommand"),
        @JsonSubTypes.Type(value = ReconnectCommand.class, name = "ReconnectCommand"),
        @JsonSubTypes.Type(value = DefendFromLargeCommand.class, name = "DefendLarge"),
        @JsonSubTypes.Type(value = DefendFromSmallCommand.class, name = "DefendSmall"),
        @JsonSubTypes.Type(value = GiveSpeedCommand.class, name = "GiveSpeedCommand"),
        @JsonSubTypes.Type(value = GiveAttackCommand.class, name = "GiveAttackCommand"),
        @JsonSubTypes.Type(value = KillCommand.class, name = "KillCommand")



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
    @JsonCreator
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
