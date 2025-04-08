package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Cards.Card;

public class AcceptCommand implements Command {

    Card card;
    boolean accepting;


    public AcceptCommand(Card card, boolean accepting) {
        this.card = card;
        this.accepting = accepting;
    }

    @Override
    public void execute() {
        card.continueCard(accepting);
    }
}
