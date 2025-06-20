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

    public PlayerStateClient(){

    }

    public void showGame(Out out) {
    }

    public void showGame(GuiOut out) {
    }

    @JsonIgnore
    public abstract ArrayList<String> getCommands();
}
