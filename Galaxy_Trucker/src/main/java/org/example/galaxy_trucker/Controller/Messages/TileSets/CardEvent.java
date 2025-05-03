package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

public class CardEvent  implements Event {

    private int id;

    @Override
    public String message() {
        return "";
    }

    @JsonCreator
    public CardEvent(@JsonProperty("id")int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public CardEvent(){

    }



}
