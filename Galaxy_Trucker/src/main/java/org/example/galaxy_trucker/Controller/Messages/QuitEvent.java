package org.example.galaxy_trucker.Controller.Messages;

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
