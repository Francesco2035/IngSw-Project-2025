package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Player;

public class FinishBuildingCommand extends Command {

    @JsonProperty("commandType")
    private final String commandType = "FinishBuildingCommand";

    @JsonProperty("index")
    private int index = -1;

    public FinishBuildingCommand() {}

    public FinishBuildingCommand(int index, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.index = index;
    }

    @Override
    public void execute(Player player){
        try {
            if (player.getCommonBoard().getLevel() == 1)
                player.EndConstruction();
            else
                player.EndConstruction(index);

            player.SetReady(true);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
