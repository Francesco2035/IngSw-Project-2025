package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class ScoreboardEvent implements Event {

    private HashMap<String, Integer> scores = new HashMap<>();

    public HashMap<String, Integer> getScores() {
        return scores;
    }

    public ScoreboardEvent(){

    }

    @JsonCreator
    public ScoreboardEvent(@JsonProperty("scores") HashMap<String, Integer> scores) {
        this.scores = scores;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Override
    public String message() {
        return "";
    }
}
