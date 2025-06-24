package org.example.galaxy_trucker.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Messages.Event;
import org.example.galaxy_trucker.Messages.EventVisitor;

import java.util.ArrayList;

public class DeckEvent implements Event {

    private ArrayList<Integer> ids;

    // Costruttore con annotazione @JsonCreator per la deserializzazione
    @JsonCreator
    public DeckEvent(@JsonProperty("ids") ArrayList<Integer> ids) {
        this.ids = ids;
    }

    // Getter per ids
    public ArrayList<Integer> getIds() {
        return ids;
    }

    // Setter per ids
    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
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
