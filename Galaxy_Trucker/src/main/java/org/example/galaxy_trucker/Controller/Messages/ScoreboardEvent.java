package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class ScoreboardEvent implements Event {

    private HashMap<String, Integer> scores = new HashMap<>();

    public HashMap<String, Integer> getScores() {
        return scores;
    }

    @JsonCreator
    public ScoreboardEvent(@JsonProperty("scores") HashMap<String, Integer> scores) {
        this.scores = scores;
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
