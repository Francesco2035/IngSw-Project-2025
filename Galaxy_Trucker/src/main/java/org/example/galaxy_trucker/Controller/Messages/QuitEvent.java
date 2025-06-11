package org.example.galaxy_trucker.Controller.Messages;

import org.example.galaxy_trucker.Controller.VirtualView;

public class QuitEvent implements Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return "";
    }
}
