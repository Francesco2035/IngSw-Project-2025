package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.VirtualView;

public class QuitEvent implements Event {

    public QuitEvent() {

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
