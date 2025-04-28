package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReconnectCommand extends Command{

    @JsonProperty("commandType")
    private final String commandType = "ReconnectCommand";
    public ReconnectCommand(String token, String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title, token);
    }
    public void execute() {
    }

    public ReconnectCommand() {

    }

}
