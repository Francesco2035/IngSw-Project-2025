package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
//TODO: fixare, non serve riceverlo in fasi diverse dalla BaseState però comunque è mezzo buggato
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
