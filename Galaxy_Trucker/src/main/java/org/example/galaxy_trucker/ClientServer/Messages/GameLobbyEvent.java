package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
public class GameLobbyEvent implements Event{

    ArrayList<String> players;
    ArrayList<Boolean> ready;

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public GameLobbyEvent() {

    }

    @JsonCreator
    public GameLobbyEvent(@JsonProperty("players") ArrayList<String> players,@JsonProperty("ready") ArrayList<Boolean> ready) {
        this.players = players;
        this.ready = ready;
    }

    @Override
    public String message() {
        return "";
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public ArrayList<Boolean> getReady() {
        return ready;
    }
}
