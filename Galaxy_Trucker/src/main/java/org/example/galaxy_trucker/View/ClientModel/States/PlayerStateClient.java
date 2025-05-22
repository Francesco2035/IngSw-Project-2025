package org.example.galaxy_trucker.View.ClientModel.States;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.View.GUI.GuiOut;
import org.example.galaxy_trucker.View.TUI.Out;
import org.example.galaxy_trucker.View.View;

import java.io.Serializable;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AcceptClient.class, name = "Accept"),
        @JsonSubTypes.Type(value = AddCrewClient.class, name = "AddCrew"),
        @JsonSubTypes.Type(value = BaseStateClient.class, name = "Base"),
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
        @JsonSubTypes.Type(value = WaitingClient.class, name = "Waiting")
})

public abstract class PlayerStateClient implements Serializable {



    public void setView() {

    }

    public void showGame(Out out) {
    }

    public void showGame(GuiOut out) {
    }
}
