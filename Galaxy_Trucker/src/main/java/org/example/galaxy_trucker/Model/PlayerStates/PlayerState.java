package org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.Model.Boards.Actions.*;
import org.example.galaxy_trucker.Model.Player;

public abstract class PlayerState {


    public boolean allows(Command command) {
        System.out.println("allows di def");
        return false;
    }

    public boolean allows(AcceptCommand command) {
        return false;
    }

    public boolean allows(AddCrewCommand command) {
        return false;
    }

    public boolean allows(ChoosingPlanetsCommand command) {
        return false;
    }

    public boolean allows(ConsumeEnergyCommand command) {
        return false;
    }

    public boolean allows(DefendFromLargeCommand command) {
        return false;
    }

    public boolean allows(DefendFromSmallCommand command) {
        return false;
    }

    public boolean allows(FinishBuildingCommand command) {
        return false;
    }

    public boolean allows(GiveAttackCommand command) {
        return false;
    }

    public boolean allows(GiveSpeedCommand command) {
        return false;
    }

    public boolean allows(HandleCargoCommand command) {
        return false;
    }

    public boolean allows(BuildingCommand command) {
        return false;
    }

    public boolean allows(KillCommand command) {
        return false;
    }

    public boolean allows(LoginCommand command) {

        return false;
    }

    public boolean allows(ReadyCommand command) {
        return false;
    }

    public boolean allows(RemoveTileCommand command) {
        return false;
    }






    public boolean allows(AddCrewAction action) {
        return false;
    }

    public boolean allows(AddGoodAction action) {
        return false;
    }

    public boolean allows(GetEnginePower action) {
        return false;
    }

    public boolean allows(GetGoodAction action) {
        return false;
    }

    public boolean allows(GetPlasmaDrillPower action) {
        return false;
    }

    public boolean allows(KillCrewAction action) {
        return false;
    }

    public boolean allows(UseEnergyAction action) {
        return false;
    }

    public boolean allows(DebugShip command){return false;}

    public Command createDefaultCommand(String gameId,Player player) {
        return null;
    }

    public void shouldAct(Player player) {
        player.SetHasActed(false);
    }

    public  boolean allows(SelectChunkCommand command){return false;};
}
