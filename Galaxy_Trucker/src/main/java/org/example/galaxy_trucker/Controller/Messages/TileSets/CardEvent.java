package org.example.galaxy_trucker.Controller.Messages.TileSets;

import org.example.galaxy_trucker.Controller.Messages.Event;

public class CardEvent  implements Event {

    int id;

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


}
