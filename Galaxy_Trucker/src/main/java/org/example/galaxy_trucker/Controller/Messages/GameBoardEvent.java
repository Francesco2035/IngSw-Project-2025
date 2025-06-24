package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameBoardEvent implements Event {


    private int position;
    private String playerID;


    @JsonCreator
    public GameBoardEvent(
            @JsonProperty("position") int position,
            @JsonProperty("playerID") String playerID) {

        this.position = position;
        this.playerID = playerID;
    }

    public GameBoardEvent() {}

    public int getPosition() {
        return position;
    }

    public String getPlayerID() {
        return playerID;
    }


    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return "";
    }

}
