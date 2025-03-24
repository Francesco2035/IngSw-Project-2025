package org.example.galaxy_trucker.Model.InputHandlers;

import org.example.galaxy_trucker.Model.Cards.Card;

public class Accept implements InputHandler {
private boolean accepted;
private Card card;

public Accept(Card ActiveCard) {
    accepted = false;
    this.card = ActiveCard;
}

    public void setInput(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public void action() {
        card.continueCard(accepted);
    }
}
