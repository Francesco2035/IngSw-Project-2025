package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

import java.util.ArrayList;

public class DeckEvent implements Event {

    private ArrayList<Integer> ids;

    @JsonCreator
    public DeckEvent(@JsonProperty("ids") ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public ArrayList<Integer> getIds() {
        return ids;
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
