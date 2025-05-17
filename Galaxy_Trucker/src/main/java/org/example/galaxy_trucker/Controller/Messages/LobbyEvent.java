package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class LobbyEvent implements Event {

    String gameId;
    int lv;
    ArrayList<String> players;


    public LobbyEvent() {

    }
    @JsonCreator
    public LobbyEvent(@JsonProperty("gameId")String gameId, @JsonProperty("lv")int lv, @JsonProperty("players") ArrayList<String> players) {
        this.gameId = gameId;
        this.players = players;
        this.lv = lv;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return "";
    }

    public ArrayList<String> getPlayers() {

        return this.players;

    }

    public int getLv() {
        return this.lv;
    }

    public String getGameId() {
        return this.gameId;
    }


}
