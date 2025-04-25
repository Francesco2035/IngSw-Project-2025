package org.example.galaxy_trucker.Controller.Messages.TileSets;

import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

public class CardEvent  implements Event {

    private int id;

    @Override
    public String message() {
        return "";
    }

    public CardEvent(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }



}
